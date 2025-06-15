package com.api_colllab.api_collab.services.tokenMessagin;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FirebaseAuthHelper {

    private static final String[] SCOPES = {"https://www.googleapis.com/auth/firebase.messaging"};

    public static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("C:\\Users\\USUARIO\\Desktop\\key\\collabnotification-b6498-firebase-adminsdk-7c3g0-0cd2353cba.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refresh();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
