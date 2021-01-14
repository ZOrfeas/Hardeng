import React from 'react';
import UserInfo from './UserInfo';
import { MapContainer, TileLayer, Marker } from 'react-leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css';
import "./ChargingExperience.css"

import icon from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl: icon,
  iconRetinaUrl: iconRetina,
  shadowUrl: iconShadow,
  iconSize: [24, 36],
  iconAnchor: [12, 36]
})
L.Marker.prototype.options.icon = DefaultIcon;

const centerPosition = [37.983810, 23.727539];

const stationsHardcoded = [
  { position: [37.983810, 23.727539], label: "Athens", time: "10 minutes", condition: "Available" },
  { position: [40.629269, 22.947412], label: "Thessaloniki", time: "5 minutes", condition: "Maintenance" }
];

class ChargingExperience extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      stations: stationsHardcoded,
      chosenPos: null,
      chosenIndex: null,
      zoom: 10,
    };

    this.handleSelect = this.handleSelect.bind(this);
    this.showMarkers = this.showMarkers.bind(this);
    this.showOptions = this.showOptions.bind(this);
    this.handleClick = this.handleClick(this);
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

  handleClick(e) {
    this.setState({ chosenPos: e.latlng });
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
              onClick={this.handleClick}
              className="map"
              center={centerPosition}
              zoom={this.state.zoom}
            >
              <TileLayer
                attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              
              {this.showMarkers()}
              {this.state.chosenPos !== null && (
                <Marker position={this.state.chosenPos}/>
              )}
            </MapContainer>
            <form>
              <select className="browser-default" onChange={this.handleSelect}>
                <option value="" disabled selected>Choose station ...</option>
                <option value="all">Show all stations</option>
                {this.showOptions()}
              </select>
            </form>
          </div>
          {this.state.chosenIndex !== null && (
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
    )
  }
}

export default ChargingExperience