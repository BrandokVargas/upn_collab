package com.api_colllab.api_collab.persistence.repository;


import com.api_colllab.api_collab.persistence.entity.DeviceEntityFormUser;
import com.api_colllab.api_collab.persistence.entity.StateForumEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DeviceRepository  extends CrudRepository<DeviceEntityFormUser,Long> {
    @Query("SELECT d FROM DeviceEntityFormUser d WHERE d.token_device = :token_device")
    DeviceEntityFormUser existDeviceId(@Param("token_device") String token_device);

    @Query("SELECT u FROM DeviceEntityFormUser u WHERE u.user.id = :user_id")
    Optional<DeviceEntityFormUser> findByUserId(@Param("user_id") Long user_id);

}
