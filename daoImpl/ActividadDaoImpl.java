package cenpro.registro.daoImpl;

import cenpro.registro.dao.ActividadDao;
import cenpro.registro.dominio.Actividad;
import cenpro.registro.dominio.Trabajador;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cenpro.registro.util.HibernateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;

/**
 *
 * @author Miriam!!!!!!!!!!!! xD
 */
public class ActividadDaoImpl implements ActividadDao {

    List<Actividad> listaActividades;
    Actividad actividad;
    Actividad actividades;
    Trabajador trabajador;
    List<Actividad> listaActividadentreFechas;

    @Override
    public boolean registrarActividad(Actividad a) {
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
    public List<Actividad> listarActividades(String codigo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Actividad a where a.trabajador.codigo = '" + codigo + "'"
                    + " order by fecha");
            listaActividades = (List<Actividad>) query.list();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaActividades;
    }

    @Override
    public Trabajador MostrarTrabajador(Trabajador t) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Trabajador t where t.codigo = '" + t.getCodigo() + "'");

            trabajador = (Trabajador) query.uniqueResult();
            Hibernate.initialize(trabajador.getArea());
            Hibernate.initialize(trabajador.getArea().getFacultad());
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return trabajador;
    }

    @Override
    public Actividad buscarActividadxFecha(String codigo, String fecha) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechad = String_a_Date(fecha);
            String cambio = DateaString(fechad);
            session.beginTransaction();

            Query query = session.createQuery("from Actividad a where a.trabajador.codigo = '" + codigo + "' and "
                    + " a.fecha = '" + cambio + "'");
            if (query.uniqueResult() != null) {
                actividad = new Actividad();
                actividad = (Actividad) query.uniqueResult();
            } else {
                actividad = null;
            }

            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return actividad;
    }

    public Date String_a_Date(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechad = null;
        try {
            fechad = sdf.parse(fecha);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return fechad;
    }

    // al borde del colapso xD
    public String DateaString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Override
    public List<Actividad> buscarActividadentreFechas(String codigo, String fechaI, String fechaF) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechaIni = String_a_Date(fechaI);
            Date fechaFin = String_a_Date(fechaF);
            session.beginTransaction();
            Query query = session.createQuery("from Actividad a where a.trabajador.codigo = '" + codigo + "' and "
                    + " a.fecha >= '" + fechaIni + "'" + " and a.fecha <= '" + fechaFin + "'");
            listaActividadentreFechas = (List<Actividad>) query.list();
            session.beginTransaction().commit();
            session.close();

        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaActividadentreFechas;

    }

    @Override
    public List<Actividad> buscarActividadesxFecha(String fecha) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechad = String_a_Date(fecha);
            String cambio = DateaString(fechad);
            session.beginTransaction();
//            SELECT `trabajador`.codigo, `trabajador`.nombres, `actividad`.fecha 
//            FROM `actividad`, `trabajador` WHERE `trabajador`.codigo = `actividad`.codigo 
//            AND `actividad`.fecha = '2014-03-12'

            Query query = session.createSQLQuery("Select * from actividad where  fecha='" + cambio + "'").addEntity(Actividad.class);
            listaActividades = new ArrayList();
            listaActividades = query.list();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaActividades;
    }

    @Override
    public List<Actividad> buscarActividadesxFechaxArea(String fecha, String nombreArea) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechad = String_a_Date(fecha);
            String cambio = DateaString(fechad);
            session.beginTransaction();
//            SELECT `trabajador`.codigo, `trabajador`.nombres, `actividad`.fecha 
//            FROM `actividad`, `trabajador` WHERE `trabajador`.codigo = `actividad`.codigo 
//            AND `actividad`.fecha = '2014-03-12'

            Query query = session.createSQLQuery("Select a.id_actividad, "
                    + "a.codigo, a.descripcion, a.fecha, a.hora_inicio, "
                    + "a.hora_salida, a.total_horas, a.revision "
                    + "from actividad a inner join "
                    + "(trabajador t inner join area ar "
                    + "on t.id_area=ar.id_area)"
                    + "on a.codigo=t.codigo "
                    + "where  a.fecha='" + cambio + "' "
                    + "and ar.nombre_area ='"+nombreArea+"'").addEntity(Actividad.class);
            listaActividades = new ArrayList();
            listaActividades = query.list();
            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return listaActividades;
    }

    @Override
    public boolean actualizarActividad(Actividad a) {
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
    public Actividad buscarActividadxFechaHoras(String codigo, String fecha) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechad = String_a_Date(fecha);
            String cambio = DateaString(fechad);
            session.beginTransaction();

            Query query = session.createQuery("from Actividad a where a.trabajador.codigo = '" + codigo + "' and "
                    + " a.fecha = '" + cambio + "' and a.horaSalida <> '' and a.horaInicio <> ''");
            if (query.uniqueResult() != null) {
                actividad = new Actividad();
                actividad = (Actividad) query.uniqueResult();
            } else {
                actividad = null;
            }

            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return actividad;

    }

    @Override
    public Actividad buscarActividadxfechaSinHoraF(String codigo, String fecha) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Date fechad = String_a_Date(fecha);
            String cambio = DateaString(fechad);
            session.beginTransaction();

            Query query = session.createQuery("from Actividad a where a.trabajador.codigo = '" + codigo + "' and "
                    + " a.fecha = '" + cambio + "' and a.horaSalida <> '' and a.horaInicio = ''");
            if (query.uniqueResult() != null) {
                actividad = new Actividad();
                actividad = (Actividad) query.uniqueResult();
            } else {
                actividad = null;
            }

            session.beginTransaction().commit();
            session.close();
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        return actividad;
    }
}
