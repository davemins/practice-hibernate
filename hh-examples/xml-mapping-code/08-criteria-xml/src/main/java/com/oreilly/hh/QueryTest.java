package com.oreilly.hh;

import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.*;
import org.hibernate.*;

// import jakarta.persistence.TypedQuery;
// import jakarta.persistence.criteria.CriteriaBuilder;
// import jakarta.persistence.criteria.CriteriaQuery;
// import jakarta.persistence.criteria.Predicate;
// import jakarta.persistence.criteria.Root;
// import jakarta.persistence.criteria.*;

import com.oreilly.hh.data.*;

import java.sql.Time;
import java.util.*;

/**
 * Retrieve data as objects
 */
public class QueryTest {

    /**
     * Retrieve any tracks that fit in the specified amount of time.
     *
     * @param length the maximum playing time for tracks to be returned.
     * @param session the Hibernate session that can retrieve data.
     * @return a list of {@link Track}s meeting the length restriction.
     */
    public static List<Track> tracksNoLongerThan(Time length, Session session) {
        Criteria criteria = session.createCriteria(Track.class);
        Disjunction any = Restrictions.disjunction();
        any.add(Restrictions.le("playTime", length));
        any.add(Restrictions.like("title", "%A%"));
        criteria.add(any);
        criteria.addOrder(Order.asc("title").ignoreCase());
        return criteria.list();

        // // -----------------------------------------------------
        // // SELECT track FROM Track track WHERE track.playTime <= :playTime
        // // -----------------------------------------------------
        //
        // CriteriaBuilder cb = session.getCriteriaBuilder();
        //
        // CriteriaQuery<Track> cq = cb.createQuery(Track.class);
        // Root<Track> track = cq.from(Track.class);
        // cq.select(track);
        // ParameterExpression<Time> playTime = cb.parameter(Time.class);
        // cq.where(cb.lessThanOrEqualTo(track.get("playTime"), playTime));
        // // cq.where(cb.le(track.get("playTime"), playTime));       // Error
        // cq.orderBy(cb.asc(track.get("title")));
        //
        // TypedQuery<Track> query = session.createQuery(cq);
        // query.setParameter(playTime, length);
        //
        // List<Track> results = query.getResultList();
        //
        // return results;
    }

    /**
     * Retrieve any tracks associated with artists whose name matches a SQL
     * string pattern.
     *
     * @param namePattern the pattern which an artist's name must match
     * @param session the Hibernate session that can retrieve data.
     * @return a list of {@link Track}s meeting the artist name restriction.
     */
    public static List<Track> tracksWithArtistLike(String namePattern, Session session) {
        Criteria criteria = session.createCriteria(Track.class);
        Criteria artistCriteria = criteria.createCriteria("artists");
        artistCriteria.add(Restrictions.like("name", namePattern));
        artistCriteria.addOrder(Order.asc("name").ignoreCase());
        return criteria.list();

        // // -----------------------------------------------------
        // // SELECT track FROM Track track WHERE track.name LIKE :namePattern
        // // -----------------------------------------------------
        //
        // CriteriaBuilder cb = session.getCriteriaBuilder();
        //
        // CriteriaQuery<Track> cq = cb.createQuery(Track.class);
        //
        // CriteriaQuery<Track> artist_cq = cb.createQuery("artists");     // ?
        // Root<Artist> artist = artist_cq.from(Artist.class);
        // artist_cq.select(artist);
        // // ParameterExpression<Time> playTime = cb.parameter(Time.class);
        // // cq.where(cb.le(track.get("playTime"), playTime));
        //
        // TypedQuery<Track> query = session.createQuery(cq);
        // // query.setParameter(playTime, length);
        //
        // return query.getResultList();
    }

    /**
     * Retrieve the titles of any tracks that contain a particular text string.
     *
     * @param text the text to be matched, ignoring case, anywhere in the title.
     * @param session the Hibernate session that can retrieve data.
     * @return the matching titles, as strings.
     */
    public static List<Track> titlesContainingText(String text, Session session) {
        Criteria criteria = session.createCriteria(Track.class);
        criteria.add(Restrictions.like("title", text, MatchMode.ANYWHERE)
                     .ignoreCase());
        criteria.setProjection(Projections.property("title"));
        return criteria.list();
    }

