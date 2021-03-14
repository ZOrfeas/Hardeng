import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import ChargingExperience from './ChargingExperience';
import Header from './Header';
import 'materialize-css/dist/css/materialize.min.css';
import EnergyMonitoring from './EnergyMonitoring';
import Home from './Home';
import EditUserInfo from './EditUserInfo';

ReactDOM.render(
  <Router>
    <React.StrictMode>
      <Header />

      <Route exact ={true} path="/" render = {() =>(
        <Home/>
        // <EnergyMonitoring/>
      )}/>

      <Route exact ={true} path="/ChargingExperience" render = {() =>(
        <ChargingExperience />
      )}/>

      <Route exact ={true} path="/EnergyMonitoring" render = {() =>(
        <EnergyMonitoring/>
      )}/>

      <Route exact ={true} path="/EditUserInfo" render = {() =>(

        <EditUserInfo/>
      )}/>

    </React.StrictMode>
  </Router>,
  document.getElementById('root')
);