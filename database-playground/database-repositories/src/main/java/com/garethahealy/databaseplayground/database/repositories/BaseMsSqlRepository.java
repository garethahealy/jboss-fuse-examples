package com.boohoo.esb.database.osgiservices.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;

import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;

public abstract class BaseMsSqlRepository implements IBaseRepository {
	
	private static String SQL_SERVER_CONNECTION_FAULT = "A connection could not be obtained";
			
	//http://docs.oracle.com/javaee/6/api/javax/persistence/Query.html
	//http://docs.oracle.com/javaee/6/api/javax/persistence/TypedQuery.html
		
	private EntityManager entityManager;
	
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected Query createNativeQuery(String sql) throws PersistenceException, FailedSqlConnectionException {
		try {
			return this.entityManager.createNativeQuery(sql);
		} catch (PersistenceException ex) {
			if (ex.getMessage().contains(SQL_SERVER_CONNECTION_FAULT)) {
				throw new FailedSqlConnectionException(FailedSqlConnectionException.EXCEPTION_MESSAGE, ex);
			} else {
				throw ex;
			}
		} catch (IllegalArgumentException ex) {
			if (ex.getMessage().contains(SQL_SERVER_CONNECTION_FAULT)) {
				throw new FailedSqlConnectionException(FailedSqlConnectionException.EXCEPTION_MESSAGE, ex);
			} else {
				throw ex;
			}
		}
	}
		
	@SuppressWarnings("unchecked")
	protected List<Object[]> getResultList(Query query) throws IllegalStateException, QueryTimeoutException, TransactionRequiredException, PessimisticLockException,
		LockTimeoutException, PersistenceException, FailedSqlConnectionException {
		try {
			return query.getResultList();
		} catch (PersistenceException ex) {
			if (ex.getMessage().contains(SQL_SERVER_CONNECTION_FAULT)) {
				throw new FailedSqlConnectionException(FailedSqlConnectionException.EXCEPTION_MESSAGE, ex);
			} else {
				throw ex;
			}
		} catch (IllegalArgumentException ex) {
			if (ex.getMessage().contains(SQL_SERVER_CONNECTION_FAULT)) {
				throw new FailedSqlConnectionException(FailedSqlConnectionException.EXCEPTION_MESSAGE, ex);
			} else {
				throw ex;
			}
		}
	}
}
