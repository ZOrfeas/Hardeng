package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;

/** Login controller business logic definitions */
public interface LoginService {
    /** Attempts to authenticate user (Driver/Admin) with provided credentials
     * @param username
     * @param password
     * @param type one of MASTER_ADMIN/STATION_ADMIN/DRIVER
     * @return DTO with JWT for requested user
     * @throws NoDataException
     */
    LoginObject login(String username, String password, String type) throws NoDataException;
}
