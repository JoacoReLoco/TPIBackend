package utn.frc.sistemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.frc.sistemas.entities.Estacion;
@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {
}
