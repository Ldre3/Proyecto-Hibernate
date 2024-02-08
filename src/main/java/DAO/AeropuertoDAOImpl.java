package DAO;

import Modelo.Aeropuerto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class AeropuertoDAOImpl implements GenericDao<Aeropuerto, Long>{
    private SessionFactory sessionFactory;

    public AeropuertoDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public void add(Aeropuerto aeropuerto) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(aeropuerto);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Aeropuerto getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Aeropuerto.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Aeropuerto> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Aeropuerto", Aeropuerto.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Aeropuerto aeropuerto) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(aeropuerto);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Aeropuerto aeropuerto) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(aeropuerto);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
