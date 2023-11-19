package utn.frc.sistemas.services.implementations;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import utn.frc.sistemas.entities.Estacion;
import utn.frc.sistemas.repositories.EstacionRepository;
import utn.frc.sistemas.services.implementations.exeptions.EstacionNotFoundException;
import utn.frc.sistemas.services.services.EstacionService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionServiceImpl implements EstacionService {

    private final EstacionRepository estacionRepository;

    public EstacionServiceImpl(EstacionRepository estacionRepository){
        this.estacionRepository = estacionRepository;
    }

    @Override
    public void add(Estacion entity) {
        this.estacionRepository.save(entity);
    }

    @Override
    public void update(Estacion entity) {
        this.estacionRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        this.estacionRepository.deleteById(id);
    }

    @Override
    public Estacion getById(Long id) {
        return this.estacionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Estacion> getAll() {
        return this.estacionRepository.findAll();
    }

    @Override
    public Estacion getEstacionMasCercana(Double latitud, Double longitud){
        List<Estacion> estaciones = this.estacionRepository.findAll();
        Double distancia = null;
        Estacion estacion_mas_cercana = null;
        for (Estacion estacion: estaciones) {
            Double nueva_distancia = Math.sqrt(Math.pow(estacion.getLatitud()-latitud,2)+Math.pow(estacion.getLongitud()-longitud,2));
            if (distancia != null){
                if (nueva_distancia < distancia) {
                    distancia = nueva_distancia;
                    estacion_mas_cercana = estacion;
                }
            } else {
                distancia = nueva_distancia;
                estacion_mas_cercana = estacion;
            }
        }
        return estacion_mas_cercana;
    }

    @Override
    public Double getDistancia(Long id_1, Long id_2) {
        Optional<Estacion> estacion_1 = this.estacionRepository.findById(id_1);
        Optional<Estacion> estacion_2 = this.estacionRepository.findById(id_2);

        if (estacion_1.isPresent() && estacion_2.isPresent()) {
            System.out.println("(" + estacion_1.get().getLatitud() + "," + estacion_1.get().getLongitud()+")");
            System.out.println("(" + estacion_2.get().getLatitud() + "," + estacion_2.get().getLongitud()+")");
            Estacion estacion_1_ = estacion_1.get();
            Estacion estacion_2_ = estacion_2.get();
            return Math.sqrt(Math.pow(estacion_1_.getLatitud() - estacion_2_.getLatitud(), 2)
                    + Math.pow(estacion_1_.getLongitud() - estacion_2_.getLongitud(), 2)) * 110;
        } else {
            if (!estacion_1.isPresent() && !estacion_2.isPresent()) {
                throw new EstacionNotFoundException("Ambas estaciones no fueron encontradas");
            } else if (!estacion_1.isPresent()) {
                throw new EstacionNotFoundException("Estación con ID " + id_1 + " no fue encontrada" );
            } else {
                throw new EstacionNotFoundException("Estación con ID " + id_2 + " no fue encontrada");
            }
        }
    }
}
