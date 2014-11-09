package com.boohoo.esb.database.osgiservices.repositories;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.commerce.modules.orders.entities.OrderItem;
import com.boohoo.esb.database.common.entities.CreatedOrderLines;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ICreatedOrderLinesRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class CreatedOrderLinesRepository extends BaseMySqlRepository implements ICreatedOrderLinesRepository {
	
	@Override
	public void create(Integer createdOrderId, List<OrderItem> items) throws FailedSqlConnectionException, ExecuteSqlException {
		for (OrderItem item : items) {
			try {
				TypedQuery<Integer> query = createNamedTypedQuery(CreatedOrderLines.CREATE_CREATED_ORDER_LINES, Integer.class);
				query.setParameter(RepositoryConstants.INDEX_ONE, createdOrderId);
				query.setParameter(RepositoryConstants.INDEX_TWO, item.getSku());
				query.setParameter(RepositoryConstants.INDEX_THREE, item.getQuantity());
				
				getSingleResult(query);
			} catch (PersistenceException ex) {
				throw new ExecuteSqlException("Failed to create created order lines for " + createdOrderId + " because: " + ex.getMessage(), ex);
			}
		}
	}
}
