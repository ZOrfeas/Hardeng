package Hardeng.Rest.services;

import Hardeng.Rest.services.PointServiceImpl.SessPointObject;

public interface PointService {
    
    SessPointObject sessionsPerPoint(Integer pointId,
        String dateFrom, String dateTo);
}
