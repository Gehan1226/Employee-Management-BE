package edu.icet.demo.service;

import edu.icet.demo.dto.manager.ManagerResponse;

public interface ManagerService {
    ManagerResponse getByEmployeeId(Long id);
}
