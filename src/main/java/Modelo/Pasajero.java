package Modelo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "pasajero")
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni")
    private String dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "descuento")
    private final Double descuentoInfantil = 20.0;

    @Column(name = "precio")
    private Double precioBase;

    @ManyToOne
    @JoinColumn(name = "billete_id")
    private Billete billete;

    @OneToOne
    @JoinColumn(name = "asiento_id")
    private Asiento asiento;

}
