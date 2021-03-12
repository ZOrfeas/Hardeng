import React, { useState } from 'react';
import UserInfo from './UserInfo';
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import { getAdminStations,updateAdminStation } from './API';
import 'leaflet/dist/leaflet.css';
import "./Map.css";

import icon from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import person from './icons/black-marker.png';


var locationIcon = L.icon({
  iconUrl: person,
  shadowUrl: iconShadow,
  iconSize: [36, 36],
  iconAnchor: [18, 36]
});

let DefaultIcon = L.icon({
  iconUrl: icon,
  iconRetinaUrl: iconRetina,
  shadowUrl: iconShadow,
  iconSize: [24, 36],
  iconAnchor: [12, 36]
});
L.Marker.prototype.options.icon = DefaultIcon;

const Athens = [37.983810, 23.727539];

const stationsHardcoded = [
  { Longitude:37.983810, Latitude: 23.727539, AddressLine: "Athens", time: "10 minutes", condition: "Available" },
  {  Longitude:40.629269, Latitude: 22.947412, AddressLine: "Thessaloniki", time: "5 minutes", condition: "Maintenance" }
];

function LocationMarker(props) {
  const [position, setPosition] = useState(null)
  // const map = useMapEvents({
  //   click() {
  //     map.locate();
  //   },

  //   // locationfound(e) {
  //   //   setPosition(e.latlng);
  //   //   props.setter(e.latlng);
  //   //   map.flyTo(e.latlng, map.getZoom());
  //   // },
  // })

  return position === null ? null : (
    <Marker position={position} icon={locationIcon}>
      <Popup>You are here</Popup>
    </Marker>
  )
}

class Map extends React.Component{
  constructor(props){
    super(props);

    this.state = {
      adminKey: localStorage.getItem("adminKey"),
      adminID: localStorage.getItem("adminID"),
      stationID: null,
      stations: stationsHardcoded,
      currentPos: null,
      chosenIndex: null,
      zoom: 3,
      error: null,
      btnIndex: true,
      address: null,
      chargingPoints: null,
      providerID: null,
      lat: null,
      lot: null,
    };
    this.setState({
      username: ""  
    })
    this.submitChanges = this.submitChanges.bind(this);
    this.showMarkers = this.showMarkers.bind(this);
    this.handleLocation = this.handleLocation.bind(this);
    this.showOptions = this.showOptions.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
    this.handleUserInput = this.handleUserInput.bind(this);
    this.enableButton = this.enableButton.bind(this);
    this.handleUserInputAddress = this.handleUserInputAddress.bind(this);
    this.handleUserInputProviderID = this.handleUserInputProviderID.bind(this);
  }

  handleUserInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }

  showMarkers() {
    var i = this.state.chosenIndex;

    if (i === null) {
      return (this.state.stations.map(({ Longitude, Latitude, AddressLine }, index) => <Marker key={index} value={AddressLine} position={[Latitude,Longitude]}>
        <Popup>
        {AddressLine}.<br /> 
        </Popup>
      </Marker>));
    }
    else {
      var station = this.state.stations[i];
      return (<Marker key={i} value={station.AddressLine} position={[station.Latitude,station.Longitude]}>
        <Popup>
        {station.AddressLine}.<br /> 
        </Popup>
      </Marker>);
    }
  }

  showOptions() {
    return (
      this.state.stations.map(({ Longitude, Latitude, AddressLine }, index) => <option value={index}> {AddressLine} </option>)
    );
  }

  handleSelect(e) {
    e.target.value === "all" ? this.setState({ chosenIndex: null }) : this.setState({ chosenIndex: e.target.value });
    console.log(e.target.value)
    console.log(this.state.stations[e.target.value]["AddressLine"])
    console.log(this.state.stations[e.target.value]["TotalChargingPoints"])
    this.setState({address: this.state.stations[e.target.value]["AddressLine"]})
    this.setState({chargingPoints: this.state.stations[e.target.value]["TotalChargingPoints"]})
    this.setState({providerID: this.state.stations[e.target.value]["EnergyProviderID"]})
    this.setState({lot: this.state.stations[e.target.value]["Longitude"]})
    this.setState({lat: this.state.stations[e.target.value]["Latitude"]})
    this.setState({stationID: this.state.stations[e.target.value]["StationID"]})
  }

  handleLocation(latlng) {
    this.setState({
      currentPos: latlng
    } 
    );
}

  componentDidMount() {
    document.addEventListener('DOMContentLoaded', function() {
      var elems = document.querySelectorAll('.dropdown-trigger');
      var instances = M.Dropdown.init(elems, {});
    });

    // M.AutoInit();

    getAdminStations(this.state.adminKey,this.state.adminID)
    .then(res => {
      console.log(res)
      this.setState({stations: res.data})
    })
    .catch(() => {
  
    })

  }

  submitChanges(){
    if (window.confirm("U sur?????")) {
      const obj = {
      "lat" : this.state.lat,
      "lon" : this.state.lot,
      "address" : this.state.address,
      "adminId" : this.state.adminID,
      "providerId" : this.state.providerID,
      }
      updateAdminStation(this.state.adminKey,this.state.stationID,obj)
      .then(res => {
        console.log(res)
        window.location.reload();
      })
      .catch(error => {
        console.log(error.response)
      })
    } 
    else {
        //do nothing
    }
  }

  enableButton(){
    this.setState({btnIndex: false});
  }

  handleUserInputProviderID (e){
    this.setState({btnIndex: false});
    this.setState({providerID: e.target.value})
  }

  handleUserInputAddress (e){
    this.setState({btnIndex: false});
    this.setState({address: e.target.value})
  }

  render(){
    return(
      <div>
        <div className="row">
          <div className="col s9 right align">
            <div className="card hoverable">
                <MapContainer
                  className="map"
                  center={Athens}
                  zoom={this.state.zoom}
                >
                  <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  />
                  {this.showMarkers()}
                  <LocationMarker setter={this.handleLocation} />
                </MapContainer>
                <form>
                    <select className="browser-default" onChange={this.handleSelect}>
                      <option value="" disabled selected>Choose station ...</option>
                      <option value="all">Show all stations</option>
                      {this.showOptions()}
                    </select>
                  </form>
            </div>
          </div>
          <div className="col s3 m3">
            <div className="card blue-grey darken-1" >
              <div className="card-content white-text">
                <span className="card-title">Charging Station's Info</span>
              </div>
              <div className="card-action">
                <div class="input-field col s12 right align">
                  <input placeholder="Address" id="station_address" value ={this.state.address} type="text" class="validate" onChange={this.handleUserInputAddress}/>
                  <label for="station_address">  </label>
                </div> 
                <div class="input-field col s12 right align">
                  <input placeholder="Charging Points" id="station_charging_points" value={this.state.chargingPoints} type="text" class="validate"/>
                  <label for="station_charging_points">  </label>
                </div> 
                <div class="input-field col s12 right align">
                  <input placeholder="Energy Provider ID" id="provider_id" value={this.state.providerID} type="text" class="validate" onChange={this.handleUserInputProviderID}/>
                  <label for="provider_id"> </label>
                </div> 
              </div>
              <div className="right-align">
                <button className="waves-effect waves-light btn modal-trigger" type="submit" name="action" id="sumbit-changes" onClick={this.submitChanges} disabled={this.state.btnIndex}>Save Changes 
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }

  
}

export default Map