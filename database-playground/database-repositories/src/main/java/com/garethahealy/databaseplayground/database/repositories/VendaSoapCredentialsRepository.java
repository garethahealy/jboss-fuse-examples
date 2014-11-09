package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.VendaSoapCredentials;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IVendaSoapCredentialsRepository;

public class VendaSoapCredentialsRepository extends BaseMySqlRepository implements IVendaSoapCredentialsRepository {
	
	private ILogger logger;
	
	public VendaSoapCredentialsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<VendaSoapCredentials> getVendaSoapCredentials() {
		List<VendaSoapCredentials> result = new ArrayList<VendaSoapCredentials>();
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(VendaSoapCredentials.GET_VENDA_SOAP_CREDENTIALS, Object[].class);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFtpFrontEnds", "Failed to get soap credentials because: {}", ex, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFtpFrontEnds", "Failed to get soap credentials because: {}", ex, ex.getMessage());
		}
		
		return result;
	}

	private List<VendaSoapCredentials> convert(List<Object[]> result) {
		List<VendaSoapCredentials> vendaSoapCredentials = new ArrayList<VendaSoapCredentials>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {
				VendaSoapCredentials entity = new VendaSoapCredentials();
				entity.setId(row[0] == null ? Integer.valueOf(0) : ((Integer)row[0]));
				entity.setOperation(row[1] == null ? null : ((String)row[1]));
				entity.setConsumerName(row[2] == null ? null : ((String)row[2]));
				entity.setUsername(row[3] == null ? null : ((String)row[3]));
				entity.setPassword(row[4] == null ? null : ((String)row[4]));
				
				vendaSoapCredentials.add(entity);
			}
		}
		
		return vendaSoapCredentials;
	}
}
