import React, { useState } from 'react';
import UserInfo from './UserInfo';
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import { getStations } from './API';
import 'leaflet/dist/leaflet.css';
import "./ChargingExperience.css";

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
const pricesHardcoded = [10, 20, 30, 40, 50, 60];
const vehiclesHardcoded = [
  {label:"Honda Civic", id:1},
  {label:"Renault Scenic", id:2}
];
const stationsHardcoded = [
  { position: [37.983810, 23.727539], label: "Athens", id: 1 },
  { position: [40.629269, 22.947412], label: "Thessaloniki", id: 2 }
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

class ChargingExperience extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      stations: stationsHardcoded,
      prices: pricesHardcoded,
      vehicles: vehiclesHardcoded,
      
      currentPos: null,
      
      chosenIndex: null,
      stationID: null,
      vehicleID: null,
      payment: null,
      
      zoom: 10,
      error: null,
    };

    this.handleSelect = this.handleSelect.bind(this);
    this.showMarkers = this.showMarkers.bind(this);
    this.showOptions = this.showOptions.bind(this);
    this.handleLocation = this.handleLocation.bind(this);
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
      this.state.stations.map(({ label, id}, index) => <option value={index}> {label} </option>)
    );
  }

  handleSelect(e) {
    if(e.target.name === "stationID"){
      if(e.target.value === "all") {
        this.setState({ chosenIndex: null, stationID: null });
      }
      else {
        this.setState({ chosenIndex: e.target.value });
        const s = this.state.stations;
        const c = e.target.value;

        this.setState({ stationID: s[c]['id'] });
        
      }
    }

    else {
      this.setState({[e.target.name]: e.target.value});
    }
  }

  handleLocation(latlng) {
    this.setState({
      currentPos: latlng
    }, () => {
      getStations(latlng)
        .then(res => { this.setState({ stations: res.data }) })
        .catch(err => { 
          if(err.response){
            this.setState({ error: err.response.status });
            M.toast({html: 'Error ' + this.state.error, classes:"purple darken-4 yellow-text"});
          }
        })
    });
  }
  componentDidMount() {
    M.AutoInit();
  }

  render() {
    return (
      <div className="row">
        <div className="col s2">
          <UserInfo />
        </div>
        <div className="col s10">
          <div className="card">
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
              <div className="">
                <select name="stationID" className="browser-default" onChange={this.handleSelect}>
                  <option value="" disabled selected>Choose station ...</option>
                  <option value="all">Show all stations</option>
                  {this.state.stations.map(({label}, index) => <option value={index}> {label} </option>)}
                </select>
                <select name="payment" className="browser-default" onChange={this.handleSelect}>
                  <option value="" disabled selected>Payment amount</option>
                  {this.state.prices.map(price => <option value={price}> {price} </option>)}
                </select>

                <select name="vehicleID" className="browser-default" onChange={this.handleSelect}>
                  <option value="" disabled selected>Choose car</option>
                  {this.state.vehicles.map(({label, id}) => <option value={id}> {label} </option>)}
                </select>

                <div className="center-align yellow">
                <button
                  className="btn-flat waves-effect waves-light purple-text text-darken-4"
                  type="submit"
                  >
                  Initiate Charging Session
                </button>
                </div>
              </div>
            </form>
          
            {this.state.chosenIndex !== null && false && (
              <table className="centered white">
              <thead>
                <tr>
                  <th>Location</th>
                  <th>Condition</th>
                  <th>Required Time</th>
                </tr>
              </thead>

              <tbody>
                <tr>
                  <td>{this.state.stations[this.state.chosenIndex].label}</td>
                  <td>{this.state.stations[this.state.chosenIndex].condition}</td>
                  <td>{this.state.stations[this.state.chosenIndex].time}</td>
                </tr>
              </tbody>
            </table>
            )
            }
          </div>
        </div>

        <div className="fixed-action-btn">
          <button className="btn-floating tooltipped red pulse" data-position="left" data-tooltip="Click map to give location">
            !
          </button>
        </div>
      </div>
    )
  }
}

export default ChargingExperience