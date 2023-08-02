package ru.s21school.util;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import ru.s21school.entity.*;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
//        configuration.configure();


        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        configuration.addAnnotatedClass(Checks.class);
        configuration.addAnnotatedClass(Experience.class);
        configuration.addAnnotatedClass(Friends.class);
        configuration.addAnnotatedClass(Peers.class);
        configuration.addAnnotatedClass(PeerToPeer.class);
        configuration.addAnnotatedClass(Recommendations.class);
        configuration.addAnnotatedClass(Tasks.class);
        configuration.addAnnotatedClass(TimeTracking.class);
        configuration.addAnnotatedClass(TransferredPoints.class);
        configuration.addAnnotatedClass(Verter.class);

        return configuration;
    }


}