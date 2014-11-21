package com.garethahealy.databaseplayground.database.model.repositories;

import com.garethahealy.databaseplayground.database.model.entities.Beer;

import javax.persistence.EntityManager;
import java.util.List;

public interface BeerRepository {

        void setEntityManager(EntityManager entityManager);

        List<Beer> getBeers();
}
