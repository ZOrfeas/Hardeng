import React from 'react';
import ReactDOM from 'react-dom';
import ChargingExperience from './ChargingExperience';
import Header from './Header';
import 'materialize-css/dist/css/materialize.min.css';
import StationMonitoring from './StationMonitoring';
/* Imports */
import Map from './Map.js';


ReactDOM.render(
  <React.StrictMode>
    <Header />

    <ChargingExperience />
    
    {/* <StationMonitoring /> */}
    {/*<>*/}
    {/* <Map/> */}
  </React.StrictMode>,
  document.getElementById('root')
);


