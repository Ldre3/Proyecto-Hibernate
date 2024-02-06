package Modelo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "aeropuerto")
public class Aeropuerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "ciudad")
    private String ciudad;

    @OneToMany(mappedBy = "aeropuertoOrigen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vuelo> vuelosOrigen;

    @OneToMany(mappedBy = "aeropuertoDestino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vuelo> vuelosDestino;


}


