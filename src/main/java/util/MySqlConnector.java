package util;

import model.Bumblebee;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class MySqlConnector {

    public static final SessionFactory sessionFactory = new MySqlConnector().getConnection();

    private MySqlConnector() {

    }

    private SessionFactory getConnection() {

        Configuration cfg = new Configuration()
                .addResource("hibernate.cfg.xml").configure();
        cfg.addAnnotatedClass(Bumblebee.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                applySettings(cfg.getProperties()).build();
        SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        System.out.println("База данных подсоединена (Hibernate)");
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory () {
        return sessionFactory;
    }
}
