package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.NominatedDelivery;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.INominatedDeliveryRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class NominatedDeliveryRepository extends BaseMySqlRepository implements INominatedDeliveryRepository {
	
	private ILogger logger;
	
	public NominatedDeliveryRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer getNominatedDeliveryFor(String country, String shippingMethod) {
		Integer result = 0;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(NominatedDelivery.GET_NOMINATED_DELIVERY_FOR, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, country);
			query.setParameter(RepositoryConstants.INDEX_TWO, shippingMethod);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getNominatedDeliveryFor", "No data returned for {} / {} because: {}", ex, country, shippingMethod, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getNominatedDeliveryFor", "Failed to get nominated delivery for {} / {} because: {}", ex, country, shippingMethod, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getNominatedDeliveryFor", "Failed to get nominated delivery for {} / {} because: {}", ex, country, shippingMethod, ex.getMessage());
		}
		
		if (result == null || result <= 0) {
			result = 0;
		}
		
		return result;
	}
}
