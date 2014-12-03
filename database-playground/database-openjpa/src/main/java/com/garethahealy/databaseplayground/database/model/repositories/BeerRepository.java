package com.garethahealy.databaseplayground.database.model.repositories;

import com.garethahealy.databaseplayground.database.model.entities.Beer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public interface BeerRepository {

        void setEntityManagerFactory(EntityManagerFactory entityManagerFactory);

        List<Beer> getBeers();
}
