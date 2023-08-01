package ru.s21school.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.s21school.peerDto.Peer;

import java.util.List;
import java.util.Optional;


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

    public Optional<Peer> getById(int id) {
        String SQL = "SELECT * FROM peer WHERE id = ?";
        return jdbcTemplate.query(SQL, beanPropertyRowMapper, id).stream().findAny();
    }

    public Optional<Peer> getByEmail(String email) {
        String SQL = "SELECT * FROM peer WHERE email = ?";
        return jdbcTemplate.query(SQL, beanPropertyRowMapper, email).stream().findAny();
    }

    public void save(Peer peer) {
        String SQL = "INSERT INTO peer(name, age, email, address) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(SQL, peer.getName(), peer.getAge(), peer.getEmail(), peer.getAddress());
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
