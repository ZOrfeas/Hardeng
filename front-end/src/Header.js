import React from 'react';
import LoginForm from './LoginForm'

var pad = {
  paddingLeft: '1%',
  paddingRight: '1%',
};

class Header extends React.Component {

  render() {
    return (
      <nav>
        <div style={pad} className="nav-wrapper purple darken-4">
          <div className="brand-logo"> HardEng </div>
          <div className="right" >
            <LoginForm />
          </div>
        </div>
      </nav>
    )
  }
}

export default Header;