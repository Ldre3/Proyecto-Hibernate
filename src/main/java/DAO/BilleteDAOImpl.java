package DAO;

import Modelo.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Calendar.getInstance;

public class BilleteDAOImpl implements BilleteDAO{

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
            Calendar calendar = Calendar.getInstance();
            billete.setPrecioFinal(billete.getPasajeros().stream()
                    .mapToDouble(pasajero -> new Date().getYear() - pasajero.getFechaNacimiento().getYear() < 16?pasajero.getPrecioBase() - (pasajero.getPrecioBase() * (pasajero.getDescuentoInfantil() / 100)):pasajero.getPrecioBase())
                    .sum());
            session.merge(billete);
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
                    .allMatch(pasajero -> pasajero.getAsiento().getFila()==billete.getPasajeros().get(0).getAsiento().getFila())) return;
            AtomicBoolean asignado = new AtomicBoolean();
            asignado.set(false);

            billete.getVuelo().getAsientos().stream()
                    .map(Asiento::getFila).forEach(fila ->{
                        if (billete.getPasajeros().size() <= billete.getVuelo().getAsientos().stream().filter(asiento -> asiento.getFila() == fila && asiento.isLibre()).count() && !asignado.get()) {
                            billete.getPasajeros().forEach(pasajero -> {
                                Asiento asientoAntiguo = pasajero.getAsiento();
                                asientoAntiguo.setPasajero(null);
                                pasajero.setAsiento(billete.getVuelo().getAsientos().stream().filter(asiento -> asiento.getFila() == fila && asiento.isLibre() && Objects.equals(asiento.getVuelo().getId(), billete.getVuelo().getId())).findFirst().get());
                                Asiento asientoPasajero = pasajero.getAsiento();
                                asientoPasajero.setPasajero(pasajero);
                                billete.getVuelo().getAsientos().stream().filter(asiento -> asiento.getFila() == asientoPasajero.getFila() && asiento.getLetra().equals(asientoPasajero.getLetra())||asiento.getFila()==asientoAntiguo.getFila() && asiento.getLetra().equals(asientoAntiguo.getLetra())).forEach(asiento -> {
                                    asiento.setLibre(!asiento.isLibre());
                                    session.merge(asiento);
                                    System.out.println(asiento);
                                });
                                session.merge(pasajero);
                                System.out.println(pasajero);
                            });
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
}
