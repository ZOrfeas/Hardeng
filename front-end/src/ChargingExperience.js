import React from 'react';
import 'leaflet/dist/leaflet.css';
import { MapContainer, TileLayer, Marker, Popup} from 'react-leaflet'
import L from 'leaflet'
import "./ChargingExperience.css"

//delete L.Icon.Default.prototype._getIconUrl;
//L.Icon.Default.mergeOptions({
//    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
//    iconUrl: require('leaflet/dist/images/marker-icon.png'),
//    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
//});

import icon from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl: icon,
  iconRetinaUrl: iconRetina,
  shadowUrl: iconShadow,
  iconSize: [24,36],
  iconAnchor: [12,36]
})
L.Marker.prototype.options.icon = DefaultIcon;

const centerPosition = [37.983810, 23.727539];

export class ChargingExperience extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      stations: this.props.stations,
      zoom: 10
    };
  }

  render(){
    return(
      <div className="container">
        <MapContainer 
          className="map" 
          center={centerPosition} 
          zoom={this.state.zoom} 
        >
          <TileLayer
            attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          {this.state.stations.map(({position, label}) => <Marker key={label} position={position}></Marker>)}
          
        </MapContainer>

        <form>
          <select className="browser-default">
            <option value="" disabled selected>Choose station ...</option>
            {this.state.stations.map(({ position, label }) => <option value={position}> {label} </option>)}
          </select>
        </form>
      </div>
    )
  }
}

export default ChargingExperience