package com.uq.msdepartments.service;

import com.uq.msdepartments.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentDTO create(DepartmentDTO dto);

    DepartmentDTO getById(Long id);

    List<DepartmentDTO> getAll();

    DepartmentDTO update(Long id, DepartmentDTO dto);

    void delete(Long id);
}
