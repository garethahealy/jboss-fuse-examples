package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.helpers.BooleanHelper;
import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.Feeds;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IFeedsRepository;

public class FeedsRepository extends BaseMySqlRepository implements IFeedsRepository {
	
	private ILogger logger;
	
	public FeedsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<Feeds> getFeeds() {
		List<Feeds> result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(Feeds.GET_FEEDS, Object[].class);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFeeds", "Failed to get feeds because: {}", ex, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFeeds", "Failed to get feeds because: {}", ex, ex.getMessage());
		}
		
		return result;
	}
	
	private List<Feeds> convert(List<Object[]> result) {
		List<Feeds> feeds = new ArrayList<Feeds>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				Feeds entity = new Feeds();
				entity.setId(row[0] == null ? Integer.valueOf(0) : ((Integer)row[0]));
				entity.setName(row[1] == null ? null : ((String)row[1]));
				entity.setConverterName(row[2] == null ? null : ((String)row[2]));
				entity.setMarshallerName(row[3] == null ? null : ((String)row[3]));
				entity.setLocaleLanguage(row[4] == null ? null : ((String)row[4]));
				entity.setLocaleCountry(row[5] == null ? null : ((String)row[5]));
				entity.setCron(row[6] == null ? null : ((String)row[6]));
				entity.setIsIncremental(row[7] == null ? Boolean.FALSE : BooleanHelper.parse(row[7].toString()));
				entity.setChannelIdentifier(row[8] == null ? null : ((String)row[8]));
				entity.setOutputFileName(row[9] == null ? null : ((String)row[9]));
				
				feeds.add(entity);
			}
		}
		
		return feeds;
	}
}
