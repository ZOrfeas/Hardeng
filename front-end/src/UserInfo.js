import React from 'react';
import { BsPersonFill as Person } from 'react-icons/bs';
import { getDriverInfo, postPricePolicy } from './API';
import M from 'materialize-css';
import Payment from './Payment';

const policiesHardcoded = [
  {PricePolicyID: 1, KWh: 5, CostPerKWh: 3},
  {PricePolicyID: 2, KWh: 6, CostPerKWh: 4},
  {PricePolicyID: 3, KWh: 100, CostPerKWh: 6}
];

class UserInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      driverKey: localStorage.getItem("driverKey"),
      driverID: localStorage.getItem("driverID"),
      info: {},
      policies: policiesHardcoded,
      PricePolicyID: null,
      importantInfo: ["DriverName", "Username", "Email", "BonusPoints"],
      error: null
    }

    if (this.state.driverKey !== null && this.state.driverID !== null) {
      getDriverInfo(this.state.driverKey, this.state.driverID)
        .then(res => {
          this.setState({info: res.data});
        })
        .catch(err => {
          if (err.response){
            this.setState({error: err.response.value})
          }
        });
    }
    else {
      this.state.error = "Guest";
    }

    this.showInfo = this.showInfo.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
    this.buyPolicy = this.buyPolicy.bind(this);
  }

  componentDidMount() {
    M.AutoInit();
  }
  showInfo() {
    const info = this.state.info;

    //return Object.keys(info).map(key => {return <div>{String(key) + ": " + info[key]}</div>});
    
    return(
      this.state.importantInfo.map(key => {return <div>{info[key]}</div>})
    )
  }
  handleSelect(e){
    this.setState({[e.target.name]: e.target.value});
  }
  buyPolicy(e){
    postPricePolicy(this.state.driverKey, this.state.driverID, this.state.PricePolicyID)
      .then(res => {
        window.location.reload();
      })
      .catch(err => {
        if(err.response) {
          M.toast({html: 'BuyPolicy error ' + err.response.status, classes:"purple darken-4 yellow-text"});
        }
      });
  }

  render() {
    //const username = (this.state.driverKey === null) ? '<anonymous>' : this.state.username;
    return (
      <ul className="collapsible white">
        <li className="active">
          <div className="collapsible-header">
            <h5 className="black-text">
              <Person style={{ verticalAlign: "bottom" }} /> &nbsp; User Info
            </h5>
          </div>
          {this.state.error !== null && (
            <div className="collapsible-body red-text"> {this.state.error} </div>
          )}
          {this.state.error === null && this.state.driverKey !== null && (
            <div className="collapsible-body">
              {this.showInfo()}

              <select name="PricePolicyID" className="browser-default" onChange={this.handleSelect}>
                <option value="" disabled selected>Choose policy</option>
                {this.state.policies.map(({PricePolicyID, CostPerKWh}, index) => <option value={PricePolicyID}> {CostPerKWh + '€ per KWh'} </option>)}
              </select>

              <Payment
                disabled={this.state.PricePolicyID === null} 
                payment={10101}
                doNext={e => this.buyPolicy(e)}
              />
            </div>
          )}
        </li>
      </ul>
    )
  }
}

export default UserInfo;