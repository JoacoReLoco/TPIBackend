package utn.frc.sistemas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import utn.frc.sistemas.entities.Alquiler;
import utn.frc.sistemas.services.implementations.exeptions.EstacionNotFoundException;
import utn.frc.sistemas.services.services.AlquilerService;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService  ){
        this.alquilerService = alquilerService;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAll(@RequestParam(name = "estado", required = false) Long estado){
        List<Alquiler> alquileres;
        if (estado != null) {
            alquileres = this.alquilerService.getAllByEstado(estado);
        } else {
            alquileres = this.alquilerService.getAll();
        }
        return ResponseEntity.ok(alquileres);
    }

    @PostMapping
    public ResponseEntity<Void> iniciarAlquiler(@RequestBody Map<String, Long> requestBody){
        try{
            this.alquilerService.addByEstacionId(requestBody.get("estacion_id"));
            return ResponseEntity.created(null).build();

        }
        catch (EstacionNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping("/finalizar/{alquilerId}")
    public ResponseEntity<Alquiler> finalizarAlquiler(@PathVariable Long alquilerId,
                                                                 @RequestParam(required = false, defaultValue = "ARS") String moneda,
                                                                 @RequestBody Map<String, String> requestBody) {
        // Obtener alquiler por ID
        Long estacion_devolucion_id = Long.valueOf(requestBody.get("estacion_devolucion_id"));

        Alquiler alquiler = this.alquilerService.finalizarById(alquilerId, estacion_devolucion_id, moneda);

        return ResponseEntity.ok(alquiler);}
}
