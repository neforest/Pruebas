
package cenpro.registro.daoImpl;

import cenpro.registro.dao.AreaDao;
import cenpro.registro.dominio.Area;
import cenpro.registro.util.HibernateUtil;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Las Condori xD
 */
public class AreaDaoImpl implements AreaDao {

    List<Area> listaArea;
    Area area;
    
    @Override
    public boolean registrarArea(Area a) {
        Transaction tx = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(a);
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
    public List<Area> listarAreas(String facultad) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Area a where a.facultad.nombreFacultad = '"+facultad+"' order by nombreArea");
            listaArea = (List<Area>) query.list();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaArea;
    }

    @Override
    public List<Area> listarTodasAreas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Area a order by nombreArea");
            listaArea = (List<Area>) query.list();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaArea;
    }
    
    @Override
    public Area buscarArea(String nombrearea) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Area a where a.nombreArea = '"+nombrearea+"'");
            area = (Area) query.uniqueResult();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return area;
    }

    @Override
    public boolean modificcarArea(Area a) {

     Transaction tx = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(a);
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
    public boolean eliminarArea(Area a) {

     Transaction tx = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(a);
            session.beginTransaction().commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Existen trabajadores asignados a esta Area","Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
   
    }
}
