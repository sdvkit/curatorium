package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.GroupDto;

import java.util.List;

public interface GroupService {

    List<GroupDto> findAllByUser(String username);
}
