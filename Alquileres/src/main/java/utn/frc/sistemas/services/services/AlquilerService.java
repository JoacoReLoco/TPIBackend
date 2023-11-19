package utn.frc.sistemas.services.services;


import utn.frc.sistemas.entities.Alquiler;
import utn.frc.sistemas.services.Service;

import java.util.List;

public interface AlquilerService extends Service<Alquiler, Long> {
    public void addByEstacionId(Long estacion_id);

    public Alquiler finalizarById(Long id, Long estacion_devolucion_id, String moneda);

    public List<Alquiler> getAllByEstado(Long estado);
}
