import React from "react";
import Popup from "reactjs-popup";
import 'reactjs-popup/dist/index.css';
import './LoginForm.css';

class LoginForm extends React.Component {
  constructor() {
    super();
    this.state = {
      username: '',
      password: '',
      other: {},
      error: null
    };
    this.handleInput = this.handleInput.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleInput(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({ [name]: value });
  }

  handleSubmit(e) {
    e.preventDefault();
  }

  getName() {
    return this.state.username;
  }

  render() {
    return (
      <div>
        <Popup
          trigger={<button className="btn right waves-effect waves-light"> Login </button>}
          modal
        >
          <diV className="center-align">
            <h5 > Use your information </h5>
          </diV>
          <div className="container">
            <form className="addSpace" onSubmit={this.handleSubmit}>
              <div className="input-field browser-default">
                <input
                  name="username"
                  placeholder="Username"
                  value={this.state.username}
                  onChange={this.handleInput}
                />
              </div>
              <div className="input-field input-outlined">
                <input
                  name="password"
                  type="password"
                  placeholder="Password"
                  onChange={this.handleInput}
                />
              </div>
              <div className="right-align addSpace">
                <button
                  className="btn waves-effect waves-light"
                >
                  Sign In
              </button>
              </div>
            </form>
          </div>
        </Popup>
      </div>
    )
  }
}


export default LoginForm;