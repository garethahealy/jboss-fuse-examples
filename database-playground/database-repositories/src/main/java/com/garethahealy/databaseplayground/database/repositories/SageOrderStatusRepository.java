package com.boohoo.esb.database.osgiservices.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.SageOrderStatus;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.ISageOrderStatusRepository;

public class SageOrderStatusRepository  extends BaseMsSqlRepository implements ISageOrderStatusRepository {

	private ILogger logger;
	
	public SageOrderStatusRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<SageOrderStatus> getCancellationReturnsForOrderNumber(String orderNumber) {
		List<SageOrderStatus> result = new ArrayList<SageOrderStatus>();
		try {
			Query query = createNativeQuery(SageOrderStatus.GET_CANCELLATION_RETURNS_FOR_ORDER_NUMBER);
			query.setParameter(1, orderNumber);

			result = convert(getResultList(query));
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getCancellationReturnsForOrderNumber", "Failed to get cancellation returns for order number for {} because: {}", ex, orderNumber, ex.getMessage());
		}
		
		return result;
	}
	
	private List<SageOrderStatus> convert(List<Object[]> result) {
		List<SageOrderStatus> sageOrderStatuses = new ArrayList<SageOrderStatus>();

		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				SageOrderStatus item = new SageOrderStatus();
				item.setOrderNumber(row[0] == null ? null : ((String)row[0]));
				item.setItemCode(row[1] == null ? null : ((String)row[1]));
				item.setCancelledQuantity(row[2] == null ? Integer.valueOf(0) : ((BigDecimal)row[2]).intValue());
				item.setReturnedQuantity(row[3] == null ? Integer.valueOf(0) : ((BigDecimal)row[3]).intValue());

				sageOrderStatuses.add(item);
			}
		}
		
		return sageOrderStatuses;
	}
}
