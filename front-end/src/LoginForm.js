import React from "react";
import Popup from "reactjs-popup";
import {userLogin} from './API';
import 'reactjs-popup/dist/index.css';
import './LoginForm.css';

class LoginForm extends React.Component {
  constructor() {
    super();
    this.state = {
      username: '',
      password: '',

      // "adminKey" or "driverKey"
      keyType: '',
      error: null,
    };
    this.handleInput = this.handleInput.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
  }

  handleSelect(e) {
    const value = e.target.value;

    this.setState({ keyType: value });
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
    const keyType = this.state.keyType;

    this.setState({ error: null });

    if (username === '' || password === '' || keyType === '') {
      this.setState({ error: 'All fields required' });
    }
    else {
      userLogin(username, password, keyType)
        .then(res => {
          //this.setState({ [keyType]: res.data.key });
          localStorage.setItem(keyType, res.data.key);
          localStorage.setItem('username', username);
          close();
          window.location.reload();
        })
        .catch(err => {
          console.log(err);
          this.setState({ error: err.response.status });
        });
    }
  }

  handleLogout() {
    this.setState({ username: '' });
    this.setState({ password: '' });

    localStorage.clear();
    window.location.reload();
  }

  render() {
    return (
      <div>
        <button onClick={this.handleLogout} className="btn-flat waves-effect waves-light yellow-text"> Logout </button>
        <Popup
          trigger={open => <button open={open} className="btn-flat yellow waves-effect waves-light"> Login </button>}
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
                      <option value="driverKey">Driver</option>
                      <option value="adminKey">Admin</option>
                    </select>
                  </div>
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