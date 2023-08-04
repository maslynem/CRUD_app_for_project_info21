package ru.s21school.mapper;

public interface Mapper<F, T> {
    T map(F object);
}
