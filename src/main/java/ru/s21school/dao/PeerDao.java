package ru.s21school.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.entity.Peer;

import java.util.List;
import java.util.Optional;


@Component
public class PeerDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public PeerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Peer> getAllPeer() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select p from Peer p", Peer.class).getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Peer> getById(int id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Peer.class, id));
    }

    @Transactional(readOnly = true)
    public Optional<Peer> getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Peer> query = session.createQuery("select p from Peer p WHERE p.email = ?1", Peer.class);
        query.setParameter(1, email);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Transactional
    public void save(Peer peer) {
        sessionFactory.getCurrentSession().save(peer);
    }

    @Transactional
    public void update(int id, Peer peer) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(peer);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession().remove(id);
    }
}
