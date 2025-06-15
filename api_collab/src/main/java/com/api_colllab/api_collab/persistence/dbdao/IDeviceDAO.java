package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.DeviceEntityFormUser;

import java.util.Optional;

public interface IDeviceDAO {

    Optional<DeviceEntityFormUser> findById(Long device);

}
