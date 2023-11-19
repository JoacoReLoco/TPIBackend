package utn.frc.sistemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.frc.sistemas.entities.Tarifa;

import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    @Query(value = "SELECT t FROM Tarifa t WHERE t.definicion = 'S' AND t.dia_semana = :diaSemana")
    List<Tarifa> findByDiaSemana(Integer diaSemana);

    @Query(value = "SELECT t FROM Tarifa t WHERE t.definicion = 'c' AND t.dia_mes = :dia AND t.mes = :mes AND t.anio = :anio")
    List<Tarifa> findByFecha(Integer dia, Integer mes, Integer anio);
}
