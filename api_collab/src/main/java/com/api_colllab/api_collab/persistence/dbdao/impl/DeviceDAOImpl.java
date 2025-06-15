package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.IDeviceDAO;
import com.api_colllab.api_collab.persistence.entity.DeviceEntityFormUser;
import com.api_colllab.api_collab.persistence.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class DeviceDAOImpl implements IDeviceDAO {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Optional<DeviceEntityFormUser> findById(Long device) {
        return deviceRepository.findById(device);
    }
}
