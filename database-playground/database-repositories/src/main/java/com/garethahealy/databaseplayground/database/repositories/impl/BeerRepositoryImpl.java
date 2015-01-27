/*
 * #%L
 * database-repositories
 * %%
 * Copyright (C) 2013 - 2014 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.databaseplayground.database.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import com.garethahealy.databaseplayground.database.model.entities.Beer;
import com.garethahealy.databaseplayground.database.model.repositories.BeerRepository;

public class BeerRepositoryImpl implements BeerRepository {

    private EntityManager entityManager;

    @Override
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public List<Beer> getBeers() {
        if (entityManager == null) {
            throw new IllegalStateException("entityManager == null");
        }

        TypedQuery<Beer> beersQuery = entityManager.createNamedQuery(Beer.SELECT_ALL_QUERY, Beer.class);
        return beersQuery.getResultList();
    }
}
