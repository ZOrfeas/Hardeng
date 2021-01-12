import axios from 'axios';
axios.defaults.baseURL = "";

export function login(user, pass) {
  const reqURL = "drivers";
  const obj = {
    username: user,
    password: pass,
  };

  return axios.post(reqURL, obj);
}



