package DAO;

import Modelo.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.HibernateUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOImplTest {

    private final Long ID_REMOVAL = 2L; // Id del cliente sobre el que se van a probar los metodos de eliminacion y actualizacion
    private SessionFactory sessionFactory;
    private Session session;
    private ClienteDAOImpl clienteDAOImpl;
    @BeforeEach
    void setUp() {
        sessionFactory = HibernateUtils.getSessionFactory();
        clienteDAOImpl = new ClienteDAOImpl(sessionFactory);
        session = sessionFactory.openSession();
    }

    @AfterEach
    void tearDown() {
        HibernateUtils.shutdown();
        session.close();
    }


    @Test
    void add() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setCorreoElectronico("adsdas");
        cliente.setBilletes(new ArrayList<>());
        clienteDAOImpl.add(cliente);
        assertEquals(cliente, session.find(Cliente.class, cliente.getId()));
    }

    @Test
    void getById() {
        assertEquals(session.find(Cliente.class, 1L), clienteDAOImpl.getById(1L));
    }

    @Test
    void getAll() {
        assertEquals(session.createQuery("FROM Cliente", Cliente.class).getResultList(), clienteDAOImpl.getAll());
    }

    @Test
    void update() {
        Cliente cliente = session.find(Cliente.class, ID_REMOVAL);
        cliente.setNombre("JuanTest");
        clienteDAOImpl.update(cliente);
        assertEquals(cliente, session.find(Cliente.class, ID_REMOVAL));
    }

    @Test
    void delete() {
        Cliente cliente = session.find(Cliente.class, ID_REMOVAL);
        session.close();
        clienteDAOImpl.delete(cliente);
        session = sessionFactory.openSession();
        assertNull(session.find(Cliente.class, ID_REMOVAL),"El usuario debe ser nulo");
    }
}