import React from 'react';
import "./StationMonitoring.css";
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'
import M, { Autocomplete } from 'materialize-css';
import { AiOutlineSend } from "react-icons/ai";

const driversHardcoded = [
  {driver_name: "Kostas", id: 11111, bonus_points: 10, carID: 13,  email: "kostas@kostas.gr", walletID: 12315464758},
  {driver_name: "Kostakis", id: 22222, bonus_points: 12, carID: 5,  email: "kostakis@kostakis.gr", walletID: 9999999999 }
];

document.addEventListener('DOMContentLoaded', function() {
  var options = {
     data: {},
    onAutocomplete:function(res){
      document.getElementById('drivers-name').value=res;
      // document.getElementById('drivers-name').value=res;
      // document.getElementById('drivers-name').value=res;
      // document.getElementById('drivers-name').value=res;
      
    }    
  };
  var elems = document.querySelectorAll('.autocomplete');
  var instances = M.Autocomplete.init(elems, options);
  var sela = {};
  for (const i in driversHardcoded){
    var name = driversHardcoded[i]["driver_name"] + " #" + driversHardcoded[i]["id"];
    sela[name] = 'https://placehold.it/250x250';
  }
  instances[0].updateData(sela);
});

class StationMonitoring extends React.Component {
  constructor(props) {
    super(props);
    this.setState({
      username: "",
      drivers: driversHardcoded
    });
    this.handleUserInput = this.handleUserInput.bind(this);
    this.state = {
      user: props.user
    };
  }
  ReturnFromViewUsers(){
    let viewUsers =  document.getElementById('viewUsers');
    let btns =  document.getElementById('btn-group');
    btns.style.display = 'block';
    viewUsers.style.display = 'none';
  }
  
  searchForUsers(){
    // getDriver();
    let btns =  document.getElementById('btn-group');
    let rtnBtns =  document.getElementById('rtn-btn-users');
    let viewUsers =  document.getElementById('viewUsers');
    btns.style.display = 'none';
    viewUsers.style.display = 'block';
    rtnBtns.style.display = 'block';
  }

  searchForStations(){
    // let rbtn = document.getElementById('rbtn');
    let btns =  document.getElementById('btn-group');
    let stationsMap = document.getElementById('stationsMap');
    let btn = document.getElementById('rtn-btn-stations');
    stationsMap.style.display = 'block';
    btn.style.display = 'block';
    btns.style.display = 'none';
  }
  ReturnFromViewStations(){
    let stationsMap = document.getElementById('stationsMap');
    let btns =  document.getElementById('btn-group');
    let rtnBtns =  document.getElementById('rtn-btn-stations');
    rtnBtns.style.display = 'none';
    stationsMap.style.display = 'none';
    btns.style.display = 'block';
  }
  handleUserInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }
  

  render(){
    return(
      
      <div className="a">
        <div className="stationsMap" id="stationsMap">
          <MapContainer center={[51.505, -0.09]} zoom={13} scrollWheelZoom={true} className="station-map">
            <TileLayer
              attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={[51.505, -0.09]}>
              <Popup>
                data bla bla <br /> data.
              </Popup>
            </Marker>
          </MapContainer>
          <form action="">
            <input type="button" className="returnbtn" value="Return" id="rtn-btn-stations" onClick={this.ReturnFromViewStations}/>
          </form>
        
        </div>
        
        
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
              {/* <div className="card small"> */}
              
                <div className="row">
                  <div className="col s12 m6" id='res'>
                    <div className="card blue-grey darken-1">
                      <div className="card-content white-text">
                        <span className="card-title">Driver's Info.</span>
                        
                        {/* <p>User's Name.</p> */}
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
                        <button className="btn waves-effect waves-light" type="submit" name="action">Submit Changes  <AiOutlineSend/>
                        </button>
                      </div>
        
                    </div>
                  </div>
                    {/* <div className="col s12 m6" id='res'>
                      <div className="card blue-grey darken-1">
                        <div className="card-content white-text">
                          <span className="card-title">User's Name.</span>

                        </div>
                        <div className="card-action">
                          <input 
                          type="username" 
                          placeholder="Name"
                          id="example-text" 
                          className="example text" 
                          value={this.state.username}
                          onChange={this.handleUserInput}
                          />
                        </div>
                      </div>
                    </div> */}
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
    );
  }
}

export default StationMonitoring;