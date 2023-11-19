package utn.frc.sistemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.frc.sistemas.entities.Alquiler;

import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    @Query(value = "SELECT a FROM Alquiler a WHERE a.estado = :estado")
    List<Alquiler> findAllByEstado(Long estado);
}
