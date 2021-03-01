import React, { useState } from 'react';
import UserInfo from './UserInfo';
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import { getStations } from './API';
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
  { position: [37.983810, 23.727539], label: "Athens", time: "10 minutes", condition: "Available" },
  { position: [40.629269, 22.947412], label: "Thessaloniki", time: "5 minutes", condition: "Maintenance" }
];

function LocationMarker(props) {
  const [position, setPosition] = useState(null)
  const map = useMapEvents({
    click() {
      map.locate();
    },

    locationfound(e) {
      setPosition(e.latlng);
      props.setter(e.latlng);
      map.flyTo(e.latlng, map.getZoom());
    },
  })

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
      stations: stationsHardcoded,
      currentPos: null,
      chosenIndex: null,
      zoom: 10,
      error: null,
    };
    this.setState({
      username: ""  
    })
    this.showMarkers = this.showMarkers.bind(this);
    this.handleLocation = this.handleLocation.bind(this);
    this.showOptions = this.showOptions.bind(this);
    // this.handleLocation = this.handleLocation.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
    this.handleUserInput = this.handleUserInput.bind(this);
  }

  handleUserInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }

  showMarkers() {
    var i = this.state.chosenIndex;

    if (i === null) {
      return (this.state.stations.map(({ position, label }, index) => <Marker key={index} value={label} position={position}></Marker>));
    }

    else {
      var station = this.state.stations[i];
      return (<Marker key={i} value={station.label} position={station.position}></Marker>);
    }
  }

  showOptions() {
    return (
      this.state.stations.map(({ position, label }, index) => <option value={index}> {label} </option>)
    );
  }

  handleSelect(e) {
    e.target.value === "all" ? this.setState({ chosenIndex: null }) : this.setState({ chosenIndex: e.target.value });
  }

  handleLocation(latlng) {
    this.setState({
      currentPos: latlng
    } 
    );
}

  componentDidMount() {
    M.AutoInit();
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
                  <input placeholder="Address" id="station_address" type="text" class="validate"/>
                  <label for="first_name">  </label>
                </div> 
                <div class="input-field col s12 right align">
                  <input placeholder="Charging Points" id="station_charging_points" type="text" class="validate"/>
                  <label for="first_name">  </label>
                </div> 
              </div>
              <div className="right-align">
                <button className="waves-effect waves-light btn modal-trigger" type="submit" name="action" id="sumbit-changes" onClick={this.submitChanges}>Save Changes 
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