package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.ProductCache;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IProductCacheRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class ProductCacheRepository extends BaseMySqlRepository implements IProductCacheRepository {
	
	private ILogger logger;
	
	public ProductCacheRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public Integer createOrUpdateProductCache(String styleSku, String productJson, String createdDateTime, String updatedDateTime) {
		Integer result = -1;
		try {
			Query query = createNamedQuery(ProductCache.CREATE_OR_UPDATE_PRODUCT_CACHE);
			query.setParameter(RepositoryConstants.INDEX_ONE, styleSku);
			query.setParameter(RepositoryConstants.INDEX_TWO, productJson);
			query.setParameter(RepositoryConstants.INDEX_THREE, createdDateTime);
			query.setParameter(RepositoryConstants.INDEX_FOUR, updatedDateTime);
			
			result = createOrUpdate(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "createOrUpdateProductCache", "Failed to create/update product cache for {} because: {}", ex, styleSku, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "createOrUpdateProductCache", "Failed to create/update product cache for {} because: {}", ex, styleSku, ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public List<ProductCache> getProductCache(String updatedDateTime) {
		List<ProductCache> result = null;
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(ProductCache.GET_PRODUCT_CACHE, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, updatedDateTime);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getProductCache", "Failed to get product cache after {} because: {}", ex, updatedDateTime, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getProductCache", "Failed to get product cache after {} because: {}", ex, updatedDateTime, ex.getMessage());
		}
		
		return result;
	}
	
	@Override
	public List<ProductCache> getProductCacheUpdatedByPaginated(Integer startRows, Integer endRows) {
		List<ProductCache> result = null;
        try {
        	TypedQuery<Object[]> query = createNamedTypedQuery(ProductCache.GET_PRODUCT_CACHE_UPDATED_BY_PAGINATED, Object[].class);
			query.setParameter(RepositoryConstants.INDEX_ONE, startRows);
			query.setParameter(RepositoryConstants.INDEX_TWO, endRows);
            
			result = convert(getResultList(query));
        } catch (PersistenceException ex) {
			logger.error(this.getClass(), "getProductCacheUpdatedByPaginated", "Failed to get product updated by paginated for {} / {} because: {}", ex, startRows, endRows, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getProductCacheUpdatedByPaginated", "Failed to get product updated by paginated for {} / {} because: {}", ex, startRows, endRows, ex.getMessage());
		}
        
        return result;
    }

	@Override
	public Integer getProductCacheCount() {
		Integer result = 0;
		try {
			TypedQuery<Integer> query = createNamedTypedQuery(ProductCache.GET_PRODUCT_CACHE_COUNT, Integer.class);
			
			result = getSingleResult(query);
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getProductCacheUpdatedByPaginated", "Failed to get product cache count because: {}", ex, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getProductCacheUpdatedByPaginated", "Failed to get product cache count because: {}", ex, ex.getMessage());
		}

		return result;
	}
	
	private List<ProductCache> convert(List<Object[]> result) {
		List<ProductCache> productCaches = new ArrayList<ProductCache>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				ProductCache entity = new ProductCache();
				entity.setId(row[0] == null ? Integer.valueOf(0) : ((Integer)row[0]));
				entity.setSku(row[1] == null ? null : ((String)row[1]));
				entity.setJson(row[2] == null ? null : ((String)row[2]));
				entity.setCreatedDateTime(row[3] == null ? null : ((String)row[3]));
				entity.setUpdatedDateTime(row[4] == null ? null : ((String)row[4]));
				
				productCaches.add(entity);
			}
		}
		
		return productCaches;
	}
}
