package Hardeng.Rest.services;


import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.exceptions.NoDataException;

import org.springframework.web.multipart.MultipartFile;

// import Hardeng.Rest.services.AdminServiceImpl.AdminDriverWrapper;
import Hardeng.Rest.config.auth.CustomUserPrincipal;
import Hardeng.Rest.services.AdminServiceImpl.SessionStatsObject;
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
     * @throws NoDataException
     */
    UserObject getUserInfo (String username) throws NoDataException;

     /**
      * Creates or alters an existing user
      * @param driverName driver's name 
      * @param role A Map Object with all Request body params
      * @param email user's email
      * @return an object containing a status code
      * @throws BadRequestException
      */
    StatusObject userMod (String driverName, String username, String password, String role,
    String email ) throws BadRequestException;

    /**
     * Service to import sessions from csv
     * @param file Contains the sessions to be imported
     * @return Returns an object containing db stats after import
     */
    SessionStatsObject sessionUpdate(MultipartFile file);
}
