package com.uq.msdepartments.service.impl;

import com.uq.msdepartments.dto.DepartmentDTO;
import com.uq.msdepartments.entity.Department;
import com.uq.msdepartments.exception.DepartmentNotFoundException;
import com.uq.msdepartments.mapper.DepartmentMapper;
import com.uq.msdepartments.repository.DepartmentRepository;
import com.uq.msdepartments.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public DepartmentDTO create(DepartmentDTO dto) {
        Department entity = DepartmentMapper.toEntity(dto);
        entity.setId(null);
        return DepartmentMapper.toDto(repository.save(entity));
    }

    @Override
    public DepartmentDTO getById(Long id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        return DepartmentMapper.toDto(entity);
    }

    @Override
    public List<DepartmentDTO> getAll() {
        return repository.findAll().stream().map(DepartmentMapper::toDto).toList();
    }

    @Override
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department existing = repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        existing.setNombre(dto.getNombre());
        existing.setDescripcion(dto.getDescripcion());

        return DepartmentMapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new DepartmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
