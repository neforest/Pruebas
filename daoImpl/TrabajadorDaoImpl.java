package cenpro.registro.daoImpl;

import cenpro.registro.dao.TrabajadorDao;
import cenpro.registro.dominio.Trabajador;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cenpro.registro.util.HibernateUtil;
import java.util.ArrayList;
import org.hibernate.Hibernate;

/**
 *
 * @author CENPRO
 */
public class TrabajadorDaoImpl implements TrabajadorDao {

    List<Trabajador> listTrabajador;
    Trabajador trabajador;

    @Override
    public boolean registrarTrabajador(Trabajador t) {
        Transaction tx = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(t);
            session.beginTransaction().commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificarTrabajador(Trabajador t) {
        Transaction tx = null;
        try {
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(t);
            session.beginTransaction().commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Trabajador> listarTrabajadores() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Trabajador order by codigo");

            listTrabajador = (List<Trabajador>) query.list();
            //    System.out.println(" dimension trabajadores "+listTrabajador.size());
            for (Trabajador t : listTrabajador) {
                Hibernate.initialize(t.getArea());
            }
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listTrabajador;
    }

    @Override
    public Trabajador buscarTrabajador(String cod) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Trabajador t where t.codigo = '" + cod + "'");
            trabajador = (Trabajador) query.uniqueResult();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return trabajador;
    }

    @Override
    public Trabajador buscarTrabajadorxArea(String c, String area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Trabajador t where t.codigo = '" + c + "' and t.area.nombreArea = '"+area+"'");
            trabajador = (Trabajador) query.uniqueResult();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return trabajador;



    }

    @Override
    public List<Trabajador> listarTrabajdorxArea(String area) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Trabajador> trabajadores = new ArrayList();
        trabajadores =null;
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("Select * from Trabajador where id_area = "+area).addEntity(Trabajador.class);
            trabajadores = query.list();
            
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return trabajadores;

    }
}
