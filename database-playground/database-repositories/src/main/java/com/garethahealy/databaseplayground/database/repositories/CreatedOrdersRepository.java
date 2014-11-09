package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;

import com.boohoo.esb.commerce.modules.orders.entities.OrderDetail;
import com.boohoo.esb.database.common.entities.CreatedOrders;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ICreatedOrdersRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class CreatedOrdersRepository extends BaseMySqlRepository implements ICreatedOrdersRepository {
	
	@Override
	public Integer create(String channelIdentifier, OrderDetail orderDetail, String json) throws FailedSqlConnectionException, ExecuteSqlException {
		Integer result = -1;
		try {
			DateTime orderedAt = orderDetail.getOrderedAt();
			long orderedAtMillis = orderedAt == null ? DateTime.now().getMillis() : orderedAt.getMillis();
						
			TypedQuery<Integer> query = createNamedTypedQuery(CreatedOrders.CREATE_CREATED_ORDERS, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, orderDetail.getOrderNumber());
			query.setParameter(RepositoryConstants.INDEX_TWO, orderDetail.getCustomer().getEmail());
			query.setParameter(RepositoryConstants.INDEX_THREE, orderDetail.getCurrencyISO().getCurrencyCode());
			query.setParameter(RepositoryConstants.INDEX_FOUR, orderDetail.getOrderTotal());
			query.setParameter(RepositoryConstants.INDEX_FIVE, new java.sql.Timestamp(orderedAtMillis));
			query.setParameter(RepositoryConstants.INDEX_SIX, json);
			query.setParameter(RepositoryConstants.INDEX_SEVEN, channelIdentifier);
			
			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to create create order for " + orderDetail.getOrderNumber() + " because: " + ex.getMessage(), ex);
		}
		
		if (result == null || result <= 0) {
			throw new IllegalArgumentException("Could not create order id for '" + orderDetail.getOrderNumber() + "'");
		}
		
		return result;
	}
}
