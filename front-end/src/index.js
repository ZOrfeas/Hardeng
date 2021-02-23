import React from 'react';
import ReactDOM from 'react-dom';
import ChargingExperience from './ChargingExperience';
import Header from './Header';
import 'materialize-css/dist/css/materialize.min.css';
import StationMonitoring from './StationMonitoring';
/* Imports */


ReactDOM.render(
  <React.StrictMode>
    <Header />

    <ChargingExperience />
    {/*<StationMonitoring />*/}
    {/*<>*/}
  </React.StrictMode>,
  document.getElementById('root')
);


