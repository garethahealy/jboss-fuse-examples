package com.garethahealy.databaseplayground.database.repositories.impl;

import com.garethahealy.databaseplayground.database.model.repositories.BeerRepository;

import javax.persistence.EntityManager;

public class BeerRepositoryImpl implements BeerRepository {

        private EntityManager entityManager;

        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }
}
