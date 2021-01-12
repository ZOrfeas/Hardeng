import React from 'react';
import { BsPersonFill as Person } from 'react-icons/bs';
import M from 'materialize-css';

class UserInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: localStorage.getItem('username'),
      info: {}
    }
  }

  componentDidMount() {
    M.AutoInit();
  }

  render() {
    const username = (this.state.username == null) ? '<anonymous>' : this.state.username;
    return (
      <ul className="collapsible">
        <li className="active">
          <div className="collapsible-header">
            <h5 className="purple-text text-darken-4">
              <Person style={{ verticalAlign: "bottom" }} /> &nbsp; User Info
            </h5>
          </div>
          <div className="collapsible-body purple-text text-darken-4">
            <div>{username}</div>
            <div>Fullname</div>
            <div>Email</div>
            <div>Bonus Points</div>
          </div>
        </li>
      </ul>
    )
  }
}

export default UserInfo;