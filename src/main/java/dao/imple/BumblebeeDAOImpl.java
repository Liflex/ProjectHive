package dao.imple;

import dao.interf.BumblebeeDAOInterf;
import model.Bumblebee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class BumblebeeDAOImpl implements BumblebeeDAOInterf {

    private SessionFactory sessionFactory;

    public BumblebeeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Bumblebee get(long id) {
        return null;
    }

    @Override
    public List<Bumblebee> getAll() {
        List<Bumblebee> users = (List<Bumblebee>) sessionFactory.openSession().createQuery("From User").list();
        return users;
    }

    @Override
    public void add(Bumblebee t) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(t);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Bumblebee t) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(t);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(new Bumblebee(id));
        transaction.commit();
        session.close();
    }

}
