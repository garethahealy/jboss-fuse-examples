package com.boohoo.esb.database.osgiservices.repositories;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.FeedFtpCredentials;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IFeedFtpCredentialsRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class FeedFtpCredentialsRepository extends BaseMySqlRepository implements IFeedFtpCredentialsRepository {
	
	private ILogger logger;
	
	public FeedFtpCredentialsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public FeedFtpCredentials getFeedFtpCredentials(Integer feedId) {
		FeedFtpCredentials result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(FeedFtpCredentials.GET_FEED_FTP_CREDENTIALS, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, feedId);

			result = convert(getSingleResult(query));
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getFeedFtpCredentials", "No data returned for {} because: {}", ex, feedId, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFeedFtpCredentials", "Failed to get ftp credentials for {} because: {}", ex, feedId, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFeedFtpCredentials", "Failed to get ftp credentials for {} because: {}", ex, feedId, ex.getMessage());
		}
		
		return result;
	}
	
	private FeedFtpCredentials convert(Object[] result) {
		FeedFtpCredentials entity = new FeedFtpCredentials();
		if (result != null) {
			entity.setId(result[0] == null ? Integer.valueOf(0) : ((Integer)result[0]));
			entity.setFeedId(result[1] == null ? Integer.valueOf(0) : ((Integer)result[1]));
			entity.setHost(result[2] == null ? null : ((String)result[2]));
			entity.setUsername(result[3] == null ? null : ((String)result[3]));
			entity.setPassword(result[4] == null ? null : ((String)result[4]));
			entity.setPort(result[5] == null ? null : ((String)result[5]));
			entity.setRemoteFolder(result[6] == null ? null : ((String)result[6]));
		}
		
		return entity;
	}
}
