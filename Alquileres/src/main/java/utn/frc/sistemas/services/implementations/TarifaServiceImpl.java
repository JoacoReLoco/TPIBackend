package utn.frc.sistemas.services.implementations;

import org.springframework.stereotype.Service;
import utn.frc.sistemas.entities.Tarifa;
import utn.frc.sistemas.repositories.TarifaRepository;
import utn.frc.sistemas.services.services.TarifaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarifaServiceImpl implements TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaServiceImpl(TarifaRepository tarifaRepository){
        this.tarifaRepository = tarifaRepository;
    }

    @Override
    public void add(Tarifa entity) {
        this.tarifaRepository.save(entity);
    }

    @Override
    public void update(Tarifa entity) {
        this.tarifaRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        this.tarifaRepository.deleteById(id);
    }

    @Override
    public Tarifa getById(Long id) {
        return this.tarifaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tarifa> getAll() {
        return this.tarifaRepository.findAll();
    }

    @Override
    public Tarifa findByDiaSemana(Integer diaSemana) {
        List<Tarifa> tarifas = this.tarifaRepository.findByDiaSemana(diaSemana);
        System.out.println("tarifas");
        System.out.println(tarifas);
        if(tarifas.size() > 0){
            return tarifas.get(0);
        }else {
            throw new RuntimeException("No se encontró tarifa para el día de la semana: " + diaSemana);
        }
    }

    @Override
    public Tarifa findOrCreateByFecha(Integer dia, Integer mes, Integer anio) {
        List<Tarifa> tarifas = this.tarifaRepository.findByFecha(dia, mes, anio);
        if(tarifas.size() > 0){
            return tarifas.get(0);
        }else{
            Tarifa tarifa_referencia = this.tarifaRepository.findAll().get(0);

            Tarifa tarifa = new Tarifa();
            tarifa.setTipo_tarifa(1);
            tarifa.setDefinicion("C");
            tarifa.setDia_mes(mes);
            tarifa.setMes(mes);
            tarifa.setAnio(anio);
            tarifa.setMonto_fijo_alquiler(tarifa_referencia.getMonto_fijo_alquiler());
            tarifa.setMonto_minuto_fraccion(tarifa_referencia.getMonto_minuto_fraccion());
            tarifa.setMonto_hora(tarifa_referencia.getMonto_hora());
            tarifa.setMonto_km(tarifa_referencia.getMonto_km());
            this.tarifaRepository.save(tarifa);
            return tarifa;
        }
    }
}
