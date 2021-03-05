import React from "react";
import {Redirect} from 'react-router-dom';
import Popup from "reactjs-popup";
import { getUserID, userLogin } from './API';
import 'reactjs-popup/dist/index.css';
import './LoginForm.css';
import M from 'materialize-css';

class LoginForm extends React.Component {
  constructor() {
    super();
    this.state = {
      username: '',
      password: '',
      userType: '',    /* "admin" or "driver" */
      error: null,
      redirectAdmin: false,
      redirectDriver: false,
    };
    this.handleInput = this.handleInput.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
    this.handlePopupClose = this.handlePopupClose.bind(this);
    this.renderRedirect = this.renderRedirect.bind(this);
  }

  handleSelect(e) {
    const value = e.target.value;

    this.setState({ userType: value });
  }

  handleInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }

  handleSubmit(e, close) {
    e.preventDefault();
    const username = this.state.username;
    const password = this.state.password;
    const userType = this.state.userType;

    if (username === '' || password === '' || userType === '') {
      this.setState({ error: 'All fields required' });
    }
    else {
      userLogin(username, password, userType)
        .then(res => {
          localStorage.setItem(userType + 'Key', res.data.token);
          console.log('Fuck ' + res.data.token);

          getUserID(res.data.token,userType)
            .then(resID =>{
              localStorage.setItem(userType + 'ID', resID.data);
              if (userType == 'driver'){
                this.setState({redirectDriver: true})
              }
              else{
                this.setState({redirectAdmin: true})
              }
              close();
              window.location.reload();
              
            });
         
          
        })
        .catch(err => {
          console.log('Serious? ' + err.response);
          if(err.response){
            this.setState({ error: err.response.status });
          }
        });
    }
    
  }

  handleLogout() {
    this.setState({ username: '' });
    this.setState({ password: '' });
    this.setState({redirectAdmin: false});
    this.setState({redirectDriver: false});
    localStorage.clear();
    window.location.reload();
    window.location.assign("/")
  }

  handlePopupClose() {
    this.setState({ username: '' });
    this.setState({ password: '' });
    this.setState({ userType: '' });
    this.setState({ error: null });
  }
  componentDidMount(){
    M.AutoInit();
  }

  renderRedirect = () => {
    if (this.state.redirectAdmin) {
      console.log("apo admin")
      return <Redirect to='/StationMonitoring'/>
    }
    else if (this.state.redirectDriver){
      console.log("apo driver")
      return <Redirect to='/EnergyMonitoring'/>
    }
  }
  render() {
    return (
      <div>
        <button onClick={this.handleLogout}  className="btn-flat waves-effect waves-light green-text"> Logout </button>
        <Popup
          onClose={this.handlePopupClose}
          trigger={open => <button open={open} className="btn-flat green waves-effect waves-light"> Login </button>}
          modal
        >
          {close => (
            <div>
              <diV className="center-align">
                <h5 > Enter your information </h5>
              </diV>
              <div className="container">
                <form className="addSpace" onSubmit={(e) => this.handleSubmit(e, close)}>
                  <div className="input-field">
                    <input
                      name="username"
                      placeholder="Username"
                      value={this.state.username}
                      onChange={this.handleInput}
                    />
                  </div>
                  <div className="input-field">
                    <input
                      name="password"
                      type="password"
                      placeholder="Password"
                      onChange={this.handleInput}
                    />
                  </div>
                  <div className="input-field">
                    <select className="browser-default" onChange={this.handleSelect}>
                      <option value='' disabled selected>Choose account type</option>
                      <option value="driver">Driver</option>
                      <option value="admin">Admin</option>
                    </select>
                  </div>
                  {this.renderRedirect()}
                  <div className="row">
                    <div className="red-text col s7">
                      {this.state.error != null ? this.state.error : ''}
                    </div>
                    <div className="col s5">
                      <button
                        className="btn waves-effect waves-light"
                        type="submit"
                      >
                        Sign In
                      </button>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          )}
        </Popup>
      </div>
    )
  }
}
export default LoginForm;