package Hardeng.Rest.services;


// import Hardeng.Rest.exceptions.BadRequestException;
import Hardeng.Rest.exceptions.NoDataException;
// import Hardeng.Rest.services.AdminServiceImpl.AdminDriverWrapper;
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

    // /**
    //  * Creates or alters an existing user
    //  * @param paramDict A Map Object with all Request body params
    //  * @return an object containing a status code
    //  * @throws BadRequestException
    //  */
    // StatusObject userMod (String username, String password,
    //                       AdminDriverWrapper paramDict) throws BadRequestException;
}
