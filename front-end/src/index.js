import React from 'react';
import ReactDOM from 'react-dom';
import 'materialize-css/dist/css/materialize.min.css';
import ChargingExperience from './ChargingExperience';
import LoginForm from './LoginForm';
/* Imports */


ReactDOM.render(
  <React.StrictMode>
    <div className="row">
      <LoginForm/>
      <div> {'Hello' + localStorage.getItem('username')} </div>
    </div>

    <ChargingExperience />
    {/*<StationMonitoring />*/}
    {/*<>*/}
  </React.StrictMode>,
  document.getElementById('root')
);


