import React, { useState } from 'react';
import "./Home.css";
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'
import M, { Autocomplete } from 'materialize-css';
import { AiOutlineSend } from "react-icons/ai";
import { getStations } from './API';
import Map from './Map.js';
import image from './icons/image4.jpg'

class Home extends React.Component{
  constructor(props){
    super(props);
  }
  render(){
    return(
      <div>
        <div className="row">
          <div style={{ 
              backgroundImage: `url(${image})`,
              backgroundRepeat: 'no-repeat',
              height: '1000px',
              backgroundAttachment: 'fixed',
              backgroundSize: '100% 100%',
              }} className = "background">
                
            <div className="row">
              <div className="col s12">
                <div className="navbar-fixed">
                  <nav className="transparent z-depth-0" id="home-nav">
                    <div className="nav-wrapper">
                      <a href="" className="home-brand-logo">Logo Placeholder</a>
                      <ul id="nav-mobile" className="right hide-on-med-and-down">
                        <li><a href="/StationMonitoring" className="home-link">
                        Station Monitoring
                        </a></li>
                        <li><a href="/ChargingExperience" className="home-link">
                        Charging Experience
                        </a></li>
                        <li><a href="/EnergyMonitoring" className="home-link">
                        Energy Monitoring
                        </a></li>
                      </ul>
                      <div class="divider"></div>
                    </div>
                  </nav >
                </div>
              </div>
            </div>
            
          </div>
          
        </div>
      </div>
    );
  }
}

export default Home;