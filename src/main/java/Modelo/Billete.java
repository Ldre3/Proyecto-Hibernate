package Modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "billete")
public class Billete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_emision")
    private Date fechaEmision;

    @Column(name = "precio_final")
    private Double precioFinal;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    @OneToMany(mappedBy = "billete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pasajero> pasajeros;

}
