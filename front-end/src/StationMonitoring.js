import React from 'react';
import "./StationMonitoring.css";
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import L from 'leaflet';
import 'leaflet/dist/leaflet.css'

class StationMonitoring extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      user: props.user
    };
  }
  Return(){
    let viewUsers =  document.getElementById('viewUsers');
    let btns =  document.getElementById('btn-group');
    btns.style.display = 'block';
    viewUsers.style.display = 'none';
  }
  
  searchForUsers(){
    let btns =  document.getElementById('btn-group');
    let viewUsers =  document.getElementById('viewUsers');
    btns.style.display = 'none';
    viewUsers.style.display = 'block';
  }

  myFunction() {
    // Declare variables
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById('myInput');
    filter = input.value.toUpperCase();
    ul = document.getElementById("names");
    li = ul.getElementsByTagName('li');
  
    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < li.length; i++) {
      a = li[i].getElementsByTagName("a")[0];
      txtValue = a.textContent || a.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        li[i].style.display = "";
      } else {
        li[i].style.display = "none";
      }
    }
  }

  searchForStations(){
    let rbtn = document.getElementById('rbtn');
    let btns =  document.getElementById('btn-group');
    let overlay = document.getElementById('overlay');
    let btn = document.getElementById('rtn-btn-stations');
    overlay.style.display = 'block';
    btn.style.display = 'block';
    btns.style.display = 'none';
  }
  ReturnFromViewStations(){
    let overlay = document.getElementById('overlay');
    let btns =  document.getElementById('btn-group');
    overlay.style.display = 'none'
    btns.style.display = 'block';
  }
  // showInfo(Event){
  //   let input = document.getElementById("myInput");
  //   let e = event.which || event.keyCode;
  //   if (e == 13){
  //     alert("Enter was pressed was presses");
  //   }
  // }
  

  
  render(){
    return(
      
      <div className="a">
        <div className="overlay" id="overlay">
          <div className="stations-map">
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
          </div>
          <form action="">
            <input type="button" className="returnbtn" value="Return" id="rtn-btn-stations" onClick={this.ReturnFromViewStations}/>
          </form>
        </div>
        <div className="viewUsers" id="viewUsers" display="none">
          <form action="">
            <input type="button" className="returnbtn" value="Return" id="rtn-btn" onClick={this.Return}/>
            <input type="text" id="myInput" onKeyUp={this.myFunction} placeholder="Search for names.."/* onKeyPress={this.showInfo(Event)}*//>
            <ul id="names">
              <li><a href="#">Adele #3118</a></li>
              <li><a href="#">Agnes</a></li>
              <li><a href="#">Billy</a></li>
              <li><a href="#">Bob</a></li>
              <li><a href="#">Calvin</a></li>
              <li><a href="#">Christina</a></li>
              <li><a href="#">Cindy</a></li>
            </ul> 
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