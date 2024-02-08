package DAO;

import Modelo.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ClienteDAOImpl implements GenericDao<Cliente, Long>{
    private SessionFactory sessionFactory;

    public ClienteDAOImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}


    @Override
    public void add(Cliente cliente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(cliente);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public Cliente getById(Long id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Cliente.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Cliente> getAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Cliente", Cliente.class).getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void update(Cliente cliente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(cliente);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Cliente cliente) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(cliente);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }

    }
}