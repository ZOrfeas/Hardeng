import React from 'react';
import { BsPersonFill as Person } from 'react-icons/bs';
import { getDriverInfo } from './API';
import M from 'materialize-css';

class UserInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      driverKey: localStorage.getItem("driverKey"),
      driverID: localStorage.getItem("driverID"),
      info: {},
      importantInfo: ["DriverName", "Username", "Email", "BonusPoints"],
      error: null
    }

    if (this.state.driverKey !== null && this.state.driverID !== null) {
      getDriverInfo(this.state.driverKey, this.state.driverID)
        .then(res => {
          this.setState({info: res.data});
        })
        .catch(err => {
          this.setState({error: err.response.value})
        });
    }
    else {
      this.state.error = "Guest";
    }

    this.showInfo = this.showInfo.bind(this);
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

  render() {
    //const username = (this.state.driverKey === null) ? '<anonymous>' : this.state.username;
    return (
      <ul className="collapsible">
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
            <div className="collapsible-body green-text text-darken-2">
              {this.showInfo()}
            </div>
          )}
        </li>
      </ul>
    )
  }
}

export default UserInfo;