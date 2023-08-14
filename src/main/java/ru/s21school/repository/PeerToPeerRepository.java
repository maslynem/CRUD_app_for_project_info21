package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import ru.s21school.entity.PeerToPeer;

import java.time.LocalTime;

@Repository
public interface PeerToPeerRepository extends JpaRepository<PeerToPeer, Long> {
    @Procedure
    void ADD_P2P_CHECK(String checkingPeer, String checkedPeer, String taskTitle, String state, LocalTime checkTime);
}
