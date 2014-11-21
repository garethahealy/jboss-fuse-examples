package com.garethahealy.databaseplayground.database.repositories.impl;

import com.garethahealy.databaseplayground.database.model.entities.Beer;
import com.garethahealy.databaseplayground.database.model.repositories.BeerRepository;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Queue;

public class BeerRepositoryImpl implements BeerRepository {

        private EntityManager entityManager;

        public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
        }


        @Override
        public List<Beer> getBeers() {
                TypedQuery<Beer> beersQuery = entityManager.createQuery("SELECT b.id, b.name FROM Beer b", Beer.class);
                return beersQuery.getResultList();
        }
}
