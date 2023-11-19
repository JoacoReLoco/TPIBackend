package utn.frc.sistemas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frc.sistemas.entities.Estacion;
import utn.frc.sistemas.services.implementations.exeptions.EstacionNotFoundException;
import utn.frc.sistemas.services.services.EstacionService;

import java.util.List;

@RestController
@RequestMapping("/api/estacion")
public class EstacionController {

    private final EstacionService estacionService;

    public EstacionController(EstacionService estacionService){
        this.estacionService = estacionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estacion> getEstacion(@PathVariable Long id){
        Estacion estacion = this.estacionService.getById(id);
        return ResponseEntity.ok(estacion);
    }

    @GetMapping
    public ResponseEntity<List<Estacion>> getAll(){
        List<Estacion> estaciones = this.estacionService.getAll();
        return ResponseEntity.ok(estaciones);
    }

    @GetMapping("/cercana")
    public ResponseEntity<Estacion> getEstacionMasCercana(@RequestParam Double latitud, @RequestParam Double longitud){
        Estacion estacion = this.estacionService.getEstacionMasCercana(latitud, longitud);
        return ResponseEntity.ok(estacion);
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Estacion estacion){
        this.estacionService.add(estacion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/distancia/{id_1}/{id_2}")
    public ResponseEntity<String> getDistancia(@PathVariable Long id_1, @PathVariable Long id_2){
        try{
            Double distancia = this.estacionService.getDistancia(id_1, id_2);
            return ResponseEntity.ok( String.valueOf(distancia));
        }
        catch (EstacionNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
