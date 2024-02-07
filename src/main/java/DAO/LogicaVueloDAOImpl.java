package DAO;

import Modelo.Asiento;
import Modelo.Vuelo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.IntStream;

public class LogicaVueloDAOImpl implements LogicaVueloDAO{
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
}
