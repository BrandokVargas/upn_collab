package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.controller.dto.*;
import com.api_colllab.api_collab.persistence.entity.*;
import com.api_colllab.api_collab.persistence.repository.CareerRepository;
import com.api_colllab.api_collab.persistence.repository.NivelRepository;
import com.api_colllab.api_collab.persistence.repository.RoleRepository;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import com.api_colllab.api_collab.services.sendEmail.SendEmailImpl;
import com.api_colllab.api_collab.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NivelServicesImpl nivelServices;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SendEmailImpl sendEmailImpl;
    @Autowired
    private CareerRepository careerRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Este email no existe"));

        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authoritiesList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authoritiesList);
    }


    public AuthResponse login(AuthLoginRequest authLoginRequest){
        String userEmail = authLoginRequest.email_upn();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(userEmail,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accesToken = jwtUtils.createToken(authentication,false);
        String refreshToken = jwtUtils.createToken(authentication,true);

        AuthResponse authResponse = new AuthResponse(userEmail,
                "Usuario Logueado correctamente",accesToken,refreshToken,true);
        return authResponse;
    }

    public AuthResponse refresh(String refreshToken) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(refreshToken);
        String userEmail = jwtUtils.extractUserEmail(decodedJWT);
        UserDetails userDetails = loadUserByUsername(userEmail);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        String newAccessToken = jwtUtils.createToken(authentication, false);

        return new AuthResponse(userEmail, "Token Refrescado correctamente", newAccessToken, refreshToken ,true);
    }

    public Authentication authenticate(String userEmail,String password){
        UserDetails userDetails = this.loadUserByUsername(userEmail);
        if(userDetails==null){
           throw new BadCredentialsException("Password y/o correo son incorrectos.");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("El Password es incorrecto");
        }

        return new UsernamePasswordAuthenticationToken(userEmail,userDetails.getPassword(),userDetails.getAuthorities());
    }


    public AuthResponse register(AuthRegisterRequest authRegisterRequest){
        String cod_upn = authRegisterRequest.cod_upn();
        String name = authRegisterRequest.name();
        String lastname = authRegisterRequest.lastname();
        String email = authRegisterRequest.email();

        List<String> careers = authRegisterRequest.careersRequest().nameCareers();

        if (!email.toUpperCase().contains("@UPN.PE")) {
            throw new IllegalArgumentException("NO ES UNA CUENTA UPN, INCLUYE @UPN.PE");
        }
        //Roles default
        List<String> roleDefault = List.of("USER");
        Set<RoleEntity> roles = roleRepository.findRoleEntitiesByRoleEnumIn(roleDefault)
                .stream().collect(Collectors.toSet());

        //Genración del password automatico
        String password = generatePassword(20);
        System.out.println(password);

        //Nivel por default
        String  nivel = "INV. APRENDIZ";
        NivelEntity addNivel = nivelServices.getNivelEntity(nivel);

        //Carreras
        List<CareerEntity> careersEntity = careerRepository.findCareerEntitiesByNameIn(careers);
        if(careersEntity.isEmpty()){
            throw new IllegalArgumentException("Alguna de las carreras especificadas no existen");
        }


        UserEntity userEntity = UserEntity.builder()
                .cod_upn(cod_upn)
                .name(name)
                .lastname(lastname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .dateRegister(LocalDate.now())
                .roles(roles)
                .nivel_id(addNivel)
                .careers(careersEntity)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userCreated.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userCreated.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(),
                userCreated.getPassword(),authorities);

        String accesToken = jwtUtils.createToken(authentication,false);
        String refreshToken = jwtUtils.createToken(authentication,true);
        AuthResponse authResponse = new AuthResponse(userCreated.getEmail(),
                "Usuario registrado correctamente",accesToken,refreshToken,true);

        CompletableFuture.runAsync(()-> {
            EmailRequest emailRequest = new EmailRequest(email,"CONTRASEÑA SEGURA",password);
            sendEmailImpl.sendEmail(emailRequest);
        });

        return authResponse;

    }

    private String generatePassword(int characters){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(characters);

        for (int i = 0; i < characters; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

}
