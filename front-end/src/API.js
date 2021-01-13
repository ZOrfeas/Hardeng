import axios from 'axios';
axios.defaults.baseURL = "";

export function userLogin(user, pass, type) {
  const reqURL = type;
  const obj = {
    username: user,
    password: pass,
  };

  return axios.post(reqURL, obj);
}

export function getDriverInfo(driverKey) {
  const reqURL = "driver";
  const obj = {
    apiKey: driverKey,
  };

  return axios.post(reqURL, obj);
}

export function getAdminInfo(adminKey) {
  const reqURL = "admin";
  const obj = {
    apiKey: adminKey,
  };

  return axios.post(reqURL, obj);
}

//export function getSt


