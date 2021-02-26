import React, { useState } from 'react';
import "./EditUserInfo.css";
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'
import M, { Autocomplete } from 'materialize-css';
import { AiOutlineSend } from "react-icons/ai";
import { getStations } from './API';
import Map from './Map.js';
import image from './icons/image3.jpg'

const driversHardcoded = [
  {driver_name: "Kostas", id: 11111, bonus_points: 10, carID: 13,  email: "kostas@kostas.gr", walletID: 12315464758},
];



class EditUserInfo extends React.Component {
  constructor(props) {
    super(props);
    this.setState({
      username: "",
    });
    this.state = {
      username: driversHardcoded[0]["driver_name"],
      email: driversHardcoded[0]["email"],
      bonusPoints: driversHardcoded[0]["bonus_points"],
      wallet: driversHardcoded[0]["walletID"],
    };
  }

  submitChanges(){
    if (window.confirm("U sur?????")) {
        //get driver with id and change info
    } 
    else {
        //do nothing
    }
  }
 
  componentDidMount(){
    document.addEventListener('DOMContentLoaded', function() {
      var elems = document.querySelectorAll('.datepicker');
      var instances = M.Datepicker.init(elems, {format: 'yyyy-mm-dd'});
    });
  }
  
   render(){
     return(
      
      <div className="a">
        <div style={{ 
              backgroundImage: `url(${image})`,
              width:'100%',
              backgroundRepeat: 'no-repeat',
              height: '1000px',
              backgroundSize: 'cover',
              }} className = "background">
          <div className="viewUserInfo">
            <div className="row">
              <div className="col s12">
                  <div className="row">
                    <div className="col s3" id='res'>
                      <div className="card blue-grey darken-1 hoverable">
                        <div className="card-content white-text">
                          <span className="card-title">Driver's Info.</span>
                        </div>
                        <div className="card-action">
                          <div class="input-field col s6">
                            <input placeholder={this.state.username} id="first_name" type="text" class="validate"/>
                            <label for="first_name"> User Name</label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.email} id="first_name" type="text" class="validate"/>
                            <label for="first_name"> Email</label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.bonusPoints} id="first_name" type="text" class="validate"/>
                            <label for="first_name"> Bonus Points</label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.wallet} id="first_name" type="text" class="validate"/>
                            <label for="first_name"> Wallet</label>
                          </div> 
                        </div>
                        <div className="right-align">
                          <button className="waves-effect waves-light btn modal-trigger" 
                                  type="submit" 
                                  name="action" 
                                  id="sumbit-changes" 
                                  onClick={this.submitChanges}> Save Changes 
                          </button>
                        </div>
                        
                      </div>
                    </div>
                  </div>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col s3">
              <div className="card small blue-grey darken-1 hoverable" id="energy_per_km">
                <div className="card-content white-text">
                <span className="card-title">Calculate Energy Cost per km.</span>
                  <div class="input-field ">
                    <input placeholder="42 km" id="first_name" type="text" class="validate"/>
                    <label for="first_name"> Total distance traveled in chosen duration </label>
                  </div> 
                  <p>This is supposed to be  retrieved from the car</p>
                </div>
                <div className="card-action">      
                  <input type="button" className="datepicker" value="From" id="user-datepicker-from" />
                  <input type="button" className="datepicker" value="To" id="user-datepicker-to" />
                  <div class="input-field right align">
                    <input placeholder="0.42" id="first_name" type="text" class="validate"/>
                    <label for="first_name"> Calculated Cost </label>
                  </div> 
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default EditUserInfo;