package com.boohoo.esb.database.osgiservices.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.commerce.modules.inventory.entities.Inventory;
import com.boohoo.esb.common.helpers.ProductHelper;
import com.boohoo.esb.common.helpers.StringHelper;
import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.SageMasterStock;
import com.boohoo.esb.database.common.exceptions.ExecuteSqlException;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ISageMasterStockRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class SageMasterStockRepository extends BaseMySqlRepository implements ISageMasterStockRepository {
	
	private ILogger logger;
	
	public SageMasterStockRepository(ILogger logger) {
		this.logger = logger;
	}
	@Override
	public void updateSageMasterStock(String sku, int quantity, String updateDateTime) throws FailedSqlConnectionException, ExecuteSqlException {
		try {
			Query query = createNamedQuery(SageMasterStock.UPDATE_SAGE_MASTER_STOCK);
			query.setParameter(RepositoryConstants.INDEX_ONE, sku);
			query.setParameter(RepositoryConstants.INDEX_TWO, ProductHelper.getSkuWithoutDashes(sku));
			query.setParameter(RepositoryConstants.INDEX_THREE, quantity);
			query.setParameter(RepositoryConstants.INDEX_FOUR, updateDateTime);
			
			createOrUpdate(query);
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to update sage master stock for " + sku + " / " + quantity + " / " + updateDateTime + " because: " + ex.getMessage(), ex);
		}
	}
	
	@Override
	public SageMasterStock getInventory(String sku) throws FailedSqlConnectionException, ExecuteSqlException {
		SageMasterStock result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(SageMasterStock.GET_INVENTORY, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, sku);
			
			result = convert(getSingleResult(query));
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getInventory", "No data returned for {} because: {}", ex, sku, ex.getMessage());
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get inventory for " + sku + " because: " + ex.getMessage(), ex);
		}
		
		return result;
	}
	
	@Override
	public Integer getInventoryWithHandledExceptions(String sku, Integer currentQuantity) {
		SageMasterStock inventory = null;
	
		try {
			inventory = getInventory(sku);
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getInventoryWithHandledExceptions", "Failed to get inventory for {} because: {}", ex, sku, ex.getMessage());
		} catch (ExecuteSqlException ex) {
			logger.error(this.getClass(), "getInventoryWithHandledExceptions", "Failed to get inventory for {} because: {}", ex, sku, ex.getMessage());
		}

		Integer tempQuantity = inventory == null ? currentQuantity : inventory.getQuantity();
		return new Inventory(sku, tempQuantity).getQuantity();
	}
	
	@Override
	public Map<String, SageMasterStock> getAllInventory() throws FailedSqlConnectionException, ExecuteSqlException {
		List<SageMasterStock> result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(SageMasterStock.GET_ALL_INVENTORY, Object[].class);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get all inventory because: " + ex.getMessage(), ex);
		}
		
		Map<String, SageMasterStock> inventories = new HashMap<String, SageMasterStock>();
		if (result != null) {
			for (SageMasterStock row : result) {
				inventories.put(row.getSku(), row);
			}
		}
		
		return inventories;
	}
	
	@Override
	public String getSageMasterStockSku(String skuWithoutDashes) throws FailedSqlConnectionException, ExecuteSqlException {
		String result = "";
		try {
			TypedQuery<String> query = createNamedTypedQuery(SageMasterStock.GET_SAGE_MASTER_STOCK_SKU, String.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, skuWithoutDashes);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getSageMasterStockSku", "No data returned for {} because: {}", ex, skuWithoutDashes, ex.getMessage());
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get sage master stock sku for " + skuWithoutDashes + " because: " + ex.getMessage(), ex);
		}
		
		return StringHelper.trim(result);
	}
	
	@Override
	public Integer getMasterInventory(String skuToFind) throws FailedSqlConnectionException, ExecuteSqlException {
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(SageMasterStock.GET_MASTER_INVENTORY, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, skuToFind);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getMasterInventory", "No data returned for {} because: {}", ex, skuToFind, ex.getMessage());
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to get master inventory for " + skuToFind + " because: " + ex.getMessage(), ex);
		}
		
		return result;
	}
	
	@Override
	public Integer updateMasterStockViaAdjustment(String sku, Integer adjustment) throws FailedSqlConnectionException, ExecuteSqlException {
		Integer result = -1;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(SageMasterStock.UPDATE_MASTER_STOCK_VIA_ADJUSTMENT, Integer.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, sku);
			query.setParameter(RepositoryConstants.INDEX_TWO, ProductHelper.getSkuWithoutDashes(sku));
			query.setParameter(RepositoryConstants.INDEX_THREE, adjustment);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "updateMasterStockViaAdjustment", "No data returned for {} / {} because: {}", ex, sku, adjustment, ex.getMessage());
		} catch (PersistenceException ex) {
			throw new ExecuteSqlException("Failed to update master stock via adjustment for " + sku + " / " + adjustment + " because: " +ex.getMessage(), ex);
		}
		
		return result;
	}
	
	private List<SageMasterStock> convert(List<Object[]> result) {
		List<SageMasterStock> sageMasterStocks = new ArrayList<SageMasterStock>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {			
				SageMasterStock entity = convert(row);
				
				sageMasterStocks.add(entity);
			}
		}
		
		return sageMasterStocks;
	}
	
	private SageMasterStock convert(Object[] result) {
		SageMasterStock entity = new SageMasterStock();
		if (result != null) {		
			entity.setId(result[0] == null ? Integer.valueOf(0) : ((Integer)result[0]));
			entity.setSku(result[1] == null ? null : ((String)result[1]));
			entity.setSkuWithoutDash(result[2] == null ? null : ((String)result[2]));
			entity.setQuantity(result[3] == null ? Integer.valueOf(0) : ((BigDecimal)result[3]).intValue());
			entity.setUpdatedDateTime(result[4] == null ? null : ((String)result[4]));
		}
		
		return entity;
	}
}
