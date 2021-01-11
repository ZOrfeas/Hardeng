package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.EVServiceImpl.SessEVObject;

public interface EVService {
    
    SessEVObject sessionsPerEV(Integer driverId, Integer carId,
        String dateFrom, String dateTo) throws NoDataException;
}