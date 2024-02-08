package DAO;

import Modelo.Asiento;
import Modelo.Vuelo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class LogicaVueloDAOImpl implements LogicaVueloDAO, GenericDao<Vuelo, Long> {
    private SessionFactory sessionFactory;

    public LogicaVueloDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public boolean creaVuelo737(String codigo, Date fechaVuelo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            // Comprobar si existe un vuelo con el mismo código
            if (session.createQuery("FROM Vuelo", Vuelo.class).getResultList().stream().anyMatch(vuelo -> vuelo.getCodigo().equals(codigo))) return false;
            transaction = session.beginTransaction();
            Vuelo vuelo = new Vuelo();
            vuelo.setCodigo(codigo);
            vuelo.setFechaVuelo(fechaVuelo);
            vuelo.setAsientos(new ArrayList<>());
            IntStream.rangeClosed(1, 31)
                    .forEach(filas -> IntStream.rangeClosed(0, 5)
                            .forEach(letra -> {
                                Asiento asiento = new Asiento();
                                asiento.setFila(filas);
                                asiento.setLetra(String.valueOf((char) (letra + 65)));
                                asiento.setLibre(true);
                                Character tipo = filas<=5?'A':'B';
                                asiento.setTipo(tipo);
                                asiento.setVuelo(vuelo);
                                vuelo.getAsientos().add(asiento);
                            })
                    );
            session.save(vuelo);
            transaction.commit();
            return true;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public void add(Vuelo vuelo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(vuelo);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Vuelo getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Vuelo.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Vuelo> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Vuelo", Vuelo.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Vuelo vuelo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(vuelo);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void delete(Vuelo vuelo) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(vuelo);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
