package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.InventoryLevel;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IInventoryLevelRepository;

public class InventoryLevelRepository extends BaseMsSqlRepository implements IInventoryLevelRepository  {
	
	private ILogger logger;
	
	public InventoryLevelRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<InventoryLevel> getChangedInventoryLevels(DateTime lastDate) {
		List<InventoryLevel> result = new ArrayList<InventoryLevel>();
		try {
			Query query = createNativeQuery(InventoryLevel.GET_CHANGED_INVENTORY_LEVELS);
			query.setParameter(1, lastDate.toDate(), TemporalType.TIMESTAMP);
			
			result = convert(getResultList(query));
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getChangedInventoryLevels", "Failed to get changed inventory levels for {} because: {}", ex, lastDate, ex.getMessage());
		}
		
		return result;
	}
	
	private List<InventoryLevel> convert(List<Object[]> result) {
		List<InventoryLevel> inventoryLevels = new ArrayList<InventoryLevel>();
		
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				InventoryLevel item = new InventoryLevel();
				item.setItemSku(row[0] == null ? null : ((String)row[0]));
				item.setQuantity(row[1] == null ? null : ((Integer)row[1]));
				item.setModifiedDateTime(row[2] == null ? null : new DateTime((Date)row[2]));
				
				inventoryLevels.add(item);
			}
		}
		
		return inventoryLevels;
	}
}
