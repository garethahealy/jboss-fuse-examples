package com.boohoo.esb.database.osgiservices.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.helpers.BooleanHelper;
import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.VendaFtpCredentials;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IVendaFtpCredentialsRepository;

public class VendaFtpCredentialsRepository extends BaseMySqlRepository implements IVendaFtpCredentialsRepository {
	
	private ILogger logger;
	
	public VendaFtpCredentialsRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public List<VendaFtpCredentials> getFtpFrontEnds(String sql) {
		List<VendaFtpCredentials> result = new ArrayList<VendaFtpCredentials>();
		try {
			TypedQuery<Object[]> query = createNamedTypedQuery(sql, Object[].class);
			
			result = convert(getResultList(query));
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getFtpFrontEnds", "Failed to get ftp frontends using {} because: {}", ex, sql, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getFtpFrontEnds", "Failed to get ftp frontends using {} because: {}", ex, sql, ex.getMessage());
		}
		
		return result;
	}

	private List<VendaFtpCredentials> convert(List<Object[]> result) {
		List<VendaFtpCredentials> vendaFtpCredentials = new ArrayList<VendaFtpCredentials>();
		if (result != null && result.size() > 0) {
			for (Object[] row : result) {		
				VendaFtpCredentials entity = new VendaFtpCredentials();
				entity.setId(row[0] == null ? Integer.valueOf(0) : ((Integer)row[0]));
				entity.setName(row[1] == null ? null : ((String)row[1]));
				entity.setHostname(row[2] == null ? null : ((String)row[2]));
				entity.setUsername(row[3] == null ? null : ((String)row[3]));
				entity.setPassword(row[4] == null ? null : ((String)row[4]));
				entity.setRemoteFolder(row[5] == null ? null : ((String)row[5]));
				entity.setPort(row[6] == null ? null : ((String)row[6]));
				entity.setIsDownload(row[7] == null ? Boolean.FALSE : BooleanHelper.parse(row[7].toString()));
				entity.setIsUpload(row[8] == null ? Boolean.FALSE : BooleanHelper.parse(row[8].toString()));
				entity.setIsUploadSwatches(row[9] == null ? Boolean.FALSE : BooleanHelper.parse(row[9].toString()));
				
				vendaFtpCredentials.add(entity);
			}
		}
		
		return vendaFtpCredentials;
	}
}
