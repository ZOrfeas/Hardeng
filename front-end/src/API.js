import axios from 'axios';
axios.defaults.baseURL = "http://snf-880282.vm.okeanos.grnet.gr:8080/evcharge/api/";

const token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNQVNURVJfQURNSU58bWFzdGVyLW9mLXB1cHBldHMiLCJleHAiOjE2MTQxMzg5NTYsImlhdCI6MTYxNDEwNzQyMH0.WpEqYtGzYtfIZnqjhRqDUaj_OSh8dRJK1edwfNrKOu1VzfFQdNu2_DzXvK-WQ3NCKa1J1ZexCDwO7yW-qhitWw';

const config = {
  headers: {
    'X-OBSERVATORY-AUTH': token
  }
};

//const driverKey = localStorage.getItem("driverKey");
//const driverID = localStorage.getItem("driverID");
const adminKey = localStorage.getItem("adminKey");
const adminID = localStorage.getItem("adminID")

export function userLogin(user, pass, type) {
  var reqURL;
  type === 'driver' ? reqURL = "login/DRIVER" : reqURL = "login/ADMIN";

  const obj = new URLSearchParams();
  obj.append('username', user);
  obj.append('password', pass);

  const urlencoded = {
    headers: {
      'content-Type': 'application/x-www-form-urlencoded'
    }
  }

  return axios.post(reqURL, obj, urlencoded);
}

export function getUserID(driverKey){
  const reqURL = 'Driver/getId';
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function getDriverInfo(driverKey,driverID) {
  const reqURL = "Driver/" + driverID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey
    }
  };

  return axios.get(reqURL, auth); 
}

export function updateDriverInfo(driverKey,driverID,obj) {
  const reqURL = "Driver/" + driverID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey
    }
  };

  return axios.put(reqURL, obj, auth); 
}

export function getAdminInfo(adminKey) {
  const reqURL = "admin";
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': adminKey,
    }
  };

  return axios.post(reqURL, auth);
}

export function getStations(driverKey, latlng){
  const radius = 0.25;
  const reqURL = "NearbyStations/" + latlng["lat"] + "/" + latlng["lng"] + "/" + radius;

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function getDriverCars(driverKey, driverID) {
  const reqURL = "EVsPerDriver/" + driverID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function getDriverPolicies(driverKey, driverID) {
  const reqURL = "PricePoliciesPerDriver/" + driverID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function userPay(driverKey, type, credential){
  const reqURL = "payment";
  const obj = {
    paymentType: type,
    account: credential,
  };

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.post(reqURL, obj, auth);
}

export function postInitiateSession(driverKey, stationID){
  const reqURL = "InitiateSession/" + stationID;

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };

  return axios.post(reqURL, null, auth);
}

export function logSession(driverKey, obj){
  const reqURL = "FinalizeSession";

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };
  
  return axios.post(reqURL, obj, auth);
}




