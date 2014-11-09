package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.boohoo.esb.database.common.entities.AllocatedOrderLines;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IAllocatedOrderLinesRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class AllocatedOrderLinesRepository extends BaseMySqlRepository implements IAllocatedOrderLinesRepository {
	
	@Override
	public void createAllocatedOrderLines(Integer allocatedOrderId, String sku, Integer quantity, Integer statusToInsert) throws FailedSqlConnectionException, ExecuteSqlException {
		try {
			Query query = createNamedQuery(AllocatedOrderLines.CREATE_ALLOCATED_ORDER_LINES);
			query.setParameter(RepositoryConstants.INDEX_ONE, allocatedOrderId);
			query.setParameter(RepositoryConstants.INDEX_TWO, sku);
			query.setParameter(RepositoryConstants.INDEX_THREE, quantity);
			query.setParameter(RepositoryConstants.INDEX_FOUR, statusToInsert);
			
			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to create allocated order line for " + allocatedOrderId + " / " + sku + " / " + quantity + " / " + statusToInsert 
					+ " because: " + ex.getMessage(), ex);
		}
	}
	
	@Override
	public void updateAllocatedOrderLinesStatus(Integer orderStatus, Integer allocatedOrderId) throws FailedSqlConnectionException, ExecuteSqlException {
		try {
			Query query = createNamedQuery(AllocatedOrderLines.UPDATE_ALLOCATED_ORDER_LINES_STATUS);
			query.setParameter(RepositoryConstants.INDEX_ONE, orderStatus);
			query.setParameter(RepositoryConstants.INDEX_TWO, allocatedOrderId);
			
			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to update allocated rder line status for " + allocatedOrderId + " / " + orderStatus + " because: " + ex.getMessage(), ex);
		}
	}
}
