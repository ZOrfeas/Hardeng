import axios from 'axios';
axios.defaults.baseURL = "http://snf-880282.vm.okeanos.grnet.gr:8080/evcharge/api/";

const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNQVNURVJfQURNSU58bWFzdGVyLW9mLXB1cHBldHMiLCJleHAiOjE2MTQxMzg5NTYsImlhdCI6MTYxNDEwNzQyMH0.WpEqYtGzYtfIZnqjhRqDUaj_OSh8dRJK1edwfNrKOu1VzfFQdNu2_DzXvK-WQ3NCKa1J1ZexCDwO7yW-qhitWw';

const config = {
  headers: {
    'X-OBSERVATORY-AUTH': token
  }
};

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

  return axios.post(reqURL, obj, config);
}

export function getAdminInfo(adminKey) {
  const reqURL = "admin";
  const obj = {
    apiKey: adminKey,
  };

  return axios.post(reqURL, obj);
}

export function getStations(latlng){
  const radius = 0.25;
  const reqURL = "NearbyStations/" + latlng["lat"] + "/" + latlng["lng"] + "/" + radius;
  return axios.get(reqURL, config);
}

export function postInitiateSession(driverKey, stationID){
  const reqURL = "Session";
  const obj = {
    apiKey: driverKey,
    station_id: stationID
  };
  return axios.post(reqURL, obj, config);
}

export function getDriverCars(driverKey) {
  const reqURL = "driverCars";
  const obj = {
    apiKey: driverKey,
  };

  return axios.post(reqURL, obj, config);
}


