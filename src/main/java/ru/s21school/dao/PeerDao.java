package ru.s21school.dao;

import ru.s21school.models.Peer;

import java.util.ArrayList;
import java.util.List;

public class PeerDao {
    private static int PEOPLE_COUNT;
    private List<Peer> peers;

    {
        peers = new ArrayList<>();
        peers.add(new Peer(++PEOPLE_COUNT, "Tom"));
        peers.add(new Peer(++PEOPLE_COUNT, "Bob"));
        peers.add(new Peer(++PEOPLE_COUNT, "Mike"));
        peers.add(new Peer(++PEOPLE_COUNT, "Nike"));
        peers.add(new Peer(++PEOPLE_COUNT, "Katy"));
    }

    public List<Peer> getAllPeer() {
        return peers;
    }

    public Peer getById(int id) {
        return peers.stream().filter(peer -> peer.getId() == id).findAny().orElse(null);
    }
}
