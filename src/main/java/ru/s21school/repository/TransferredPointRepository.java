package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.s21school.entity.TransferredPoint;

@Repository
public interface TransferredPointRepository extends JpaRepository<TransferredPoint, Long> {
}
