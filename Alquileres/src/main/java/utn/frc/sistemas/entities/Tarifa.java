package utn.frc.sistemas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TARIFAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(generator = "TARIFAS", strategy = GenerationType.TABLE)
    @TableGenerator(
            name = "TARIFAS",
            pkColumnValue = "TARIFAS", //
            table = "sqlite_sequence", // nombre por defecto de la tabla de valores autoincrementales de las PK
            pkColumnName = "name", // nombre de la columna de la tabla sqlite_sequence donde se guardan los nombres de las tablas
            valueColumnName = "seq", // nombre de la columna de la tabla sqlite_sequence donde se guarda la pk actual
            initialValue=1, // comienza en 1
            allocationSize=1 // aumenta de a 1
    )
    private Long id;

    @Column(name = "TIPO_TARIFA")
    private Integer tipo_tarifa;

    @Column(name = "DEFINICION")
    private String definicion;

    @Column(name = "DIA_SEMANA")
    private Integer dia_semana;

    @Column(name = "DIA_MES")
    private Integer dia_mes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private Float monto_fijo_alquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private Float monto_minuto_fraccion;

    @Column(name = "MONTO_KM")
    private Float monto_km;

    @Column(name = "MONTO_HORA")
    private Float monto_hora;
}
