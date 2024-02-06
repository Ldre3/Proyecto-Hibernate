package Modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vuelo")
@Data
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_vuelo")
    private Date fechaVuelo;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Billete> billetes;

    //@ManyToOne
//    @JoinColumn(name = "aeropuerto_origen_id", nullable = false)
//    private Aeropuerto aeropuertoOrigen;
//
//    @ManyToOne
//    @JoinColumn(name = "aeropuerto_destino_id", nullable = false)
//    private Aeropuerto aeropuertoDestino;


}
