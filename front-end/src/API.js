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
//const adminKey = localStorage.getItem("adminKey");
//const adminID = localStorage.getItem("adminID")

export function userLogin(user, pass, type) {
  var reqURL;
  type === 'driver' ? reqURL = "login/DRIVER" : reqURL = "login/STATION_ADMIN";

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

export function getUserID(Key,type){

  var reqURL;
  type === 'driver' ? reqURL = 'Driver/getId' : reqURL = "admin/getId";

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': Key,
    }
  };

  return axios.get(reqURL, auth);
}

export function getDriverInfo(driverKey, driverID) {
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

export function postPricePolicy(driverKey, driverID, PricePolicyID){
  const reqURL = "/AddPricePolicy/" + PricePolicyID + "/" + driverID;

  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': driverKey,
    }
  };
  
  return axios.post(reqURL, null, auth);
}

export function getAdminPolicies(adminKey, adminID) {
  const reqURL = "AdminPricePolicies/" + adminID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': adminKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function updateAdminPolicies(adminKey,PricePolicyID,obj) {
  const reqURL = "PricePolicy/" + PricePolicyID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': adminKey
    }
  };

  return axios.put(reqURL, obj, auth); 
}


export function getAdminStations(adminKey, adminID) {
  const reqURL = "AdminStations/" + adminID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': adminKey,
    }
  };

  return axios.get(reqURL, auth);
}

export function updateAdminStation(adminKey,stationID,obj) {
  const reqURL = "Station/" + stationID;
  const auth = {
    headers: {
      'X-OBSERVATORY-AUTH': adminKey
    }
  };

  return axios.put(reqURL, obj, auth); 
}

export function getAdminAreaStationEnergy(adminKey, adminID, latlng,radius,dateFrom,dateTo){

  const reqURL = "AdminAreaStationEnergy/";

  const headers= {
    'X-OBSERVATORY-AUTH': adminKey
  }
  const params = {
    "latitude": latlng["lat"],
    "longitude": latlng["lng"],
    "radius": radius,
    "dateFrom": dateFrom,
    "dateTo": dateTo,
    "adminId": adminID,
    
  }
  return axios.get(reqURL, {params, headers});
}

export function getAdminTotalEnergy(adminKey, adminID, dateFrom, dateTo){

const reqURL = "admin/totalEnergy/" + adminID + "/" + dateFrom + "/" + dateTo;

const auth = {
  headers: {
    'X-OBSERVATORY-AUTH': adminKey
  }
};
  return axios.get(reqURL, auth);
}