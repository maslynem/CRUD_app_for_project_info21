package ru.s21school.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.peerDto.Peer;

import java.util.List;


@Component
public class PeerDao {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Peer> beanPropertyRowMapper;

    public PeerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        beanPropertyRowMapper = new BeanPropertyRowMapper<>(Peer.class);
    }

    public List<Peer> getAllPeer() {
        String SQL = "SELECT * FROM peer";
        return jdbcTemplate.query(SQL, beanPropertyRowMapper);
    }

    public Peer getById(int id) {
        String SQL = "SELECT * FROM peer WHERE id = ?";
        return jdbcTemplate.query(SQL, beanPropertyRowMapper, id).stream().findAny().orElse(null);
    }

    public void save(Peer peer) {
        String SQL = "INSERT INTO peer(name, age, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(SQL, peer.getName(), peer.getAge(), peer.getEmail());
    }

    public void update(int id, Peer peer) {
        String SQL = "UPDATE peer set name = ?, age = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(SQL, peer.getName(), peer.getAge(), peer.getEmail(), id);
    }

    public void delete(int id) {
        String SQL = "DELETE FROM peer WHERE id = ?";
        jdbcTemplate.update(SQL, id);
    }
}
