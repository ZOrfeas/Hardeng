package Hardeng.Rest.services;

import Hardeng.Rest.services.AdminServiceImpl.StatusObject;

public interface AdminService {
    
    StatusObject isHealthy();

    StatusObject resetSessions();
}
