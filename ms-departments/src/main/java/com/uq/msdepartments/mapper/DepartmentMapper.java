package com.uq.msdepartments.mapper;

import com.uq.msdepartments.dto.DepartmentDTO;
import com.uq.msdepartments.entity.Department;

public final class DepartmentMapper {

    private DepartmentMapper() {
    }

    public static DepartmentDTO toDto(Department entity) {
        if (entity == null) {
            return null;
        }
        return DepartmentDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public static Department toEntity(DepartmentDTO dto) {
        if (dto == null) {
            return null;
        }
        return Department.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }
}
