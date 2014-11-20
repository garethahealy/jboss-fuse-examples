package com.garethahealy.databaseplayground.database.model.repositories;

import javax.persistence.EntityManager;

public interface BeerRepository {

        void setEntityManager(EntityManager entityManager);
}
