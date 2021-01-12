import React from "react";
import Popup from "reactjs-popup";
import { login } from './API'
import 'reactjs-popup/dist/index.css';
import './LoginForm.css';

class LoginForm extends React.Component {
  constructor() {
    super();
    this.state = {
      username: '',
      password: '',
      key: null,
      info: {},
      error: null
    };
    this.handleInput = this.handleInput.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
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

    this.setState({ error: null });

    if (username === '' || password === '') {
      this.setState({ error: 'Both fields required' });
    }
    else {
      login(username, password)
        .then(res => {
          this.setState({ key: res.data.key });
          localStorage.setItem('key', res.data.key);
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
    for (const key in this.state.info) {
      this.setState({ [key]: '' });
    }

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
                  <div className="row addSpace">
                    <div className="red-text col s8">
                      {this.state.error != null ? this.state.error : ''}
                    </div>
                    <div className="col s4">
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