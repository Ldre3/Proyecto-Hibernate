package DAO;

import Modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class BilleteDAOImpl implements BilleteDAO, GenericDao<Billete, Long>{

    private SessionFactory sessionFactory;

    public BilleteDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void calcularPrecioFinal(Billete billete) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            billete.setPrecioFinal(billete.getPasajeros().stream()
                    .mapToDouble(pasajero -> new Date().getYear() - pasajero.getFechaNacimiento().getYear() < 16?pasajero.getPrecioBase() - (pasajero.getPrecioBase() * (pasajero.getDescuentoInfantil() / 100)):pasajero.getPrecioBase())
                    // Si el pasajero es menor de 16 años se le aplica el descuento infantil que tiene asociado
                    .sum()); // Sumamos el precio de todos los pasajeros
            session.merge(billete); // Actualizamos el billete
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        }finally {
            session.close();
        }
    }

    @Override
    public void reasignarAsientosFamilia(Billete billete) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (billete.getPasajeros().stream()
                    .allMatch(pasajero -> pasajero.getAsiento().getFila()==billete.getPasajeros().get(0).getAsiento().getFila())) return; // Si todos los pasajeros ya están en la misma fila no hacemos nada
            AtomicBoolean asignado = new AtomicBoolean(); // Variable para saber si ya se ha asignado un asiento, tiene que ser atomica para poder ser modificada dentro de un stream
            asignado.set(false);
            billete.getVuelo().getAsientos().stream()
                    .map(Asiento::getFila).forEach(fila ->{ // Para cada fila
                        // Si hay suficientes asientos libres en la fila y no se ha asignado ya
                        if (billete.getPasajeros().size() <= billete.getVuelo().getAsientos().stream()
                                .filter(asiento -> asiento.getFila() == fila && asiento.isLibre())
                                .count() && !asignado.get())  {
                            // Para cada pasajero
                            billete.getPasajeros().forEach(pasajero -> {
                                // Cambiamos el asiento del pasajero
                                Asiento asientoAntiguo = pasajero.getAsiento();
                                asientoAntiguo.setPasajero(null);
                                // Buscamos el primer asiento libre en la fila para ese vuelo y se lo asignamos al pasajero
                                pasajero.setAsiento(billete.getVuelo().getAsientos().stream()
                                        .filter(asiento -> asiento.getFila() == fila && asiento.isLibre() && Objects.equals(asiento.getVuelo().getId(), billete.getVuelo().getId()))
                                        .findFirst()
                                        .get());
                                Asiento asientoPasajero = pasajero.getAsiento();
                                asientoPasajero.setPasajero(pasajero);
                                // Cambiamos el estado de los asientos que hemos modificado y lo perisistimos
                                billete.getVuelo().getAsientos().stream()
                                        .filter(asiento -> asiento.getFila() == asientoPasajero.getFila() && asiento.getLetra().equals(asientoPasajero.getLetra())||asiento.getFila()==asientoAntiguo.getFila() && asiento.getLetra().equals(asientoAntiguo.getLetra()))
                                        .forEach(asiento -> {
                                    asiento.setLibre(!asiento.isLibre());
                                    session.merge(asiento);
                                });
                            });
                            // Cambiamos la variable para que no reasigne más asientos
                            asignado.set(true);
                        }
                    });
            session.merge(billete);
            transaction.commit();
        }catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            System.out.println(e.getMessage());

        }finally {
            session.close();
        }
    }

    @Override
    public void crearBillete(Date fechaEmision, Double precioFinal, Cliente cliente, Vuelo vuelo, List<Pasajero> pasajeros){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Billete billete = new Billete();
            billete.setFechaEmision(fechaEmision);
            billete.setPrecioFinal(precioFinal);
            billete.setCliente(cliente);
            billete.setVuelo(vuelo);
            billete.setPasajeros(pasajeros);
            session.save(billete);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void add(Billete billete) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(billete);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Billete getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Billete.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Billete> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Billete", Billete.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Billete billete) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(billete);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Billete entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
