package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.TimerPolls;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ITimerPollsRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class TimerPollsRepository extends BaseMySqlRepository implements ITimerPollsRepository {
	
	private ILogger logger;
	
	public TimerPollsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer createTimerPollsFor(String system, String name, String systemType, String lastDateTime) {
		String systemName = system + "_" + name;
		
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(TimerPolls.CREATE_TIMER_POLL_FOR, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, systemType);
			query.setParameter(RepositoryConstants.INDEX_TWO, systemName);
			query.setParameter(RepositoryConstants.INDEX_THREE, lastDateTime);
			
			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "createTimerPollsFor", "Failed to create poll for {} / {} @ {} because: {}", ex, systemName, systemType, lastDateTime, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "createTimerPollsFor", "Failed to create poll for {} / {} @ {} because: {}", ex, systemName, systemType, lastDateTime, ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public TimerPolls getTimerPollsFor(String system, String name, String systemType) {
		String systemName = system + "_" + name;
		
		TimerPolls result = new TimerPolls();
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(TimerPolls.GET_TIMER_POLL_FOR, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, systemName);
			query.setParameter(RepositoryConstants.INDEX_TWO, systemType);
			
			result = convert(getSingleResult(query));
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getTimerPollsFor", "No data returned for {} / {} because: {}", ex, systemName, systemType, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getTimerPollsFor", "Failed to get poll for {} / {} because: {}", ex, systemName, systemType, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getTimerPollsFor", "Failed to get poll for {} / {} because: {}", ex, systemName, systemType, ex.getMessage());
		}

		return result;
	}
	
	@Override
	public Integer updateTimerPolls(String system, String name, String systemType, String lastDateTime) {
		String systemName = system + "_" + name;
		
		Integer result = -1;
		try {
			Query query = createNamedQuery(TimerPolls.UPDATE_TIMER_POLL_FOR);
			query.setParameter(RepositoryConstants.INDEX_ONE, lastDateTime);
			query.setParameter(RepositoryConstants.INDEX_TWO, systemName);
			query.setParameter(RepositoryConstants.INDEX_THREE, systemType);
			
			result = createOrUpdate(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "updateTimerPolls", "Failed to update poll for {} / {} to {} because: {}", ex, systemName, systemType, lastDateTime, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "updateTimerPolls", "Failed to update poll for {} / {} to {} because: {}", ex, systemName, systemType, lastDateTime, ex.getMessage());
		}
		
		return result;
	}
	
	private TimerPolls convert(Object[] result) {
		TimerPolls entity = new TimerPolls();
		if (result != null) {
			entity.setId(result[0] == null ? Integer.valueOf(0) : ((Integer)result[0]));
			entity.setType(result[1] == null ? null : ((String)result[1]));
			entity.setName(result[2] == null ? null : ((String)result[2]));
			entity.setLastDateTime(result[3] == null ? null : ((String)result[3]));
		}
		
		return entity;
	}
}
