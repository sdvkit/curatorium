package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
}
