import axios from 'axios';
import https from 'https';
// import * as fs from 'fs';

const httpsAgent = new https.Agent({
  ca:`-----BEGIN CERTIFICATE-----
MIIEnzCCA4egAwIBAgIUFMVHMowlmFsrJVnFIRKmWr6RwwowDQYJKoZIhvcNAQEL
BQAwgd4xCzAJBgNVBAYTAkdSMQ8wDQYDVQQIDAZBdHRpY2ExDzANBgNVBAcMBkF0
aGVuczEwMC4GA1UECgwnTmF0aW9uYWwgVGVjaG5pY2FsIFVuaXZlcnNpdHkgb2Yg
QXRoZW5zMSowKAYDVQQLDCFFbGVjdHJpY2FsICYgQ29tcHV0ZXIgRW5naW5lZXJp
bmcxJzAlBgNVBAMMHnNuZi04ODAyODIudm0ub2tlYW5vcy5ncm5ldC5ncjEmMCQG
CSqGSIb3DQEJARYXZWwxNzE2MEBjZW50cmFsLm50dWEuZ3IwHhcNMjEwMzEwMjAy
NDI0WhcNMjIwMzEwMjAyNDI0WjCB3jELMAkGA1UEBhMCR1IxDzANBgNVBAgMBkF0
dGljYTEPMA0GA1UEBwwGQXRoZW5zMTAwLgYDVQQKDCdOYXRpb25hbCBUZWNobmlj
YWwgVW5pdmVyc2l0eSBvZiBBdGhlbnMxKjAoBgNVBAsMIUVsZWN0cmljYWwgJiBD
b21wdXRlciBFbmdpbmVlcmluZzEnMCUGA1UEAwwec25mLTg4MDI4Mi52bS5va2Vh
bm9zLmdybmV0LmdyMSYwJAYJKoZIhvcNAQkBFhdlbDE3MTYwQGNlbnRyYWwubnR1
YS5ncjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMfKxpiPs1qRtQ8e
iloAsCg4XX/XgAI4ksddQoVHyVqOpqfE9s2faxn64tSACw/UuG0us03lCNkyxo06
5Dhh0aCr3phpjsatfPNXwryHl+y0DZWovabyy9H3qDEL2UoduLvql9snLNSv9ISm
W52J+BGilfTo/mUdwukYD6IRg/OB7LVx9RqGvvWZlyJ6kfzzIECDx9Xg2sUXRt2/
NLsM/xoqgGzT1vvO+HpueTH4TOUuaUOFD+ry7ZiA/E8/GiIa6n2VVVmTSieDKqHr
DU3tCCwK530cfpa2+IH7Lw571EadOn4lXuNxzRlQ+wai75GIPtW3lisJzPP0QXw0
sYXBO2cCAwEAAaNTMFEwHQYDVR0OBBYEFAouqTGJM3SCy5WFjxf4TD4YvcRkMB8G
A1UdIwQYMBaAFAouqTGJM3SCy5WFjxf4TD4YvcRkMA8GA1UdEwEB/wQFMAMBAf8w
DQYJKoZIhvcNAQELBQADggEBAHdzGmto+pG28XjaZb8eb+fRut1f6/fCMgRurhrD
tmWH3zFSIa+2ZYCxFgxHdwDL3r64Oe5ekVust3l6nHUCE1TPOfbenI19u2o2l0aT
Ic4HsysnIlXU+KoW2Hvd6ZLnvcagfVMLwDCRZFd54saNLZgx4dEP2sQCeQAtGDXG
zWlasheuLO/tye4aN84orFONN9xUhXi5UsPlSoQ6JLnnQ+4dfbXr0u6BfHe7WXjQ
OOzLFhgquh6mSMjxeNF40CJt4ZMwBOiljML3EJeN8K1bYz54zXprsFlbYipIo09Q
GnXNArQbpyOBQwLEAC+GlP+lm7C/a7FqwLYxqc6bbFGzrRo=
-----END CERTIFICATE-----`
})

axios.defaults.baseURL = "https://hardeng.ddns.net:5000/";
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