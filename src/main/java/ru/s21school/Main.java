package ru.s21school;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.s21school.entity.Tasks;
import ru.s21school.util.HibernateUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()){
            Session session = sessionFactory.openSession();

            session.beginTransaction();

//            Tasks tasks = session.createQuery("SELECT p FROM Tasks p WHERE p.title = 'SQL3_s21_RetailAnalitycs v1.0'", Tasks.class).getSingleResult();
//            Tasks tasks1 = tasks.getParentTask();
//            Tasks tasks2 = tasks1.getParentTask();
//            Tasks tasks3 = tasks2.getParentTask();
//            Tasks tasks4 = tasks3.getParentTask();
//            Tasks tasks5 = tasks4.getParentTask();
            session.getTransaction().commit();
        }

    }
}