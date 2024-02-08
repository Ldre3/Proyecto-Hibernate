package DAO;

import Modelo.Asiento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class AsientoDAOImpl implements GenericDao<Asiento, Long>{
    private SessionFactory sessionFactory;

    public AsientoDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void add(Asiento entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Asiento getById(Long aLong) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Asiento.class, aLong);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Asiento> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Asiento", Asiento.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Asiento entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Asiento entity) {
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
