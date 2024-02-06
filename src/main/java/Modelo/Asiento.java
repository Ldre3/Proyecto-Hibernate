package Modelo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "asiento")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fila")
    private int fila;

    @Column(name = "letra")
    private String letra;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    @OneToOne(mappedBy = "asiento")
    private Pasajero pasajero;

}
