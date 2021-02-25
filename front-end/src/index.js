import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Link, Route, Redirect} from 'react-router-dom';
import ChargingExperience from './ChargingExperience';
import Header from './Header';
import 'materialize-css/dist/css/materialize.min.css';
import StationMonitoring from './StationMonitoring';
import Home from './Home';

ReactDOM.render(
  <Router>
    <React.StrictMode>
      <Header />

      <Route exact ={true} path="/" render = {() =>(
        <Home/>
      )}/>

      <Route exact ={true} path="/ChargingExperience" render = {() =>(
        <ChargingExperience />
      )}/>

      <Route exact ={true} path="/StationMonitoring" render = {() =>(
        <StationMonitoring />
      )}/>

    </React.StrictMode>
  </Router>,
  document.getElementById('root')
);