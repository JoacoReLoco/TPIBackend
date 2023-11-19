package utn.frc.sistemas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.frc.sistemas.entities.Tarifa;
import utn.frc.sistemas.services.services.TarifaService;

import java.util.List;

@RestController
@RequestMapping("/api/tarifa")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService){
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarifa>> getAll(){
        List<Tarifa> tarifas =  this.tarifaService.getAll();
        return ResponseEntity.ok(tarifas);
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody Tarifa tarifa){
        this.tarifaService.add(tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
