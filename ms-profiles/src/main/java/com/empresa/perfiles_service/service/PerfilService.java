package com.empresa.perfiles_service.service;

import com.empresa.perfiles_service.dto.PerfilUpdateDTO;
import com.empresa.perfiles_service.mapper.PerfilMapper;
import com.empresa.perfiles_service.model.Perfil;
import com.empresa.perfiles_service.model.State;
import com.empresa.perfiles_service.repository.PerfilRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PerfilService {

    private final PerfilRepository repository;
    private final PerfilMapper mapper;

    public PerfilService(PerfilRepository repository,PerfilMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public Perfil crearPerfilPorDefecto(Long empleadoId, String nombre, String email) {

        if(repository.findByEmpleadoId(empleadoId).isPresent()){
            System.out.println("⚠️ Perfil ya existe: " + empleadoId);
            return null;
        }

        Perfil perfil = Perfil.builder()
                .id(UUID.randomUUID().toString())
                .empleadoId(empleadoId)
                .nombre(nombre)
                .email(email)
                .telefono("")
                .direccion("")
                .ciudad("")
                .biografia("")
                .fechaCreacion(LocalDateTime.now())
                .state(State.ENABLED)
                .build();

        return repository.save(perfil);
    }

    public List<Perfil> listarPerfiles() {
        return repository.findAll();
    }

    public Perfil obtenerPerfil(Long empleadoId) {
        return repository.findByEmpleadoId(empleadoId)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    public Perfil actualizarPerfil(Long empleadoId, PerfilUpdateDTO dto) {
        Perfil perfil = obtenerPerfil(empleadoId);
        mapper.actualizarPerfilDesdeDTO(dto, perfil);
        return repository.save(perfil);
    }

    public void desactivarPerfil(Long id){
        Perfil perfil = obtenerPerfil(id);
        perfil.setState(State.DISABLED);
        repository.save(perfil);
    }
}