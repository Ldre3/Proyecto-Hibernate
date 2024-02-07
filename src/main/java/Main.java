import DAO.BilleteDAOImpl;
import DAO.LogicaVueloDAOImpl;
import Modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        // Abrir una sesión de Hibernate
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            // Iniciar una transacción
            transaction = session.beginTransaction();
            // Crear una instancia de Cliente
            Cliente cliente = new Cliente();
            cliente.setNombre("Juan");
            cliente.setCorreoElectronico("uno@dos.com");
            cliente.setBilletes(new ArrayList<>());
            // Crear instancias de Billete
            Billete billete1 = new Billete();
            billete1.setCliente(cliente);
            billete1.setFechaEmision(new Date());
            billete1.setPrecioFinal(100.0);
            Billete billete2 = new Billete();
            billete2.setCliente(cliente);
            billete2.setFechaEmision(new Date());
            billete2.setPrecioFinal(200.0);
            // Añadir los billetes al cliente
            cliente.getBilletes().add(billete1);
            cliente.getBilletes().add(billete2);
            // Crear vuelo
            Vuelo vuelo = new Vuelo();
            vuelo.setCodigo("V001");
            vuelo.setFechaVuelo(new Date());
            billete1.setVuelo(vuelo);
            billete2.setVuelo(vuelo);
            // Crear aeropuertos
            Aeropuerto aeropuertoOrigen = new Aeropuerto();
            aeropuertoOrigen.setCodigo("A001");
            aeropuertoOrigen.setCiudad("Madrid");
            Aeropuerto aeropuertoDestino = new Aeropuerto();
            aeropuertoDestino.setCodigo("A002");
            aeropuertoDestino.setCiudad("Barcelona");
            // Añadir los aeropuertos al vuelo
            vuelo.setAeropuertoOrigen(aeropuertoOrigen);
            vuelo.setAeropuertoDestino(aeropuertoDestino);
            aeropuertoOrigen.setVuelosOrigen(new ArrayList<>());
            aeropuertoOrigen.getVuelosOrigen().add(vuelo);
            aeropuertoDestino.setVuelosDestino(new ArrayList<>());
            aeropuertoDestino.getVuelosDestino().add(vuelo);
            // Crear asientos y añadirlos al vuelo
            vuelo.setAsientos(new ArrayList<>());
            Asiento asiento = new Asiento();
            asiento.setFila(1);
            asiento.setLetra("A");
            asiento.setVuelo(vuelo);
            Asiento asiento2 = new Asiento();
            asiento2.setFila(2);
            asiento2.setLetra("B");
            asiento2.setVuelo(vuelo);
            vuelo.getAsientos().add(asiento);
            vuelo.getAsientos().add(asiento2);
            Pasajero pasajero = new Pasajero();
            pasajero.setNombre("Pepe");
            pasajero.setDni("12345678A");
            pasajero.setPrecioBase(100.0);
            Date fechaNacimiento = new Date();
            fechaNacimiento.setYear(111);
            pasajero.setFechaNacimiento(fechaNacimiento);
            Pasajero pasajero2 = new Pasajero();
            pasajero2.setNombre("Pepe2");
            pasajero2.setDni("12345678A2");
            pasajero2.setPrecioBase(100.0);
            Date fechaNacimiento2 = new Date();
            fechaNacimiento2.setYear(70);
            pasajero2.setFechaNacimiento(fechaNacimiento2);
            pasajero.setBillete(billete1);
            pasajero2.setBillete(billete1);
            Pasajero pasajero3 = new Pasajero();
            pasajero3.setNombre("Pepe2");
            pasajero3.setDni("12345678A2");
            pasajero3.setPrecioBase(100.0);
            billete1.setPasajeros(new ArrayList<>());
            billete1.getPasajeros().add(pasajero);
            billete1.getPasajeros().add(pasajero2);
            billete1.getPasajeros().add(pasajero3);
            asiento.setPasajero(pasajero);
            asiento2.setPasajero(pasajero2);
            Asiento asiento3 = new Asiento();
            asiento3.setFila(4);
            asiento3.setLetra("C");
            asiento3.setVuelo(vuelo);
            asiento.setPasajero(pasajero3);
            vuelo.getAsientos().add(asiento3);
            pasajero3.setAsiento(asiento3);
            pasajero3.setBillete(billete1);
            pasajero.setAsiento(asiento);
            pasajero2.setAsiento(asiento2);

            for (int i = 0; i < 3; i++) {
                Asiento asiento4 = new Asiento();
                asiento4.setFila(5);
                asiento4.setLetra(String.valueOf((char) (i + 65)));
                asiento4.setLibre(true);
                asiento4.setVuelo(vuelo);
                vuelo.getAsientos().add(asiento4);
            }
            // Persistir
            session.save(cliente);
            session.save(vuelo);
            session.save(aeropuertoOrigen);
            session.save(aeropuertoDestino);
            session.save(pasajero);
            // Comprometer la transacción
            transaction.commit();
            SessionFactory session2 = HibernateUtils.getSessionFactory();
            BilleteDAOImpl billeteDAO = new BilleteDAOImpl(session2);
            billeteDAO.calcularPrecioFinal(billete1);
            billeteDAO.crearBillete(new Date(), 100.0, cliente, vuelo, new ArrayList<>());
            billeteDAO.reasignarAsientosFamilia(billete1);
            LogicaVueloDAOImpl logicaVueloDAO = new LogicaVueloDAOImpl(session2);
            logicaVueloDAO.creaVuelo737("V002", new Date());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cerrar la sesión
            session.close();
            // Cerrar la SessionFactory
            HibernateUtils.shutdown();
        }
    }
}
