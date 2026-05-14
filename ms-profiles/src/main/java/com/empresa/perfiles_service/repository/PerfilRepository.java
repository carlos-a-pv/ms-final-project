package com.empresa.perfiles_service.repository;
import com.empresa.perfiles_service.model.Perfil;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PerfilRepository extends MongoRepository<Perfil, Long> {
    Optional<Perfil> findByEmpleadoId(Long empleadoId);
}
