package ru.s21school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.s21school.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
