import React, { useState } from 'react';
import 'leaflet/dist/leaflet.css'
import image from './icons/image4.jpg'
import "./Map.css";
import L from 'leaflet'
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents, Circle} from 'react-leaflet'
import "./EnergyMonitoring.css"
import { ResponsiveLine} from '@nivo/line'
import { ResponsiveStream } from '@nivo/stream'
import bxb from './icons/bxxb.png'
import icon from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import person from './icons/black-marker.png';
import Map from './Map.js';
import { getAdminPolicies, updateAdminPolicies, getAdminAreaStationEnergy, getAdminTotalEnergy} from './API';

const data = [
  {
    "2019-10-01": Math.floor(Math.random() * 10), 
    "2020-02-01": 20, 
    "2020-06-01": 30, 
    "2020-10-01": 15, 
    "2021-02-01": 40,
  },
  {
    "2019-10-01": 5, 
    "2020-02-01": 10, 
    "2020-06-01": 25, 
    "2020-10-01": 35, 
    "2021-02-01": 10,
  },
  {
    "2019-10-01": 20, 
    "2020-02-01": 39, 
    "2020-06-01": 41, 
    "2020-10-01": 16, 
    "2021-02-01": 28,
  },
  {
    "2019-10-01": 39, 
    "2020-02-01": 19, 
    "2020-06-01": 28, 
    "2020-10-01": 9, 
    "2021-02-01": 13,
  },
  {
    "2019-10-01": 10, 
    "2020-02-01": 20, 
    "2020-06-01": 30, 
    "2020-10-01": 15, 
    "2021-02-01": 40,
  },
  {
    "2019-10-01": 10, 
    "2020-02-01": 20, 
    "2020-06-01": 30, 
    "2020-10-01": 15, 
    "2021-02-01": 40,
  },
]

const fillBlueOptions = { fillColor: 'blue' }
const Athens = [37.983810, 23.727539];


const driversHardcoded = [
  {driver_name: "Kostas", id: 11111, bonus_points: 10, carID: 13,  email: "kostas@kostas.gr", walletID: 12315464758},
  {driver_name: "Kostakis", id: 22222, bonus_points: 12, carID: 5,  email: "kostakis@kostakis.gr", walletID: 9999999999 },
  {driver_name: "Kost", id: 22222, bonus_points: 12, carID: 5,  email: "kostakis@kostakis.gr", walletID: 9999999999 }
];

const stationsHardcoded = [
  { position: [37.983810, 23.727539], label: "Athens", time: "10 minutes", condition: "Available" },
  { position: [40.629269, 22.947412], label: "Thessaloniki", time: "5 minutes", condition: "Maintenance" }
];

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
const style = {
  map: {
    height: '500px',
    width: '100%'
  }
}

var latlng2 = {};
function LocationMarker(props) {
  const [position, setPosition] = useState(null)
  const map = useMapEvents({
    click(e) {
      latlng2 = e.latlng;
      console.log(typeof e.latlng)
      setPosition(e.latlng);
      map.flyTo(e.latlng, map.getZoom());
      let val = document.getElementById('region-placeholder')
      val.value = e.latlng;
    },
  })

  return position === null ? null : (
    <Marker position={position} icon={locationIcon}>
      <Popup> 
        <div style={{ 
          backgroundImage: `url(${bxb})`,
          backgroundRepeat: 'no-repeat',
          height: '50px',
          backgroundAttachment: 'fixed',
          backgroundSize: '100% 100%',
          }}> 
        </div> 
      </Popup>
      <Circle center={position} pathOptions={fillBlueOptions} radius={100000} />
    </Marker>
    
  )
}

var listsela = [
  
];

