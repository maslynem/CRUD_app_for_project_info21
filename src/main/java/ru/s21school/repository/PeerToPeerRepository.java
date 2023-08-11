package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.s21school.entity.PeerToPeer;

@Repository
public interface PeerToPeerRepository extends JpaRepository<PeerToPeer, Long> {
}
