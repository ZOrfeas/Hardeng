package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.AdminServiceImpl.StatusObject;
import Hardeng.Rest.services.AdminServiceImpl.UserObject;

public interface AdminService {
    
    /**
     * Performs a ping to the underlying database system
     * @return an object containing a status code
     */
    StatusObject isHealthy();

    /**
     * Clears ChargingSessions and initializes the
     * default administrative account
     * @return an object containing a status code
     */
    StatusObject resetSessions();

    /**
     * Fetches info regarding a particular user
     * @param username the username of the user in question
     * @return an object containing the info of a User
     */
    UserObject getUserInfo (String username) throws NoDataException;
}
