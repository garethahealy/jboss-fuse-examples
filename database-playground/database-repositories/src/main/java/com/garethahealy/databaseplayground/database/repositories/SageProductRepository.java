package com.boohoo.esb.database.osgiservices.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.SageProduct;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ISageProductRepository;

public class SageProductRepository  extends BaseMsSqlRepository implements ISageProductRepository {

	private ILogger logger;
	
	public SageProductRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<SageProduct> getAllSageProducts() {
		List<SageProduct> result = new ArrayList<SageProduct>();
		try {
			Query query = createNativeQuery(SageProduct.GET_ALL_PRODUCTS);
			
			result = convert(getResultList(query));
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getAllSageProducts", "Failed to get all sage products because: {}", ex, ex.getMessage());
		}
		
		return result;
	}
	
	private List<SageProduct> convert(List<Object[]> result) {
		List<SageProduct> sageProducts = new ArrayList<SageProduct>();
		
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				SageProduct item = new SageProduct();
				item.setSku(row[0] == null ? null : ((String)row[0]));
				item.setName(row[1] == null ? null : ((String)row[1]));
				item.setProductGroup(row[2] == null ? null : ((String)row[2]));
				item.setColour(row[3] == null ? null : ((String)row[3]));
				item.setSize(row[4] == null ? null : ((String)row[4]));
				item.setProductWeight(row[5] == null ? 0 : ((Integer)row[5]));
				item.setGbpStd(row[6] == null ? Double.valueOf(0) : ((BigDecimal)row[6]).doubleValue());
				item.setGbpSp(row[7] == null ? Double.valueOf(0) : ((BigDecimal)row[7]).doubleValue());
				item.setCategory(row[8] == null ? null : ((String)row[8]));
				item.setDepartment(row[9] == null ? null : (String)row[9]);
				item.setSleeveLength(row[10] == null ? null : (String)row[10]);
				item.setOccasion(row[11] == null ? null : ((String)row[11]));
				item.setStyle(row[12] == null ? null : ((String)row[12]));
				item.setTrend(row[13] == null ? null : ((String)row[13]));
				item.setCollection(row[14] == null ? null : ((String)row[14]));
				item.setGender(row[15] == null ? null : ((String)row[15]));
				item.setAverageBuyingPrice(row[16] == null ? Double.valueOf(0) : ((BigDecimal)row[16]).doubleValue());
				
				sageProducts.add(item);
			}
		}
		
		return sageProducts;
	}
}
