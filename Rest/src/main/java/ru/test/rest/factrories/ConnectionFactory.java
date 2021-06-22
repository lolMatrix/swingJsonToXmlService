package ru.test.rest.factrories;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.test.rest.entity.Client;
import ru.test.rest.entity.Document;

/**
 * It create new session factory instances
 */
public class ConnectionFactory {

    /**
     * Create hibernate session factory
     *
     * @return configured hibernate session factory
     */
    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Document.class);

        return configuration.buildSessionFactory();
    }
}
