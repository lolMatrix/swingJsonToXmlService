package ru.test.rest.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.test.rest.entity.Client;
import ru.test.rest.factrories.ConnectionFactory;

/**
 * It response for saving clients into postgres database
 */
@Repository
public class ClientRepository {

    /**
     * configured session factory for connection and saving entities into database
     */
    private SessionFactory connection;

    public ClientRepository() {
        connection = ConnectionFactory.getSessionFactory();
    }

    /**
     * save client entity into database
     *
     * @param client entity
     */
    public void save(Client client) {
        Session connect = connection.openSession();
        connect.beginTransaction();
        for (var document : client.getDocument()) {
            connect.save(document);
        }
        connect.save(client);
        connect.getTransaction().commit();
        connect.close();
    }

}
