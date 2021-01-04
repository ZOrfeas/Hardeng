import React, { Component } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'


export class MapLayer extends Component {
  constructor(props){
    super(props);
    this.state = {
      lat: 37.983810,
      lng: 23.727539,
      zoom: 13
    };
  }

  render() {
    return (
      <div>
      <MapContainer 
        center={[this.state.lat, this.state.lng]} 
        zoom={this.state.zoom} 
        style={{ width: '100%', height: '500px'}}
      >
        <TileLayer
          attribution='&copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
      </MapContainer>
      </div>
    )
  }
}

export default MapLayer



