package utn.frc.sistemas.services.services;

import utn.frc.sistemas.entities.Estacion;
import utn.frc.sistemas.services.Service;

public interface EstacionService extends Service<Estacion, Long> {
    Estacion getEstacionMasCercana(Double longitud, Double latitud);

    Double getDistancia(Long id_1, Long id_2);
}
