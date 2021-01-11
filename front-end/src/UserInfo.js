import React from 'react';
import {BsPerson} from 'react-icons/bs';

class UserInfo extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      username: localStorage.getItem('username'),
      info: {}
    }
  }
  render(){
    const username = (this.state.username == null) ? '<anonymous>' : this.state.username;
    return(
      <div className="card"> 
        <div className="card-content">
          <div> <BsPerson/> </div>
          <div className="purple-text text-darken-4"> {'Hello ' + username} </div>
        </div>
      </div>
    )
  }
}

export default UserInfo;