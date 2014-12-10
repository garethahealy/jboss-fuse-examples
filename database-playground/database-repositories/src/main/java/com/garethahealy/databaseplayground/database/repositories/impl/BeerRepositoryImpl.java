package com.garethahealy.databaseplayground.database.repositories.impl;

import com.garethahealy.databaseplayground.database.model.entities.Beer;
import com.garethahealy.databaseplayground.database.model.repositories.BeerRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class BeerRepositoryImpl implements BeerRepository {

    private EntityManager entityManager;

    //public void setEntityManager(EntityManager entityManager) {
    //        this.entityManager = entityManager;
    /// }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public List<Beer> getBeers() {
        TypedQuery<Beer> beersQuery = entityManager.createNamedQuery(Beer.SELECT_ALL_QUERY, Beer.class);
        return beersQuery.getResultList();
    }
}
