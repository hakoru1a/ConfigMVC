/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.social.repositories.impl;

import com.social.pojo.User;
import com.social.repositories.UserRepository;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author DinhChuong
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public User getUserByUsername(String username) {
        Session session = getSession();
        Query query = session.createQuery("FROM User WHERE username = :username");
        query.setParameter("username", username);
        try {
            Object result = query.getSingleResult();
            if (result instanceof User) {
                return (User) result;
            }
        } catch (NoResultException ex) {
            return null;
        }

        return null;
    }

    private Session getSession() {
        SessionFactory sessionFactory = this.sessionFactory.getObject();
        Session session = sessionFactory.getCurrentSession();
        return session;
    }

    @Override
    public List<User> getUserByEmail(String email) {
        Session session = getSession();
        Query query = session.createQuery("FROM User WHERE email = :email");
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users;
    }

    @Override
    public User save(User user) {
        Session session = getSession();
        session.saveOrUpdate(user);
        return user;
    }

    @Override
    public User getUserByUsernameAndAuthProvider(String username, Integer id) {
        Session session = getSession();
        Query query = session.createQuery("FROM User WHERE username = :username and provider_id = :id");
        query.setParameter("username", username);
        query.setParameter("id", id);
        try {
            Object result = query.getSingleResult();
            if (result instanceof User) {
                return (User) result;
            }
        } catch (NoResultException ex) {
            return null;
        }

        return null;
    }

}
