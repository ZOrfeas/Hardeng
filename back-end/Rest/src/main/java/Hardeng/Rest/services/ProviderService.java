package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.ProviderServiceImpl.SessProvObject;

public interface ProviderService {
    /**
     * Fetch sessions per provider within specific date time
     * @param providerId provider requested
     * @param dateFrom starting Date
     * @param dateTo ending date
     * @return Object with requested info
     */
    SessProvObject sessionsPerProvider(Integer providerId,
        String dateFrom, String dateTo) throws NoDataException;
}