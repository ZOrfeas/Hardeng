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
      info: {},
      error: ''
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
    localStorage.setItem('username', this.state.username);
    
    if(true) {
      close();
      window.location.reload();
    }
    else this.setState({error: 'error'});
  }

  handleLogout(){
    this.setState({username: ''});
    this.setState({password: ''});
    for(const key in this.state.info) {
      this.setState({[key]: ''});
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
                  <div className="right-align addSpace">
                    <button
                      //onClick={close}
                      className="btn waves-effect waves-light"
                      type="submit"
                    >
                      Sign In
                  </button>
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