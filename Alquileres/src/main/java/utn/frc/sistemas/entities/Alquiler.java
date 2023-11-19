package utn.frc.sistemas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "ALQUILERES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alquiler {
    @Id
    @GeneratedValue(generator = "ALQUILERES", strategy = GenerationType.TABLE)
    @TableGenerator(
            name = "ALQUILERES",
            pkColumnValue = "ALQUILERES",
            table = "sqlite_sequence", // nombre por defecto de la tabla de valores autoincrementales de las PK
            pkColumnName = "name", // nombre de la columna de la tabla sqlite_sequence donde se guardan los nombres de las tablas
            valueColumnName = "seq", // nombre de la columna de la tabla sqlite_sequence donde se guarda la pk actual
            initialValue=1, // comienza en 1
            allocationSize=1 // aumenta de a 1
    )
    private Long id;

    @Column(name = "ID_CLIENTE")
    private String id_cliente;

    @Column(name = "ESTADO")
    private Integer estado = 1;

    @Column(name = "ESTACION_RETIRO")
    private Long estacionRetiroId;

    @Column(name = "ESTACION_DEVOLUCION")
    private Long estacion_devolucion;

    @Column(name = "FECHA_HORA_RETIRO")
    private Timestamp fecha_hora_retiro = Timestamp.from(Instant.now());

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private Timestamp fecha_hora_devolucion;

    @Column(name = "MONTO")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa tarifa;

    @Transient
    private String moneda = "ARS";
}
