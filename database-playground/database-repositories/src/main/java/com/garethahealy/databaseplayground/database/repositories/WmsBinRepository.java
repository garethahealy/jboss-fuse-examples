package com.boohoo.esb.database.osgiservices.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.WmsBin;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IWmsBinRepository;

public class WmsBinRepository extends BaseMySqlRepository implements IWmsBinRepository {
	
	private ILogger logger;
	
	public WmsBinRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Map<String, WmsBin> getWmsBins() {
		List<WmsBin> result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(WmsBin.GET_WMS_BINS, Object[].class);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getWmsBins", "Failed to get wms bins because: {}", ex, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getWmsBins", "Failed to get wms bins because: {}", ex, ex.getMessage());
		}
		
		Map<String, WmsBin> bins = new HashMap<String, WmsBin>();
		if (result != null && result.size() > 0) {
			for (WmsBin wmsBin : result) {
				wmsBin.setIsAllocatable(Boolean.FALSE);
				bins.put(wmsBin.getLocationId(), wmsBin);
			}
		}
		
		return bins;
	}
	
	private List<WmsBin> convert(List<Object[]> result) {
		List<WmsBin> wmsBins = new ArrayList<WmsBin>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				WmsBin entity = new WmsBin();
				entity.setId(row[0] == null ? Integer.valueOf(0) : ((Integer)row[0]));
				entity.setLocationId(row[1] == null ? null : ((String)row[1]));
				entity.setSiteId(row[2] == null ? null : ((String)row[2]));
				entity.setStatus(row[3] == null ? Integer.valueOf(0) : ((Integer)row[3]));
				entity.setUpdatedDateTime(row[4] == null ? null : ((Timestamp)row[4]));
				
				wmsBins.add(entity);
			}
		}
		
		return wmsBins;
	}
}
