package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.FrontendConnectionsAdditional;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IFrontendConnectionsAdditionalRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class FrontendConnectionsAdditionalRepository extends BaseMySqlRepository implements IFrontendConnectionsAdditionalRepository {
	
	private ILogger logger;
	
	public FrontendConnectionsAdditionalRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public String getFrontendConnectionAdditionalFor(Integer frontendConnectionId) {
		String result = "";
		try {
			TypedQuery<String> query = createNamedTypedQuery(FrontendConnectionsAdditional.GET_FRONTEND_CONNECTIONADDITIONAL_FOR, String.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, frontendConnectionId);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getFrontendConnectionAdditionalFor", "Failed to get frontend connection additional for {} because: {}", ex, frontendConnectionId,
				    ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFrontendConnectionAdditionalFor", "Failed to get frontend connection additional for {} because: {}", ex, frontendConnectionId,
			        ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFrontendConnectionAdditionalFor", "Failed to get frontend connection additional for {} because: {}", ex, frontendConnectionId,
			        ex.getMessage());
		}
		
		return result;
	}
}
