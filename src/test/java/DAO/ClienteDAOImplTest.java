package DAO;

import Modelo.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import util.HibernateUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteDAOImplTest {

    private static Long idTest; // Id del cliente sobre el que se van a probar los metodos de eliminacion y actualizacion, se asigna en el metodo add
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
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        HibernateUtils.shutdown();
    }


    @Order(1)
    @Test
    void add() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setCorreoElectronico("adsdas");
        cliente.setBilletes(new ArrayList<>());
        clienteDAOImpl.add(cliente);
        assertEquals(cliente, session.find(Cliente.class, cliente.getId()));
        idTest = cliente.getId();
    }

    @Order(2)
    @Test
    void getById() {
        assertEquals(session.find(Cliente.class, 1L), clienteDAOImpl.getById(1L));
    }

    @Order(3)
    @Test
    void getAll() {
        assertEquals(session.createQuery("FROM Cliente", Cliente.class).getResultList(), clienteDAOImpl.getAll());
    }

    @Order(4)
    @Test
    void update() {
        Cliente cliente = session.find(Cliente.class, idTest);
        cliente.setNombre("JuanTest");
        clienteDAOImpl.update(cliente);
        assertEquals(cliente, session.find(Cliente.class, idTest));
    }

    @Order(5)
    @Test
    void delete() {
        Cliente cliente = session.find(Cliente.class, idTest);
        session.close();
        clienteDAOImpl.delete(cliente);
        session = sessionFactory.openSession();
        assertNull(session.find(Cliente.class, idTest),"El usuario debe ser nulo");
    }
}