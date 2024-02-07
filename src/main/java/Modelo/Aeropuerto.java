package Modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "aeropuertoOrigen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vuelo> vuelosOrigen;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "aeropuertoDestino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vuelo> vuelosDestino;


}


