import React, { useState } from 'react';
import 'leaflet/dist/leaflet.css'
import image from './icons/image4.jpg'
import "./Map.css";
import L from 'leaflet'
import M from 'materialize-css';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents, Circle} from 'react-leaflet'
import "./EnergyMonitoring.css"
import { ResponsiveLine } from '@nivo/line'
import {Line} from 'react-chartjs-2'
import bxb from './icons/bxxb.png'
import icon from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import person from './icons/black-marker.png';


const fillBlueOptions = { fillColor: 'blue' }
const Athens = [37.983810, 23.727539];
const data = [
  {
    "id": "All Stations",
    "color": "hsl(160, 70%, 50%)",
    "data": [
      {
        "x": "Athens",
        "y": 210
      },
      {
        "x": "Thessaloniki",
        "y": 287
      },
      {
        "x": "Tou Kosta",
        "y": 59
      },
      {
        "x": "Kriti",
        "y": 57
      },
      {
        "x": "Paraliaki",
        "y": 38
      },
      {
        "x": "Ameriki",
        "y": 232
      },
      {
        "x": "Iasonas",
        "y": 242
      },
      {
        "x": "Orfeas",
        "y": 96
      },
      {
        "x": "Bill",
        "y": 124
      },
      {
        "x": "Pap",
        "y": 92
      },
      {
        "x": "Kostas",
        "y": 225
      },
      {
        "x": "others",
        "y": 55
      }
    ]
  }
]


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

function LocationMarker(props) {
  const [position, setPosition] = useState(null)
  const map = useMapEvents({
    click(e) {
      
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
      <Circle center={position} pathOptions={fillBlueOptions} radius={10000} />
    </Marker>
    
  )
}

class EnergyMonitoring extends React.Component{
  constructor(props){
    super(props);

  this.state = {
    fromIndex:null,
    toIndex:null,
    regionIndex:null,
  };
  // this.datepickerFrom=this.datepickerFrom.bind(this);
  // this.datepickerTo=this.datepickerTo.bind(this);
  // this.regionSelected=this.regionSelected.bind(this);
  this.CalcCostPerRegion=this.CalcCostPerRegion.bind(this);
}

componentDidMount(){
  M.AutoInit();

  document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.datepicker');
    var instances = M.Datepicker.init(elems, {format: 'yyyy-mm-dd'});
  });
}

CalcCostPerRegion (){
    let from = document.getElementById('datepicker-from');
    let to = document.getElementById('datepicker-to');
    let region = document.getElementById('region-placeholder');
    if (from.value != 'From' && to.value != 'To' && region.value != "Region" ){
      let temp = document.getElementById('energy-cost-per-region');
      temp.value = "1000";
    }
    else if (region.value == "Region" && from.value != 'From' && to.value != 'To'){
      return M.toast({html: 'Please Select a Region '});        
    }
    else {
      return M.toast({html: 'Please Select a Date'});   
    }
    return null;
  }

  render(){
    return(
      <div>
        <div className="row">
          <div style={{ 
                backgroundImage: `url(${image})`,
                backgroundRepeat: 'no-repeat',
                height: '1000px',
                backgroundAttachment: 'fixed',
                backgroundSize: '100% 100%',
                }} className = "background">
            <div className="col s5" id="energy-per-region-card">
              <div className="card hoverable blue-grey darken-1" id="map-energy-monitoring" >
                <div className="card-content" id="map-energy-monitoring">
                  <MapContainer
                    center={Athens}
                    onClick={this.handleClick}
                    zoom={11}
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
                      <a class="waves-effect waves-light btn" onClick={this.CalcCostPerRegion}>Calculate</a>
                    </div>
                    <input type="text" value="Energy Cost per Region" id="energy-cost-per-region"/>
                      
                </div>
              </div>
            </div>
            <div className="chart-energy-per-station">
                <ResponsiveLine
                  data={data}
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
                      legend: 'Charging Stations',
                      legendOffset: 36,
                      legendPosition: 'middle'
                  }}
                  axisLeft={{
                      orient: 'left',
                      tickSize: 5,
                      tickPadding: 5,
                      tickRotation: 0,
                      legend: 'Energy Consumption',
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
            <div className="chart-energy-per-day">
            <ResponsiveLine
                  data={data}
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
                      legend: 'Day',
                      legendOffset: 36,
                      legendPosition: 'middle'
                  }}
                  axisLeft={{
                      orient: 'left',
                      tickSize: 5,
                      tickPadding: 5,
                      tickRotation: 0,
                      legend: 'Energy Consumption',
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