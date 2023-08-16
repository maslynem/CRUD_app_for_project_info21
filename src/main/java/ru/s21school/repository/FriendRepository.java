package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.s21school.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
