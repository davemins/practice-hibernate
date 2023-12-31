package com.oreilly.hh;

import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.*;

import com.oreilly.hh.data.*;

import java.sql.Time;
import java.util.Date;

/**
 * Create sample data, letting Hibernate persist it for us.
 */
public class CreateTest {

    public static void main(String args[]) throws Exception {
        //Configuration config = new Configuration();
        //config.configure();

        // Get the session factory we can use for persistence
        //SessionFactory sessionFactory = config.buildSessionFactory();
        SessionFactory sessionFactory = HibernateUtil5.getSessionFactory();

        // Ask for a session using the JDBC information we've configured
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            // Create some data and persist it
            tx = session.beginTransaction();

            Track track = new Track("Russian Trance",
                                    "vol2/album610/track02.mp3",
                                    Time.valueOf("00:03:30"), new Date(),
                                    (short)0);
            session.persist(track);

            track = new Track("Video Killed the Radio Star",
                              "vol2/album611/track12.mp3",
                              Time.valueOf("00:03:49"), new Date(),
                              (short)0);
            session.persist(track);

            track = new Track("Gravity's Angel",
                              "vol2/album175/track03.mp3",
                              Time.valueOf("00:06:06"), new Date(),
                              (short)0);
            session.persist(track);

            // We're done; make our changes permanent
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                // Something went wrong; discard all partial changes
                tx.rollback();
            }
            throw new Exception("Transaction failed", e);
        } finally {
            // No matter what, close the session
            session.close();
        }

        // Clean up after ourselves
        sessionFactory.close();
    }
}
