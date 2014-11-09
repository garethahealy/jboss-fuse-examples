package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.database.common.entities.StockUpdateHistory;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IStockUpdateHistoryRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class StockUpdateHistoryRepository extends BaseMySqlRepository implements IStockUpdateHistoryRepository {
	
	@Override
	public Integer clearDataForChannelStockUpdateHistory(String frontendName) throws ExecuteSqlException, FailedSqlConnectionException {
		Integer result = -1;
		try {
			Query query = createNamedQuery(StockUpdateHistory.CLEAR_DATA_FOR_CHANNEL_STOCKUP_DATE_HISTORY);
			query.setParameter(RepositoryConstants.INDEX_ONE, frontendName);
			
			result = createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to clear data for channel stock update history for " + frontendName + " because: " + ex.getMessage(), ex);
		}
		
		return result;
	}
	
	@Override
	public Integer createOrUpdateStockUpdateHistory(String sku, Integer quantity, String updateDateTime, String reasonForChange) throws ExecuteSqlException,
	        FailedSqlConnectionException {
		
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(StockUpdateHistory.CREATE_OR_UPDATE_STOCK_UPDATE_HISTORY, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, sku);
			query.setParameter(RepositoryConstants.INDEX_TWO, quantity);
			query.setParameter(RepositoryConstants.INDEX_THREE, updateDateTime);
			query.setParameter(RepositoryConstants.INDEX_FOUR, reasonForChange);
			
			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to createOrUpdateStockUpdateHistory for " + sku + " / " + quantity + " / " + updateDateTime + " / " +  reasonForChange 
					+ " because: " + ex.getMessage(), ex);
		}
		
		return result;
	}
	
	@Override
	public List<String> getIncorrectInventoryFor(String frontendName) throws ExecuteSqlException, FailedSqlConnectionException {
		List<String> result = new ArrayList<String>();
		try {
			TypedQuery<String> query = createNamedTypedQuery(StockUpdateHistory.GET_INCORRECT_INVENTORY_FOR, String.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, frontendName);
			
			result = getResultList(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get incorrect inventory for " + frontendName + " because: " + ex.getMessage(), ex);
		}
				
		return result;
	}
	
	@Override
	public void updateChannelStockUpdateHistory(String frontendName) throws ExecuteSqlException, FailedSqlConnectionException {
		try {
			Query query = createNamedQuery(StockUpdateHistory.UPDATE_CHANNEL_STOCK_UPDATE_HISTORY);
			query.setParameter(RepositoryConstants.INDEX_ONE, frontendName);
			
			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException(ex.getMessage(), ex);
		}
	}
	
	@Override
	public void loadChannelStockUpdateHistory(String tableName, String filePath) throws ExecuteSqlException, FailedSqlConnectionException {
		String loadFile = "LOAD DATA LOCAL INFILE '" + filePath + "' INTO TABLE " + tableName
		        + " FIELDS TERMINATED BY ',' IGNORE 1 LINES (@sku, @parentSku, @quantity, @updatedTime, @isPublished) SET Sku = @sku, Quantity = @quantity, "
		        + "UpdatedDateTime = str_to_date(@updatedTime, '%Y%m%d%H%i%s')";
		
		try {
			Query query = createNativeQuery(loadFile);
			
			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException(ex.getMessage(), ex);
		}
		
	}
}
