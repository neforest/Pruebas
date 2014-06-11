
package cenpro.registro.daoImpl;

import cenpro.registro.dao.LoginDao;
import cenpro.registro.dominio.Trabajador;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import cenpro.registro.util.HibernateUtil;
import org.hibernate.Hibernate;

/**
 *
 * @author Miriam xD
 */
public class LoginDaoImpl implements LoginDao {

    Trabajador trabajador;

    @Override
    public Trabajador logear(String usuario, String contraseña, String facultad) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Trabajador t where t.codigo = '" + usuario + "' and t.password = '" + contraseña + "' "
                    + "and t.area.facultad.nombreFacultad = '"+facultad+"' " + "and t.estado = 'ACTIVO'");
            trabajador = (Trabajador) query.uniqueResult();
            if(trabajador!=null){
                Hibernate.initialize(trabajador.getArea().getFacultad());    
            }                   
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return trabajador;
    }
}
