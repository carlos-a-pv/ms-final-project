package com.empresa.perfiles_service.mapper;

import com.empresa.perfiles_service.dto.PerfilResponseDTO;
import com.empresa.perfiles_service.dto.PerfilUpdateDTO;
import com.empresa.perfiles_service.model.Perfil;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PerfilMapper {

    PerfilResponseDTO toResponse(Perfil perfil);
    List<PerfilResponseDTO> toResponseList(List<Perfil> perfiles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarPerfilDesdeDTO(PerfilUpdateDTO dto, @MappingTarget Perfil perfil);
}
