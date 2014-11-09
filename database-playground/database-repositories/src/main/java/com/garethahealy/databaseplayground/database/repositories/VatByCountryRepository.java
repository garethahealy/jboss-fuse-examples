package com.boohoo.esb.database.osgiservices.repositories;

import java.math.BigDecimal;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.boohoo.esb.common.logging.ILogger;
import com.boohoo.esb.database.common.entities.VatByCountry;
import com.boohoo.esb.database.common.exceptions.FailedSqlConnectionException;
import com.boohoo.esb.database.common.repositories.IVatByCountryRepository;
import com.boohoo.esb.database.osgiservices.RepositoryConstants;

public class VatByCountryRepository extends BaseMySqlRepository implements IVatByCountryRepository {
	
	private ILogger logger;
	
	public VatByCountryRepository(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public BigDecimal getVatByCountryFor(String country) {
		BigDecimal result = BigDecimal.ZERO;
		try {
			TypedQuery<BigDecimal> query = createNamedTypedQuery(VatByCountry.GET_VAT_BY_COUNTRY_FOR, BigDecimal.class);
			query.setParameter(RepositoryConstants.INDEX_ONE, country);
			
			result = getSingleResult(query);
		} catch (NoResultException ex) {
			logger.error(this.getClass(), "getVatByCountryFor", "No data returned for {} because: {}", ex, country, ex.getMessage());
		} catch (PersistenceException ex) {
			logger.error(this.getClass(), "getVatByCountryFor", "Failed to get for vat by country for {} because: {}", ex, country, ex.getMessage());
		} catch (FailedSqlConnectionException ex) {
			logger.error(this.getClass(), "getVatByCountryFor", "Failed to get for vat by country for {} because: {}", ex, country, ex.getMessage());
		}
		
		return result;
	}
	
}