class EnergyMonitoring extends React.Component{
  constructor(props){
    super(props);
    this.setState({
    });
  this.state = {
    fromIndex:null,
    toIndex:null,
    regionIndex:null,
    adminKey: localStorage.getItem("adminKey"),
    adminID: localStorage.getItem("adminID"),
    btnIndex: "true",
    user: props.user,
    chosenIndex: null,
    stations: null,
    PricePolicyID: null,
    costPerkWh: null,
    kWhAmount: null,
    sela: {},
    datastream: [],
    totalsum: null,
    PricePolicy: null,
    graphTotalEnergy: [
      {
        "id": "Energy Consumption",
        "color": "hsl(160, 70%, 50%)",
        "data": [
        ]
      }
    ]
  };

  this.ReturnFromViewStations = this.ReturnFromViewStations.bind(this);
  this.searchForStations = this.searchForStations.bind(this);
  this.enableButton = this.enableButton.bind(this);
  this.handleUserInputPricePolicyID = this.handleUserInputPricePolicyID.bind(this);
  this.handleUserInputCostPerkWh = this.handleUserInputCostPerkWh.bind(this);
  this.handleUserInputkWhAmount = this.handleUserInputkWhAmount.bind(this);
  this.componentDidMount = this.componentDidMount.bind(this);
  this.submitChanges = this.submitChanges.bind(this);
  this.CalcCostPerRegion=this.CalcCostPerRegion.bind(this);
  this.showGraphs = this.showGraphs.bind(this);
  this.setGraph = this.setGraph.bind(this);
  this.getEnergy = this.getEnergy.bind(this);
}

componentDidMount(){

  M.AutoInit();

  document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.datepicker');
    var instances = M.Datepicker.init(elems, {format: 'yyyy-mm-dd'});
  });

  window.onload = function() {
    this.showGraphs();
  }.bind(this);


  getAdminPolicies(this.state.adminKey,this.state.adminID)
  .then(res =>{
    this.setState({PricePolicy: res.data});
    var elems = document.querySelectorAll('.autocomplete');
    var instances = M.Autocomplete.init(elems, {
      onAutocomplete:function(res){
        var temp = res.slice(13);
        for (const i in this.state.PricePolicy){
          if (this.state.PricePolicy[i]["PricePolicyID"] == temp){
            this.setState({PricePolicyID: temp});
            this.setState({costPerkWh: this.state.PricePolicy[i]["CostPerKWh"]});
            this.setState({kWhAmount: this.state.PricePolicy[i]["KWh"]});

          }
        }
      }.bind(this)
    });
    var sela = {};
  
    for (const i in this.state.PricePolicy){
      
      var name = "PricePolicy #" + this.state.PricePolicy[i]["PricePolicyID"];   // Create elements of autocomplete
      sela[name] = 'https://placehold.it/250x250';
    }
    instances[0].updateData(sela);
  })
  .catch(error => {
    console.log(error.response)
  })
}

async getEnergy(){
  const kostas = await Promise.all([
    getAdminTotalEnergy(this.state.adminKey, this.state.adminID, "2019-06-01", "2019-10-01"),
    getAdminTotalEnergy(this.state.adminKey, this.state.adminID, "2019-10-01","2020-02-01"),
    getAdminTotalEnergy(this.state.adminKey, this.state.adminID, "2020-02-01","2020-06-01"),
    getAdminTotalEnergy(this.state.adminKey, this.state.adminID, "2020-06-01","2020-10-01"),
    getAdminTotalEnergy(this.state.adminKey, this.state.adminID, "2020-10-01","2021-02-01"),
  ]);

  listsela.push({"x": "2019-10-01","y":kostas[0].data/1000000});
  listsela.push({"x": "2020-2-01","y":kostas[1].data/1000000});
  listsela.push({"x": "2020-6-01","y":kostas[2].data/1000000});
  listsela.push({"x": "2020-10-01","y":kostas[3].data/1000000});
  listsela.push({"x": "2021-2-01","y":kostas[4].data/1000000});
  this.setState({totalsum: (kostas[0].data+kostas[1].data+kostas[2].data+kostas[3].data+kostas[4].data)/5000000})
  console.log(listsela,"kostas sela");
  this.setGraph();
}

showGraphs(){
  this.getEnergy();
  console.log(listsela);
  return null;
}

