package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.ConsumerCount;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IConsumersCountRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class ConsumersCountRepository extends BaseMySqlRepository implements IConsumersCountRepository {
	
	private ILogger logger;
	
	public ConsumersCountRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer getConsumerEndpointsCount(String routeName) {
		Integer result = 0;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(ConsumerCount.GET_CONSUMER_ENDPOINTS_COUNT, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, routeName);

			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getConsumerEndpointsCount", "No data returned for {} because: {}", ex, routeName, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getConsumerEndpointsCount", "Failed to get consumer for {} because: {}", ex, routeName, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getConsumerEndpointsCount", "Failed to get consumer for {} because: {}", ex, routeName, ex.getMessage());
		}
		
		if (result == null || result < 0) {
			result = 0;
		}
		
		return result;
	}
	
}
