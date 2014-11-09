package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.AllocatedOrders;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IAllocatedOrdersRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class AllocatedOrdersRepository extends BaseMySqlRepository implements IAllocatedOrdersRepository {

	private ILogger logger;
	
	public AllocatedOrdersRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer createAllocatedOrder(String orderNumber, Integer processStatus, Integer orderStatus) throws FailedSqlConnectionException, ExecuteSqlException {
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(AllocatedOrders.CREATE_ALLOCATED_ORDER, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, orderNumber);
			query.setParameter(RepositoryConstants.INDEX_TWO, processStatus);
			query.setParameter(RepositoryConstants.INDEX_THREE, orderStatus);

			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to create allocated order for " + orderNumber + " / " + processStatus + " /" + orderStatus + " because: " + ex.getMessage(), ex);
		}
		
		if (result == null || result < 0) {
			result = -1;
		}
		
		return result;
	}
	
	@Override
	public Integer getAllocatedOrderId(String orderNumber) throws FailedSqlConnectionException, ExecuteSqlException {
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(AllocatedOrders.GET_ALLOCATED_ORDER_ID, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, orderNumber);

			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getAllocatedOrderId", "No data returned for {} because: {}", ex, orderNumber, ex.getMessage());
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get allocated order id for " + orderNumber + " because: " + ex.getMessage(), ex);
		}
		
		if (result == null || result < 0) {
			result = -1;
		}
		
		return result;
	}
	
	@Override
	public void updateAllocatedOrderStatus(Integer orderStatus, Integer id) throws FailedSqlConnectionException, ExecuteSqlException {
		try {
			Query query = createNamedQuery(AllocatedOrders.UPDATE_ALLOCATED_ORDER_STATUS);
			query.setParameter(RepositoryConstants.INDEX_ONE, orderStatus);
			query.setParameter(RepositoryConstants.INDEX_TWO, id);

			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to update allocated order status for " + id + " / " + orderStatus + " because: " + ex.getMessage(), ex);
		}
	}
}
