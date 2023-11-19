package utn.frc.sistemas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "ESTACIONES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estacion {
    @Id
    @GeneratedValue(generator = "ESTACIONES", strategy = GenerationType.TABLE)
    @TableGenerator(
            name = "ESTACIONES",
            pkColumnValue = "ESTACIONES",
            table = "sqlite_sequence", // nombre por defecto de la tabla de valores autoincrementales de las PK
            pkColumnName = "name", // nombre de la columna de la tabla sqlite_sequence donde se guardan los nombres de las tablas
            valueColumnName = "seq", // nombre de la columna de la tabla sqlite_sequence donde se guarda la pk actual
            initialValue=1, // comienza en 1
            allocationSize=1 // aumenta de a 1
    )
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_HORA_CREACION")
    private Timestamp fecha_hora_creacion;

    @Column(name = "LATITUD")
    private Double latitud;

    @Column(name = "LONGITUD")
    private Double longitud;
}
