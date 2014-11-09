package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.helpers.StringHelper;
import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.TimerPollsAdditional;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ITimerPollsAdditionalRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class TimerPollsAdditionalRepository extends BaseMySqlRepository implements ITimerPollsAdditionalRepository {
	
	private ILogger logger;
	
	public TimerPollsAdditionalRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer createAdditionalFor(Integer timerPollsId, String name, String additionalValue) {
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(TimerPollsAdditional.CREATE_ADDITIONAL_FOR, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, timerPollsId);
			query.setParameter(RepositoryConstants.INDEX_TWO, name);
			query.setParameter(RepositoryConstants.INDEX_THREE, additionalValue);
			
			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "createAdditionalFor", "Failed to create additional for {} using {} / {} because: {}", ex, timerPollsId, name, additionalValue,
			        ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "createAdditionalFor", "Failed to create additional for {} using {} / {} because: {}", ex, timerPollsId, name, additionalValue,
			        ex.getMessage());
		}
		
		
		return result;
	}
	
	@Override
	public String getAdditionalFor(Integer timerPollsId) {
		String result = null;
		try {
			TypedQuery<String> query = createNamedTypedQuery(TimerPollsAdditional.GET_ADDITIONAL_FOR, String.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, timerPollsId);

			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getAdditionalFor", "No data returned for {} because: {}", ex, timerPollsId, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getAdditionalFor", "Failed to get additional for {} because: {}", ex, timerPollsId, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getAdditionalFor", "Failed to get additional for {} because: {}", ex, timerPollsId, ex.getMessage());
		}
		
		return StringHelper.trim(result);
	}
	
	@Override
	public Integer updateAdditionalFor(Integer timerPollsId, String name, String additionalValue) {
		Integer result = -1;
		try {
			Query query = createNamedQuery(TimerPollsAdditional.UPDATE_ADDITIONAL_FOR);
			query.setParameter(RepositoryConstants.INDEX_ONE, additionalValue);
			query.setParameter(RepositoryConstants.INDEX_TWO, timerPollsId);
			query.setParameter(RepositoryConstants.INDEX_THREE, name);
			
			result = createOrUpdate(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "updateAdditionalFor", "Failed to update additional for {} using {} / {} because: {}", ex, timerPollsId, name, additionalValue,
			        ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "updateAdditionalFor", "Failed to update additional for {} using {} / {} because: {}", ex, timerPollsId, name, additionalValue,
			        ex.getMessage());
		}
		
		return result;
	}
}
