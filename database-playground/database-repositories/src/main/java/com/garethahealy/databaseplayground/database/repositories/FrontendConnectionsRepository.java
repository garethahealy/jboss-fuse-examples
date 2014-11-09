package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.helpers.BooleanHelper;
import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.FrontendConnections;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IFrontendConnectionsRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class FrontendConnectionsRepository extends BaseMySqlRepository implements IFrontendConnectionsRepository {
	
	private ILogger logger;
	
	public FrontendConnectionsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public FrontendConnections getUserAuthFrontends(String name, String frontendType) {
		FrontendConnections result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(FrontendConnections.GET_USER_AUTH_FRONTENDS, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, name);
			query.setParameter(RepositoryConstants.INDEX_TWO, frontendType);
			
			result = convert(getSingleResult(query));
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getUserAuthFrontends", "No data returned for {} / {} because: {}", ex, name, frontendType, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getUserAuthFrontends", "Failed to get frontend connection for {} / {} because: {}", ex, name, frontendType, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getUserAuthFrontends", "Failed to get frontend connection for {} / {} because: {}", ex, name, frontendType, ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public List<FrontendConnections> getFrontendsConnections(String sql) {
		List<FrontendConnections> result = new ArrayList<FrontendConnections>();
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(sql, Object[].class);

			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFrontendsConnections", "Failed to get frontend connection from {} because: {}", ex, sql, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFrontendsConnections", "Failed to get frontend connection from {} because: {}", ex, sql, ex.getMessage());
		}
		
		return result;
	}
	
	private List<FrontendConnections> convert(List<Object[]> result) {
		List<FrontendConnections> frontendConnections = new ArrayList<FrontendConnections>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				FrontendConnections entity = convert(row);

				frontendConnections.add(entity);
			}
		}
		
		return frontendConnections;
	}
	
	private FrontendConnections convert(Object[] result) {
		FrontendConnections entity = new FrontendConnections();
		if (result != null) {
			entity.setId(result[0] == null ? Integer.valueOf(0) : ((Integer)result[0]));
			entity.setName(result[1] == null ? null : ((String)result[1]));
			entity.setType(result[2] == null ? null : ((String)result[2]));
			entity.setRpcUrl(result[3] == null ? null : ((String)result[3]));
			entity.setDocLitUrl(result[4] == null ? null : ((String)result[4]));
			entity.setUsername(result[5] == null ? null : ((String)result[5]));
			entity.setPassword(result[6] == null ? null : ((String)result[6]));
			entity.setIsProductUpdates(result[7] == null ? Boolean.FALSE : BooleanHelper.parse(result[7].toString()));
			entity.setIsStockUpdates(result[8] == null ? Boolean.FALSE : BooleanHelper.parse(result[8].toString()));
			entity.setIsOrderRetrieval(result[9] == null ? Boolean.FALSE : BooleanHelper.parse(result[9].toString()));
			entity.setIsOrderDespatchedUpdates(result[10] == null ? Boolean.FALSE : BooleanHelper.parse(result[10].toString()));
			entity.setIsOrderReturnedUpdates(result[11] == null ? Boolean.FALSE : BooleanHelper.parse(result[11].toString()));
			entity.setIsOrderCancelledUpdates(result[12] == null ? Boolean.FALSE : BooleanHelper.parse(result[12].toString()));
			entity.setIsShippingManifestUpdates(result[13] == null ? Boolean.FALSE : BooleanHelper.parse(result[13].toString()));
		}
		
		return entity;
	}
}
