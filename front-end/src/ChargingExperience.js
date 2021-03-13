import React, { useState } from 'react';
import UserInfo from './UserInfo';
import Payment from './Payment';
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet'
import L from 'leaflet'
import { getStations, postInitiateSession, getDriverCars, getDriverPolicies, logSession } from './API';
import { FaCarBattery, FaCartPlus } from "react-icons/fa";
import 'leaflet/dist/leaflet.css';
import './ChargingExperience.css';

import image from './icons/ChargingExperience.jpg';
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
  {BrandName:"Honda Civic", Model: "e-soul", CarID:1},
  {BrandName:"Renault Scenic", Model:"e-soul", CarID:2}
];
const stationsHardcoded = [
  { position: [37.983810, 23.727539], label: "Athens", station_id: 1 },
  { position: [40.629269, 22.947412], label: "Thessaloniki", station_id: 2 }
];
const policiesHardcoded = [
  {PricePolicyID: 1, KWh: 5, CostPerKWh: 200},
  {PricePolicyID: 2, KWh: 6, CostPerKWh: 300}
]

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
      driverKey: localStorage.getItem("driverKey"),
      driverID: localStorage.getItem("driverID"),

      stations: [],
      prices: pricesHardcoded,
      vehicles: vehiclesHardcoded,
      policies: policiesHardcoded,
      
      currentPos: null,
      
      sessionStarted: false,
      chosenIndex: null,
      stationID: null,
      vehicleID: null,
      stationPointID: null,
      pricePolicy: null,
      pricePolicyID: null,
      payment: null,
      
      session: {
        startedOn: null,
        finishedOn: null,
        energyDelivered: null,
        payment: null,
        chargingPointId: null,
        pricePolicyId: null,
        carId: null,
        driverId: null
      },
      
      zoom: 10,
      error: null,
      sessionError: null
    };

    this.getDate = this.getDate.bind(this);
    this.formatZero = this.formatZero.bind(this);
    this.initiateSession = this.initiateSession.bind(this);
    this.finishSession = this.finishSession.bind(this);
    this.setPayment = this.setPayment.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
    this.showMarkers = this.showMarkers.bind(this);
    this.showOptions = this.showOptions.bind(this);
    this.handleLocation = this.handleLocation.bind(this);
  }

  formatZero(xx) {
    if(xx < 10) {
      return '0' + xx;
    }
    else{
      return xx;
    }
  }
  getDate(){
    var time = new Date();
    
    var year = time.getFullYear();
    var month = this.formatZero(time.getMonth() + 1);
    var day = this.formatZero(time.getDate());
    var hour = this.formatZero(time.getHours());
    var minute = this.formatZero(time.getMinutes());
    var seconds = this.formatZero(time.getSeconds());

    var ans = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + seconds;

    return ans;
  }
  finishSession(e, payment_type) {
    e.preventDefault();

    var newSession = this.state.session;
    newSession.energyDelivered = this.state.payment / this.state.pricePolicy ;
    newSession.finishedOn = this.getDate();
    newSession.payment = payment_type;

    console.log(newSession);

    this.setState({
      session: newSession
    }, () => {
      logSession(this.state.driverKey, this.state.session)
        .then(res => {
          window.location.reload();
        })
        .catch(err => {
          if(err.response){
            M.toast({html: 'LogSession Error ' + err.response.status, classes:"purple darken-4 yellow-text"});
          }
          else{
            M.toast({html: 'LogSession Error undefined', classes:"purple darken-4 yellow-text"});
          }
        });
    });
  }
  initiateSession(e){
    e.preventDefault();

    const station = this.state.stationID;
    const vehicle = this.state.vehicleID;
    const payment = this.state.payment;
    const policy = this.state.pricePolicy;
    var newError = null;

    if(station === null || vehicle === null || payment === null || policy === null) {
      newError = "All fields required";
    }
    else if(this.state.driverKey === null) {
      newError = "Not logged in";
    }
    else {
      postInitiateSession(this.state.driverKey, station)
        .then(res => {
          console.log(res);

          var session = this.state.session;
          session.startedOn = this.getDate();
          session.chargingPointId = res.data;
          session.carId = this.state.vehicleID;
          //session.payment = this.state.payment;
          session.pricePolicyId = this.state.pricePolicyID;
          session.driverId = this.state.driverID;
    
          this.setState({
            sessionError: null,
            sessionStarted: true,
            session: session,
          });
        })
        .catch(err => {
          if(err.response) {
            newError = "Error " + err.response.status;
          }
          else{
            console.log(err);
          }
        })
    }

    if(newError !== null){
      this.setState({sessionError: newError});
      M.toast({html: 'InitiateSession ' + newError, classes:"purple darken-4 yellow-text"});
    }
  }
  setPayment(p){
    var newSession = this.state.session;

    newSession.payment = p;

    this.setState({session: newSession});
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

        this.setState({ stationID: s[c].station_id });
        
      }
    }
    else if(e.target.name === "pricePolicyID"){
      const index = e.target.value;
      this.setState({pricePolicyID: this.state.policies[index].PricePolicyID, pricePolicy: this.state.policies[index].CostPerKWh})
    }
    else {
      this.setState({[e.target.name]: e.target.value});
    }
  }
  handleLocation(latlng) {
    this.setState({
      currentPos: latlng
    }, () => {
      getStations(this.state.driverKey, latlng)
        .then(res => { 
          console.log(res.data);
          this.setState({ stations: res.data }) })
        .catch(err => { 
          if(err.response){
            this.setState({ error: err.response.status });
            M.toast({html: 'GetStations Error ' + this.state.error, classes:"purple darken-4 yellow-text"});
          }
        })
    });
  }
  componentDidMount() {
    M.AutoInit();

    // Fetch user's cars and price policies
    if(this.state.driverKey !== null){
      getDriverCars(this.state.driverKey, this.state.driverID)
        .then(res => {this.setState({vehicles: res.data["Cars"]})})
        .catch(err => {
          if(err.response){
            M.toast({html: 'GetCars Error ' + err.response.status, classes:"purple darken-4 yellow-text"});
          }
          else{
            M.toast({html: 'GetCars Error undefined', classes:"purple darken-4 yellow-text"});
          }
        })
      
      getDriverPolicies(this.state.driverKey, this.state.driverID)
        .then(res => {this.setState({policies: res.data["PricePolicies"]})})
        .catch(err => {
          if(err.response){
            M.toast({html: 'GetPolicies Error ' + err.response.status, classes:"purple darken-4 yellow-text"});
          }
          else{
            M.toast({html: 'GetPolicies Error undefined', classes:"purple darken-4 yellow-text"});
          }
        })
    }
  }

  render() {
    return (
      <div 
      className="row"
      style={{ 
        backgroundImage: `url(${image})`,
        backgroundRepeat: 'no-repeat',
        height: '1000px',
        backgroundAttachment: 'fixed',
        backgroundSize: '100% 100%',}}
      >
        {false && <button onClick={e => {console.log(this.state)}}> State </button>}
        <div className="col s2">
          <UserInfo />
        </div>
        <div className="col s10">
          <div className="card">
            <MapContainer
              className='map'
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

            <form onSubmit={this.initiateSession}>
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
                  {this.state.vehicles.map(({CarID, BrandName, Model}) => <option value={CarID}> {BrandName + ' ' + Model} </option>)}
                </select>
                <select name="pricePolicyID" className="browser-default" onChange={this.handleSelect}>
                  <option value="" disabled selected>Choose policy</option>
                  {this.state.policies.map(({CostPerKWh}, index) => <option value={index}> {CostPerKWh + '€ per KWh'} </option>)}
                </select>
                <div className="center-align">
                  <button
                    className="btn-flat green-text"
                    type="submit"
                    >
                    Initiate Session <FaCarBattery/> 
                  </button>
                </div>
              </div>
            </form>

            <div className="center-align">
              <Payment 
                disabled={!this.state.sessionStarted} 
                payment={this.state.payment}
                doNext={(e, t) => this.finishSession(e, t)} 
              />
            </div>
            <div className="center-align">
              <button 
                blue-text
                disabled={!this.state.sessionStarted} 
                className="btn-flat"
                onClick={e => {this.finishSession(e, 'unpaid')}}
                >
                Charge Tab <FaCartPlus/>
              </button>
            </div>
            
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

        {false &&<div className="fixed-action-btn">
          <button className="btn-floating tooltipped red pulse" data-position="left" data-tooltip="Click map to give location">
            !
          </button>
        </div>}
      </div>
    )
  }
}

export default ChargingExperience