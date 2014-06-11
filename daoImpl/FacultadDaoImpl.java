
package cenpro.registro.daoImpl;

import cenpro.registro.dao.FacultadDao;
import cenpro.registro.dominio.Facultad;
import cenpro.registro.util.HibernateUtil;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author CP-LAB-4
 */
public class FacultadDaoImpl implements FacultadDao {

    List<Facultad> listafacultad;
    Facultad facultad;
    @Override
    public List<Facultad> listarFacultades() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Facultad order by nombreFacultad");
            listafacultad = (List<Facultad>) query.list();
            for (Facultad facultad : listafacultad) {
                Hibernate.initialize(facultad);
            }
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listafacultad;
    }
    
    public Facultad buscarFacultad(String nombrefacultad) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Facultad f where f.nombreFacultad = '"+nombrefacultad+"'");
            facultad = (Facultad) query.uniqueResult();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return facultad;
    }
}
