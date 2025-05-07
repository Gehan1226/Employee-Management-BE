package edu.icet.demo.service.impl;

import edu.icet.demo.dto.manager.ManagerResponse;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.ManagerRepository;
import edu.icet.demo.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ModelMapper modelMapper;

    @Override
    public ManagerResponse getByEmployeeId(Long id) {
        try {
            return modelMapper.map(managerRepository.findByEmployeeId(id), ManagerResponse.class);
        } catch (Exception e) {
            throw new UnexpectedException("Unexpected error occurred while retrieving manager by employee ID: " + id);
        }
    }
}
