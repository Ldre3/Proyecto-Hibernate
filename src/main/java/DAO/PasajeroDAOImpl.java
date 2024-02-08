package DAO;

import Modelo.Pasajero;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PasajeroDAOImpl implements GenericDao<Pasajero, Long>{
    private SessionFactory sessionFactory;
    public PasajeroDAOImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}


    @Override
    public void add(Pasajero pasajero) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(pasajero);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Pasajero getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Pasajero.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Pasajero> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Pasajero", Pasajero.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Pasajero pasajero) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(pasajero);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Pasajero pasajero) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(pasajero);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
