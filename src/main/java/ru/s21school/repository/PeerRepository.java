package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.s21school.entity.Peer;


@Repository
public interface PeerRepository extends JpaRepository<Peer, String> {
}
