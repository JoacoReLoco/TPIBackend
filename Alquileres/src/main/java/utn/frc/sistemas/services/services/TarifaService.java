package utn.frc.sistemas.services.services;

import utn.frc.sistemas.entities.Tarifa;
import utn.frc.sistemas.services.Service;

import java.time.LocalDateTime;

public interface TarifaService extends Service<Tarifa, Long> {
    public Tarifa findByDiaSemana(Integer diaSemana);

    public Tarifa findOrCreateByFecha(Integer dia, Integer mes, Integer anio);
}
