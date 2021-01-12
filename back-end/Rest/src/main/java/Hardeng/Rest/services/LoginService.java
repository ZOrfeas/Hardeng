package Hardeng.Rest.services;

import Hardeng.Rest.exceptions.NoDataException;
import Hardeng.Rest.services.LoginServiceImpl.LoginObject;

public interface LoginService {
    LoginObject login(String username, String password) throws NoDataException;
}