    /**
     * Retrieve the titles and play times of any tracks that contain a
     * particular text string.
     *
     * @param text the text to be matched, ignoring case, anywhere in the title.
     * @param session the Hibernate session that can retrieve data.
     * @return the matching titles and times wrapped in object arrays.
     */
    public static List<Track> titlesContainingTextWithPlayTimes(String text,
            Session session) {
        Criteria criteria = session.createCriteria(Track.class);
        criteria.add(Restrictions.like("title", text, MatchMode.ANYWHERE)
                     .ignoreCase());
        criteria.setProjection(Projections.projectionList().
                               add(Projections.property("title")).
                               add(Projections.property("playTime")));
        return criteria.list();
    }

    /**
     * Print statistics about various media types.
     *
     * @param session the Hibernate session that can retrieve data.
     */
    public static void printMediaStatistics(Session session) {
        Criteria criteria = session.createCriteria(Track.class);
        criteria.setProjection(Projections.projectionList().
                               add(Projections.groupProperty("sourceMedia").as("media")).
                               add(Projections.rowCount()).
                               add(Projections.max("playTime")));
        criteria.addOrder(Order.asc("media"));

        for (Object o : criteria.list()) {
            Object[] array = (Object[])o;
            System.out.println(array[0] + " track count: " + array[1] +
                               "; max play time: " + array[2]);
        }
    }

    /**
     * Retrieve any tracks that were obtained from a particular source media
     * type.
     *
     * @param media the media type of interest.
     * @param session the Hibernate session that can retrieve data.
     * @return a list of {@link Track}s meeting the media restriction.
     */
    public static List<Track> tracksFromMedia(SourceMedia media, Session session) {
        Track track = new Track();
        track.setSourceMedia(media);
        Example example = Example.create(track);

        Criteria criteria = session.createCriteria(Track.class);
        criteria.add(example);
        criteria.addOrder(Order.asc("title"));
        return criteria.list();
    }

    /**
     * Build a parenthetical, comma-separated list of artist names.
     *
     * @param artists the artists whose names are to be displayed.
     * @return the formatted list, or an empty string if the set was empty.
     */
    public static String listArtistNames(Set<Artist> artists) {
        StringBuilder result = new StringBuilder();
        for (Artist artist : artists) {
            result.append((result.length() == 0) ? "(" : ", ");
            result.append(artist.getName());
        }
        if (result.length() > 0) {
            result.append(") ");
        }
        return result.toString();
    }

    /**
     * Look up and print some tracks when invoked from the command line.
     */
    public static void main(String args[]) throws Exception {
        //Configuration config = new Configuration();
        //config.configure();

        // Get the session factory we can use for persistence
        //SessionFactory sessionFactory = config.buildSessionFactory();
        SessionFactory sessionFactory = HibernateUtil5.getSessionFactory();

        // Ask for a session using the JDBC information we've configured
        Session session = sessionFactory.openSession();
        try {
            // Print track titles that contain the letter "v"
            //System.out.println(titlesContainingText("v", session));

            // Print track titles and lengths for "v"-containing tracks.
            //for (Object o : titlesContainingTextWithPlayTimes("v", session)) {
            //    Object[] array = (Object[])o;
            //    System.out.println("Title: " + array[0] +
            //            " (Play Time: " + array[1] + ')');
            //}

            // Print information about tracks from different media
            //printMediaStatistics(session);

            // Print tracks associated with an artist whose name ends with "n"
            List<Track> tracks = tracksFromMedia(SourceMedia.CD, session);
            // for (ListIterator iter = tracks.listIterator(); iter.hasNext();) {
            //     Track aTrack = (Track)iter.next();
            for (Track aTrack : tracks) {
                String mediaInfo = "";
                if (aTrack.getSourceMedia() != null) {
                    mediaInfo = ", from " + aTrack.getSourceMedia().getDescription();
                }
                System.out.println("Track: \"" + aTrack.getTitle() + "\" " +
                                   listArtistNames(aTrack.getArtists()) +
                                   aTrack.getPlayTime() + mediaInfo);
                for (String comment : aTrack.getComments()) {
                    System.out.println("  Comment: " + comment);
                }
            }
        } finally {
            // No matter what, close the session
            session.close();
        }

        // Clean up after ourselves
        sessionFactory.close();
    }
}
