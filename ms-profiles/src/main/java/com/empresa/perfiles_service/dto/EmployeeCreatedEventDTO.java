package com.empresa.perfiles_service.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record EmployeeCreatedEventDTO(
        Long id,
        String name,
        String email,
        Long departmentId,
        Date hiringDate
) {
}
