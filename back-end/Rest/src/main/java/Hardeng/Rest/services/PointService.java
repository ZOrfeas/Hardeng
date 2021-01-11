package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.PointServiceImpl.SessPointObject;

public interface PointService {
    
    /**
     * Fetch sessions by charging point and withing dates
     * @param pointId charging pointin question
     * @param dateFrom starting date
     * @param dateTo ending date
     * @return Object with the relevant info
     */
    SessPointObject sessionsPerPoint(Integer pointId,
        String dateFrom, String dateTo) throws NoDataException;
}
