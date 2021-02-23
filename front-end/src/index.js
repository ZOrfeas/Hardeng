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

<<<<<<< HEAD
    {/*<ChargingExperience />*/}
    <StationMonitoring />
=======
    <ChargingExperience />
    {/*<StationMonitoring />*/}
>>>>>>> 4a0d45923a24129b7ef8fbb392c86254b7177df8
    {/*<>*/}
    {/* <Map/> */}
  </React.StrictMode>,
  document.getElementById('root')
);


