package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.EntityManager;

public interface IBaseRepository {
	
	void setEntityManager(EntityManager entityManager);
}