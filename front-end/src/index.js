import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Link, Route, Redirect} from 'react-router-dom';
import ChargingExperience from './ChargingExperience';
import Header from './Header';
import 'materialize-css/dist/css/materialize.min.css';
import StationMonitoring from './StationMonitoring';
import Home from './Home';
/* Imports */
import Map from './Map.js';


ReactDOM.render(
  <React.StrictMode>
    <Header />

    <ChargingExperience />
    {/*<StationMonitoring />*/}
    {/*<>*/}
    {/* <Map/> */}
  </React.StrictMode>,
  document.getElementById('root')
);


