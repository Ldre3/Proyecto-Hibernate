package DAO;

import Modelo.Billete;
import Modelo.Cliente;
import Modelo.Pasajero;
import Modelo.Vuelo;

import java.util.Date;
import java.util.List;

public interface BilleteDAO {
    public void calcularPrecioFinal(Billete billete);
    public void reasignarAsientosFamilia(Billete billete);
    public void crearBillete(Date fechaEmision, Double precioFinal, Cliente cliente, Vuelo vuelo, List<Pasajero> pasajeros);
}
