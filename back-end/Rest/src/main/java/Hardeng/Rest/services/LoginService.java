package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;

/** Login controller business logic definitions */
public interface LoginService {
    LoginObject login(String username, String password, String type) throws NoDataException;
}
