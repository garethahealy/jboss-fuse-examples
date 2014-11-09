package com.boohoo.esb.database.osgiservices.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

public abstract class BaseMySqlRepository {
	
	private static String SQL_SERVER_CONNECTION_FAULT = "A connection could not be obtained";
	private static String SQL_DUPLICATE_ENTRY_FAULT = "Duplicate entry";
	
	//http://docs.oracle.com/javaee/6/api/javax/persistence/Query.html
	//http://docs.oracle.com/javaee/6/api/javax/persistence/TypedQuery.html
		
	protected EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	protected Query createNativeQuery(String sql) throws FailedSqlConnectionException {
		try {
			return this.entityManager.createNativeQuery(sql);
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
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
	
	protected Query createNamedQuery(String namedQuery) throws IllegalArgumentException, FailedSqlConnectionException {
		try {
			return this.entityManager.createNamedQuery(namedQuery);
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
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
	
	protected <T> TypedQuery<T> createNamedTypedQuery(String namedQuery, Class<T> type) throws IllegalArgumentException, FailedSqlConnectionException {
		try {
			return this.entityManager.createNamedQuery(namedQuery, type);
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
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
	
	protected Integer createOrUpdate(Query query) throws IllegalStateException, TransactionRequiredException, QueryTimeoutException, PersistenceException, FailedSqlConnectionException {
		try {
			return query.executeUpdate();
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
			}
			else if (ex.getMessage().contains(SQL_DUPLICATE_ENTRY_FAULT)) {
				throw new IllegalStateException(ex.getMessage(), ex);
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
	
	protected <T> T getSingleResult(TypedQuery<T> query) throws NoResultException, NonUniqueResultException, IllegalStateException, QueryTimeoutException, 
		TransactionRequiredException, PessimisticLockException, LockTimeoutException, PersistenceException, FailedSqlConnectionException {
		
		try {
			return query.getSingleResult();
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
			}
			else if (ex.getMessage().contains(SQL_DUPLICATE_ENTRY_FAULT)) {
				throw new IllegalStateException(ex.getMessage(), ex);
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
		
	protected <T> List<T> getResultList(TypedQuery<T> query) throws IllegalStateException, QueryTimeoutException, TransactionRequiredException, PessimisticLockException,
		LockTimeoutException, PersistenceException, FailedSqlConnectionException {
		
		try {
			return query.getResultList();
		} catch (PersistenceException ex) {
			if (ex.getCause() instanceof MySQLNonTransientConnectionException) {
				throw new FailedSqlConnectionException(ex.getMessage(), ex);
			}
			else if (ex.getMessage().contains(SQL_DUPLICATE_ENTRY_FAULT)) {
				throw new IllegalStateException(ex.getMessage(), ex);
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
