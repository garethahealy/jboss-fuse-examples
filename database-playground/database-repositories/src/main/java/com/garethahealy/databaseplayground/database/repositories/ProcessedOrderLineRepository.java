package com.boohoo.esb.database.osgiservices.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.ProcessedOrderLine;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IProcessedOrderLineRepository;

public class ProcessedOrderLineRepository  extends BaseMsSqlRepository implements IProcessedOrderLineRepository {

	private ILogger logger;
	
	public ProcessedOrderLineRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<ProcessedOrderLine> getProcessedOrderLines(DateTime lastDate) {
		List<ProcessedOrderLine> result = new ArrayList<ProcessedOrderLine>();
		try {
			Query query = createNativeQuery(ProcessedOrderLine.GET_PROCESSED_ORDER_LINES);
			query.setParameter(1, lastDate.toDate(), TemporalType.TIMESTAMP);
			
			result = convert(getResultList(query));
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getProcessedOrderLines", "Failed to get processed order lines for {} because: {}", ex, lastDate, ex.getMessage());
		}
		
		return result;
	}
	
	private List<ProcessedOrderLine> convert(List<Object[]> result) {
		List<ProcessedOrderLine> processedOrderLines = new ArrayList<ProcessedOrderLine>();
		
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				ProcessedOrderLine item = new ProcessedOrderLine();
				item.setOrderNumber(row[0] == null ? null : ((String)row[0]));
				item.setSku(row[1] == null ? null : ((String)row[1]));
				item.setQuantity(row[2] == null ? Integer.valueOf(0) : ((BigDecimal)row[2]).intValue());
				item.setOrderDate(row[3] == null ? null : new DateTime((Date)row[3]));
				
				processedOrderLines.add(item);
			}
		}
		
		return processedOrderLines;
	}
}
