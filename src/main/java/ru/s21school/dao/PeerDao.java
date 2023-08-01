package ru.s21school.dao;

import org.springframework.stereotype.Component;
import ru.s21school.peerDto.Peer;

import java.util.ArrayList;
import java.util.List;

@Component
public class PeerDao {
    private int PEOPLE_COUNT;
    private final List<Peer> peers;

    public PeerDao() {
        peers = new ArrayList<>();
        peers.add(new Peer(++PEOPLE_COUNT, "Tom","Smith", "tom@mail.ru"));
        peers.add(new Peer(++PEOPLE_COUNT, "Bob","Ivanov", "bob@mail.ru"));
        peers.add(new Peer(++PEOPLE_COUNT, "Mike","Smirnov", "mike@mail.ru"));
        peers.add(new Peer(++PEOPLE_COUNT, "Nike","Clark", "nike@mail.ru"));
        peers.add(new Peer(++PEOPLE_COUNT, "Katy","Brown", "katy@mail.ru"));
    }

    public List<Peer> getAllPeer() {
        return peers;
    }

    public Peer getById(int id) {
        return peers.stream().filter(peer -> peer.getId() == id).findAny().orElse(null);
    }

    public void save(Peer peer) {
        peer.setId(++PEOPLE_COUNT);
        peers.add(peer);
    }
}
