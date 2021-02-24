import React, { useState } from 'react';
import "./StationMonitoring.css";
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'
import M, { Autocomplete } from 'materialize-css';
import { AiOutlineSend } from "react-icons/ai";
import { getStations } from './API';
import Map from './Map.js';
import image from './icons/image3.jpg'

const driversHardcoded = [
  {driver_name: "Kostas", id: 11111, bonus_points: 10, carID: 13,  email: "kostas@kostas.gr", walletID: 12315464758},
  {driver_name: "Kostakis", id: 22222, bonus_points: 12, carID: 5,  email: "kostakis@kostakis.gr", walletID: 9999999999 },
  {driver_name: "Kost", id: 22222, bonus_points: 12, carID: 5,  email: "kostakis@kostakis.gr", walletID: 9999999999 }
];



class StationMonitoring extends React.Component {
  constructor(props) {
    super(props);
    this.setState({
      username: ""
    });
    this.handleUserInput = this.handleUserInput.bind(this);
    this.ReturnFromViewStations = this.ReturnFromViewStations.bind(this);
    this.searchForStations = this.searchForStations.bind(this);
    this.state = {
      user: props.user,
      chosenIndex: null,
    };
  }
  componentDidMount(){
    document.addEventListener('DOMContentLoaded', function() {
      var options = {
         data: {},
        onAutocomplete:function(res){
          var temp = res.slice(0,res.length - 7);
          document.getElementById('drivers-name').value=temp;
          // document.getElementById('drivers-email').value=email;
          // document.getElementById('drivers-bonus-points').value=bonus points;
          // document.getElementById('drivers-wallet').value=wallet;
          
        }    
      };
    
      document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.modal');
        var instances = M.Modal.init(elems, options);
      });
    
      var elems = document.querySelectorAll('.autocomplete');
      var instances = M.Autocomplete.init(elems, options);
      var sela = {};
      for (const i in driversHardcoded){
        
        var name = driversHardcoded[i]["driver_name"] + " #" + driversHardcoded[i]["id"];
        sela[name] = 'https://placehold.it/250x250';
      }
      instances[0].updateData(sela);
    });
  }
  ReturnFromViewUsers(){
    let viewUsers =  document.getElementById('viewUsers');
    let btns =  document.getElementById('btn-group');
    btns.style.display = 'block';
    viewUsers.style.display = 'none';
  }
  
  searchForUsers(){
    console.log("Hello world!"); 
    let btns =  document.getElementById('btn-group');
    let rtnBtns =  document.getElementById('rtn-btn-users');
    let viewUsers =  document.getElementById('viewUsers');
    btns.style.display = 'none';
    viewUsers.style.display = 'block';
    rtnBtns.style.display = 'block';
  }

  searchForStations(){
    let btns =  document.getElementById('btn-group');
    this.setState({chosenIndex: true});
    btns.style.display = 'none';    
  }

  ReturnFromViewStations(){
    let btns =  document.getElementById('btn-group');
    btns.style.display = 'block';
    this.setState({chosenIndex: null});
  }

  handleUserInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }

  submitChanges(){
    if (window.confirm("U sur?????")) {
        //get driver with id and change info
    } 
    else {
        //do nothing
    }
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
          {this.state.chosenIndex !== null && (
            <div className="stationsMap" id="stationsMap">
              <Map/>
              
              <form action="">
                <input type="button" className="stationbutton" value="Return" id="rtn-btn-stations" onClick={this.ReturnFromViewStations} />
              </form>      
            </div>
          )
          }
          <div className="viewUsers" id="viewUsers" display="none">
            <div className="row">
              <div className="col s12">
                <div className="row">
                  <div className="input-field col s12">
                    <i className="material-icons prefix"></i>
                    <input type="text" id="autocomplete-input" className="autocomplete"/>
                    <label for="autocomplete-input">Autocomplete</label>
                  </div>
                </div>
                  <div className="row">
                    <div className="col s2" id='res'>
                      <div className="card blue-grey darken-1 hoverable">
                        <div className="card-content white-text">
                          <span className="card-title">Driver's Info.</span>
                        </div>
                        <div className="card-action">
                          <p>Name</p> 
                          <input 
                          type="username" 
                          placeholder="Drivers Name"
                          id="drivers-name" 
                          className="Drivers Name" 
                          value={this.state.username}
                          onChange={this.handleUserInput}
                          />
                          <p>Email</p> 
                          <input 
                          type="username" 
                          placeholder="Email"
                          id="email" 
                          className="email" 
                          value={this.state.username}
                          onChange={this.handleUserInput}
                          />
                          <p>Bonus Points</p> 
                          <input 
                          type="username" 
                          placeholder="Bonus Points"
                          id="bonus-points" 
                          className="bonusPoints" 
                          value={this.state.username}
                          onChange={this.handleUserInput}
                          />
                          <p>Wallet</p> 
                          <input 
                          type="username" 
                          placeholder="WalletID"
                          id="wallet-id"
                          className="walletID" 
                          value={this.state.username}
                          onChange={this.handleUserInput}
                          />
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
            <form action="">
              <input type="button" 
              className="returnbtn" 
              value="Return" 
              id="rtn-btn-users" 
              onClick={this.ReturnFromViewUsers}/>
            </form>
          </div>
          
          <form action="">
            <div className="btn-group" id="btn-group" display="block">
              <input type="button" className="leftButton" value="View Users" id="lbtn" onClick={this.searchForUsers}/>
              <input type="button" className="rightButton" value="View Stations" id="rbtn" onClick={this.searchForStations}/>
            </div>
          </form>
        </div>
      </div>
    );
  }
}

export default StationMonitoring;