setGraph(){
  console.log(listsela,"sela2");
  this.setState({graphTotalEnergy: [
    {
      "id": "Consumption",
      "color": "hsl(160, 70%, 50%)",
      "data": listsela,
    },
  ]})
  let kostas = this.state.totalsum;
  this.setState({datastream: [
    {
      "2021-03-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-04-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
      "2021-05-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-06-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-07-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
    },
    {
      "2021-03-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-04-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
      "2021-05-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-06-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-07-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
    },
    {
      "2021-03-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-04-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
      "2021-05-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-06-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-07-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
    },
    {
      "2021-03-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-04-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
      "2021-05-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-06-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-07-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
    },
    {
      "2021-03-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-04-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
      "2021-05-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-06-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50), 
      "2021-07-01": Math.abs(kostas + Math.floor(Math.random() * 100)-50),
    },
  ]})
}

handleUserInputPricePolicyID (e){
  this.setState({btnIndex: false});
  this.setState({PricePolicyID: e.target.value})
}

handleUserInputCostPerkWh (e){
  this.setState({btnIndex: false});
  this.setState({costPerkWh: e.target.value})
}

handleUserInputkWhAmount (e){
  this.setState({btnIndex: false});
  this.setState({kWhAmount: e.target.value})
}

ReturnFromViewUsers(){
  let viewUsers =  document.getElementById('viewUsers');
  let btns =  document.getElementById('btn-group');
  btns.style.display = 'block';
  viewUsers.style.display = 'none';
}

searchForUsers(){
  console.log("Hello world!"); 
  let btns =  document.getElementById('btn-group');
  let rtnBtns =  document.getElementById('rtn-btn-users');
  let viewUsers =  document.getElementById('viewUsers');
  btns.style.display = 'none';
  viewUsers.style.display = 'block';
  rtnBtns.style.display = 'block';
}

searchForStations(){
  let btns =  document.getElementById('btn-group');
  this.setState({chosenIndex: true});
  btns.style.display = 'none';    
}

ReturnFromViewStations(){
  let btns =  document.getElementById('btn-group');
  btns.style.display = 'block';
  this.setState({chosenIndex: null});
}

