import React, { useState } from 'react';
import "./Home.css";
import 'leaflet/dist/leaflet.css'
import image from './icons/image4.jpg'

class Home extends React.Component{
  constructor(props){
    super(props);

    this.state = {
      driverKey: localStorage.getItem("driverKey"),
      adminKey: localStorage.getItem("adminKey"),
    }

    this.choosePage = this.choosePage.bind(this);
  }

  choosePage(e){
    window.location = e.target.value;
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
                        <li>
                          <button 
                          onClick={this.choosePage}
                          value="\ChargingExperience"
                          className="btn-flat"
                          disabled={this.state.driverKey === null}>
                            Charging Experience
                          </button>
                        </li>
                        <li>
                          <button 
                          onClick={this.choosePage}
                          value="\EnergyMonitoring"
                          className="btn-flat"
                          disabled={this.state.adminKey === null}>
                            Energy Monitoring
                          </button>
                        </li>
                        <li>
                          <button 
                          onClick={this.choosePage}
                          value="\EditUserInfo"
                          className="btn-flat"
                          disabled={this.state.driverKey === null}>
                            Edit Profile
                          </button>
                        </li>
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