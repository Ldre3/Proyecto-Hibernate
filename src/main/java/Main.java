import Modelo.Billete;
import Modelo.Cliente;
import Modelo.Vuelo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtils;

import java.util.ArrayList;
import java.util.Date;

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
            // Añadir los billetes al vuelo
            vuelo.setBilletes(new ArrayList<>());
            vuelo.getBilletes().add(billete1);
            vuelo.getBilletes().add(billete2);
            // Persistir el cliente y el vuelo
            session.save(cliente);
            session.save(vuelo);
            // Comprometer la transacción
            transaction.commit();
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
