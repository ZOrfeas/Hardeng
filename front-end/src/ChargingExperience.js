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

class ChargingExperience extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      session = null,
      stations: stationsHardcoded,
      currentPos: null,
      chosenIndex: null,
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
      this.state.stations.map(({ position, label }, index) => <option value={index}> {label} </option>)
    );
  }

  handleSelect(e) {
    e.target.value === "all" ? this.setState({ chosenIndex: null }) : this.setState({ chosenIndex: e.target.value });
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
              <select className="browser-default" onChange={this.handleSelect}>
                <option value="" disabled selected>Choose station ...</option>
                <option value="all">Show all stations</option>
                {this.showOptions()}
              </select>
            </form>
          
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