submitChanges(){
  if (window.confirm("U sur?????")) {
    const obj = {
      "kiloWh": this.state.kWhAmount,
      "costPerKWh": this.state.costPerkWh,
      "adminId": this.state.adminID
    }
    updateAdminPolicies(this.state.adminKey,this.state.PricePolicyID,obj).then(res =>{
      console.log(res)
      window.location.reload(); 
    })
    .catch(error =>{
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

CalcCostPerRegion (){
    let from = document.getElementById('datepicker-from');
    let to = document.getElementById('datepicker-to');
    let region = document.getElementById('region-placeholder');
    if (from.value !== 'From' && to.value !== 'To' && region.value !== "Region" ){
      console.log(latlng2["lat"]);
      console.log(typeof from.value)
      console.log(to.value)
      getAdminAreaStationEnergy(this.state.adminKey,this.state.adminID,latlng2,10,from.value,to.value)
      .then(res => {
        console.log(res)
        let temp = document.getElementById('energy-cost-per-region');
        temp.value = res.data["energySum"];
      })
      .catch(error => {
        console.log(error.response)
      })
    }
    else if (region.value === "Region" && from.value !== 'From' && to.value !== 'To'){
      return M.toast({html: 'Please Select a Region '});        
    }
    else {
      return M.toast({html: 'Please Select a Date'});   
    }

    return null;
}

  render(){
    return(
      <div style={{ 
              backgroundImage: `url(${image})`,
              backgroundRepeat: 'no-repeat',
              height: '100%',
              backgroundAttachment: 'fixed',
              backgroundSize: 'cover',
              }} className = "background">
        <div className="a">

              {this.state.chosenIndex !== null && (
              <div className="stationsMap" id="stationsMap">
                <div>
                  <Map/>
                </div>
                <form action="">
                  <input type="button" className="stationbutton" value="Return" id="rtn-btn-stations" onClick={this.ReturnFromViewStations} />
                </form>      
              </div>
              )}
              <div className="viewUsers" id="viewUsers" display="none">
                <div className="row">
                  <div className="col s12">
                    <div className="row">
                      <div className="input-field col s12">
                        <i className="material-icons prefix"></i>
                        <input type="text" id="autocomplete-input" className="autocomplete"/>
                        <label for="autocomplete-input">Search for Policies</label>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col s2" id='res'>
                        <div className="card medium blue-grey darken-1 hoverable">
                          <div className="card-content white-text">
                            <span className="card-title">Price Policy</span>
                          </div>
                          <div className="card-action">
                            <div class="input-field col s12 right align">
                              <input placeholder="Id" id="policy_id" value={this.state.PricePolicyID} type="text" class="validate" onChange={this.handleUserInputPricePolicyID}/>
                              <label for="first_name"> Id </label>
                            </div> 
                            <div class="input-field col s12 right align">
                              <input placeholder="Cost Per kWh" id="costPerkWh" value={this.state.costPerkWh} type="text" class="validate" onChange={this.handleUserInputCostPerkWh} />
                              <label for="first_name"> Cost Per kWh </label>
                            </div>  
                            <div class="input-field col s12 right align">
                              <input placeholder="Amount of kWh" id="kkWhAmount" value={this.state.kWhAmount} type="text" class="validate" onChange={this.handleUserInputkWhAmount}/>
                              <label for="first_name"> Amount of kWh </label>
                            </div>
                            <div className="right-align">
                              <button className="waves-effect waves-light btn modal-trigger" 
                                      type="submit" 
                                      name="action" 
                                      id="sumbit-changes" 
                                      onClick={this.submitChanges}
                                      disabled={this.state.btnIndex}> Save Changes 
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <form action="">
                  <input type="button" 
                  className="returnbtn" 
                  value="Return" 
                  id="rtn-btn-users" 
                  onClick={this.ReturnFromViewUsers}
                  />
                </form>
            </div>
            <div className="row">
              <form action="">
                <div className="btn-group" id="btn-group" display="block">
                  <input type="button" className="leftButton" value="View Price Policies" id="lbtn" onClick={this.searchForUsers}/>
                  <input type="button" className="rightButton" value="View Stations" id="rbtn" onClick={this.searchForStations}/>
                </div>
              </form>
            </div>
          </div>
        <div className="row" id="second-part">
          <p style={{
            color: "white",
            fontSize: "40px",
            textAlign: "center",
          }}>Energy Monitoring</p>
          <div className="col s5" id="energy-per-region-card">
            <div className="card hoverable blue-grey darken-1" id="map-energy-monitoring" >
              <div className="card-content" id="map-energy-monitoring">
                <MapContainer
                  center={Athens}
                  onClick={this.handleClick}
                  zoom={5}
                  style={style.map}
                >
                  <TileLayer
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                  />
                  
                  <LocationMarker/>
                </MapContainer>
              </div>
              <div className="card-action">
                {/* <p className="titles-above-input">I am a very simple card. I am good at containing small bits of information.</p> */}
                  <input type="button" className="datepicker" value="From" id="datepicker-from" />
                  <input type="button" className="datepicker" value="To" id="datepicker-to" />
                  <input type="button" value="Region" id="region-placeholder" />
                  <div className="right-align">
                    <a className="waves-effect waves-light btn" onClick={this.CalcCostPerRegion}>Calculate</a>
                  </div>
                  <input type="text" value="Energy Cost per Region" id="energy-cost-per-region"/>
                    
              </div>
            </div>
          </div>      
          <div className="col s6 offset-s1">
              <div className="card hoverable medium blue-grey darken-1">
                <div className="card-content">
                  <div className="chart-energy-per-station">
                <ResponsiveLine
                  data={this.state.graphTotalEnergy}
                  margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
                  xScale={{ type: 'point' }}
                  yScale={{ type: 'linear', min: 'auto', max: 'auto', stacked: true, reverse: false }}
                  yFormat=" >-.2f"
                  axisTop={null}
                  axisRight={null}
                  axisBottom={{
                      orient: 'bottom',
                      tickSize: 5,
                      tickPadding: 5,
                      tickRotation: 0,
                      legend: 'Date',
                      legendOffset: 36,
                      legendPosition: 'middle'
                  }}
                  axisLeft={{
                      orient: 'left',
                      tickSize: 5,
                      tickPadding: 5,
                      tickRotation: 0,
                      legend: 'GWh',
                      legendOffset: -40,
                      legendPosition: 'middle'
                  }}
                  pointSize={10}
                  pointColor={{ theme: 'background' }}
                  pointBorderWidth={2}
                  pointBorderColor={{ from: 'serieColor' }}
                  pointLabelYOffset={-12}
                  useMesh={true}
                  legends={[
                      {
                          anchor: 'bottom-right',
                          direction: 'column',
                          justify: false,
                          translateX: 100,
                          translateY: 0,
                          itemsSpacing: 0,
                          itemDirection: 'left-to-right',
                          itemWidth: 80,
                          itemHeight: 20,
                          itemOpacity: 0.75,
                          symbolSize: 12,
                          symbolShape: 'circle',
                          symbolBorderColor: 'rgba(0, 0, 0, .5)',
                          effects: [
                              {
                                  on: 'hover',
                                  style: {
                                      itemBackground: 'rgba(0, 0, 0, .03)',
                                      itemOpacity: 1
                                  }
                              }
                          ]
                      }
                  ]}
                />
            </div>
                </div>
              </div>
            </div>
          <div className="col s6 offset-s1">
              <div className="card hoverable medium blue-grey darken-1">
                <div className="card-content">
                  <div className="chart-energy-per-day">
                    <ResponsiveStream
                      data={this.state.datastream}
                      keys={[ "2021-03-01", "2021-04-01", "2021-05-01", "2021-06-01", "2021-07-01" ]}
                      margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
                      axisTop={null}
                      axisRight={null}
                      axisBottom={{
                          orient: 'bottom',
                          tickSize: 5,
                          tickPadding: 5,
                          tickRotation: 0,
                          legend: '',
                          legendOffset: 36
                      }}
                      axisLeft={{ orient: 'left', tickSize: 5, tickPadding: 5, tickRotation: 0, legend: '', legendOffset: -40 }}
                      offsetType="silhouette"
                      colors={{ scheme: 'nivo' }}
                      fillOpacity={0.85}
                      borderColor={{ theme: 'background' }}
                      defs={[
                          {
                              id: 'dots',
                              type: 'patternDots',
                              background: 'inherit',
                              color: '#2c998f',
                              size: 4,
                              padding: 2,
                              stagger: true
                          },
                          {
                              id: 'squares',
                              type: 'patternSquares',
                              background: 'inherit',
                              color: '#e4c912',
                              size: 6,
                              padding: 2,
                              stagger: true
                          }
                      ]}
                      fill={[
                          {
                              match: {
                                  id: 'Paul'
                              },
                              id: 'dots'
                          },
                          {
                              match: {
                                  id: 'Marcel'
                              },
                              id: 'squares'
                          }
                      ]}
                      dotSize={8}
                      dotColor={{ from: 'color' }}
                      dotBorderWidth={2}
                      dotBorderColor={{ from: 'color', modifiers: [ [ 'darker', 0.7 ] ] }}
                      legends={[
                        {
                          anchor: 'bottom-right',
                          direction: 'column',
                          translateX: 100,
                          itemWidth: 80,
                          itemHeight: 20,
                          itemTextColor: '#999999',
                          symbolSize: 12,
                          symbolShape: 'circle',
                          effects: [
                            {
                              on: 'hover',
                              style: {
                                  itemTextColor: '#000000'
                              }
                            }
                          ]
                        }
                      ]}
                    />
            </div>
                </div>
              </div>
            </div>
          <div className="fixed-action-btn ">
              <button className="btn-floating tooltipped red pulse" data-position="left" data-tooltip="Click map to select region">
                !
              </button>
            </div>
        </div>      
      </div>  
    );
  }
}

export default EnergyMonitoring;