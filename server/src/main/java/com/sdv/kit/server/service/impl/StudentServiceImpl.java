package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
}
