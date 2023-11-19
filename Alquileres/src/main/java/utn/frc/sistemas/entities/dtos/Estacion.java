package utn.frc.sistemas.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estacion {
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private Timestamp fecha_hora_creacion;

}
