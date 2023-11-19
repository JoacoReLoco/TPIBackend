package utn.frc.sistemas.services.implementations;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import utn.frc.sistemas.entities.Alquiler;
import utn.frc.sistemas.entities.Tarifa;
import utn.frc.sistemas.entities.dtos.Estacion;
import utn.frc.sistemas.repositories.AlquilerRepository;
import utn.frc.sistemas.services.implementations.exeptions.AlquilerNotFoundException;
import utn.frc.sistemas.services.implementations.exeptions.EstacionNotFoundException;
import utn.frc.sistemas.services.services.AlquilerService;
import utn.frc.sistemas.services.services.TarifaService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlquilerServiceImpl implements AlquilerService {
    private static final String EstacionesMicroserviceUrl = "http://localhost:8082/";
    private final String monedaApiUrl = "http://34.82.105.125:8080/convertir";

    private final AlquilerRepository alquilerRepository;

    private final TarifaService tarifaService;

    public AlquilerServiceImpl(AlquilerRepository alquilerRepository, TarifaService tarifaService){
        this.alquilerRepository = alquilerRepository;
        this.tarifaService = tarifaService;
    }

    @Override
    public void add(Alquiler entity) {
        this.alquilerRepository.save(entity);
    }

    @Override
    public void update(Alquiler entity) {
        this.alquilerRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        this.alquilerRepository.deleteById(id);
    }

    @Override
    public Alquiler getById(Long id) {
        return this.alquilerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Alquiler> getAll() {
        return this.alquilerRepository.findAll();
    }

    @Override
    public List<Alquiler> getAllByEstado(Long estado) {
        return this.alquilerRepository.findAllByEstado(estado);
    }

    @Override
    public void addByEstacionId(Long estacion_id){
        try {
            Alquiler nuevo_alquiler = new Alquiler();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Estacion> res = restTemplate.getForEntity(EstacionesMicroserviceUrl + "api/estacion/" + estacion_id, Estacion.class);
            if (res.getStatusCode().is2xxSuccessful()){
                Estacion estacion = res.getBody();
                nuevo_alquiler.setEstacionRetiroId(estacion.getId());
                this.alquilerRepository.save(nuevo_alquiler);
            }
            else {
                throw new EstacionNotFoundException("No se encontró la estación con ID: " + estacion_id);
            }
        }
        catch (HttpClientErrorException ex) {
            throw new EstacionNotFoundException("No se encontró la estación con ID: " + estacion_id);
        }
    }

    @Override
    public Alquiler finalizarById(Long id, Long estacion_devolucion_id, String moneda){
        Optional<Alquiler> alquiler = this.alquilerRepository.findById(id);
        if (alquiler.isEmpty()) {
            throw new AlquilerNotFoundException("No se encontró el alquiler con ID: " + id);
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Estacion> res = restTemplate.getForEntity(EstacionesMicroserviceUrl + "api/estacion/" + estacion_devolucion_id, Estacion.class);

            if (res.getStatusCode().is2xxSuccessful()) {

                Alquiler alquiler_finalizado = alquiler.get();

                Timestamp fecha_hora_devolucion = new Timestamp(System.currentTimeMillis());

                Long duracion_minutos = (fecha_hora_devolucion.getTime() - alquiler_finalizado.getFecha_hora_retiro().getTime()) / 60000;

                LocalDateTime fecha_hora_retiro = alquiler_finalizado.getFecha_hora_retiro().toLocalDateTime();
                LocalDateTime fecha_hora_devolucion_local = fecha_hora_devolucion.toLocalDateTime();

                Tarifa tarifa;
                if (fecha_hora_retiro.getDayOfMonth() == fecha_hora_devolucion_local.getDayOfMonth() && fecha_hora_retiro.getMonth() == fecha_hora_devolucion_local.getMonth() && fecha_hora_retiro.getYear() == fecha_hora_devolucion_local.getYear()) {
                    // definido por dia de la semana
                    tarifa = this.tarifaService.findByDiaSemana(fecha_hora_retiro.getDayOfWeek().getValue());
                } else {
                    // definido por fecha
                    Integer dia = fecha_hora_devolucion_local.getDayOfMonth();
                    Integer mes = fecha_hora_devolucion_local.getMonthValue();
                    Integer anio = fecha_hora_devolucion_local.getYear();
                    tarifa = this.tarifaService.findOrCreateByFecha(dia, mes, anio);
                }

                Double monto = Double.valueOf(tarifa.getMonto_fijo_alquiler());
                System.out.println("monto inicial: $" + monto + " (monto fijo): $" + tarifa.getMonto_fijo_alquiler());

                if (duracion_minutos < 30) {
//                System.out.println("La diferencia es menor que 30 minutos.");
                    monto += (double) (duracion_minutos * tarifa.getMonto_minuto_fraccion());
                } else {
//                System.out.println("La diferencia es igual o mayor que 30 minutos.");
                    monto += (double) (duracion_minutos / 60 * tarifa.getMonto_hora());
                }

                Long e_ret_id = alquiler_finalizado.getEstacionRetiroId();
                try {
                    RestTemplate restTemplate2 = new RestTemplate();
                    Long distancia_en_km = restTemplate2.getForObject(EstacionesMicroserviceUrl + "api/estacion/distancia/" + e_ret_id + "/" + res.getBody().getId(), Long.class);

                    monto += distancia_en_km * tarifa.getMonto_km();

                    alquiler_finalizado.setEstado(2);
                    alquiler_finalizado.setTarifa(tarifa);
                    alquiler_finalizado.setMonto(monto);
                    alquiler_finalizado.setFecha_hora_devolucion(fecha_hora_devolucion);
                    alquiler_finalizado.setEstacion_devolucion(estacion_devolucion_id);
                    alquiler_finalizado.setEstado(2);
                    this.alquilerRepository.save(alquiler_finalizado);

                    Map<String, Object> externalApiRequest = Map.of(
                            "moneda_destino", moneda,
                            "importe", monto
                    );
                    // Convertir a la moneda especificada
                    if (!moneda.equals("ARS")) {
                        RestTemplate restTemplate3 = new RestTemplate();
                        Map<String, Object> externalApiResponse = restTemplate3.postForObject(monedaApiUrl, externalApiRequest, Map.class);
                        monto = (Double) externalApiResponse.get("importe");
                        alquiler_finalizado.setMonto(monto);
                        alquiler_finalizado.setMoneda(moneda);
                    }

                    return alquiler_finalizado;
                } catch (HttpClientErrorException ex) {
                    throw new AlquilerNotFoundException(ex.getMessage());
                }
            }
            else {
                throw new AlquilerNotFoundException("No se encontró la estación con ID: " + estacion_devolucion_id);
            }
        }
        catch (HttpClientErrorException ex) {
            throw new AlquilerNotFoundException("No se encontró la estación con ID: " + estacion_devolucion_id);
        }
    }}
