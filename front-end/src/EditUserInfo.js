import React, { useState } from 'react';
import "./EditUserInfo.css";
import 'leaflet/dist/leaflet.css'
import M, { toast } from 'materialize-css';
import { getDriverInfo, updateDriverInfo } from './API';
import image from './icons/image3.jpg'

const driversHardcoded = [
  {driver_name: "Kostas", id: 11111, bonus_points: 10, carID: 13,  email: "kostas@kostas.gr", walletID: 12315464758},
];



class EditUserInfo extends React.Component {
  constructor(props) {
    super(props);
    this.setState({
      // username: "",
    });
    this.state = {
      btnIndex: "true",
      username: "",
      email: "",
      bonusPoints: "",
      walletID: "",
      DriverName: "",
      password: "",
      CardId: "",
      driverKey: localStorage.getItem("driverKey"),
      driverID: localStorage.getItem("driverID"),
    };
    this.handleinputDriverName = this.handleinputDriverName.bind(this);
    this.handleinputUsername = this.handleinputUsername.bind(this);
    this.handleinputEmail = this.handleinputEmail.bind(this);
    this.handleinputBP = this.handleinputBP.bind(this);
    this.handleinputCard = this.handleinputCard.bind(this);
    this.submitChanges = this.submitChanges.bind(this);
    this.handleinputWallet = this.handleinputWallet.bind(this);
    this.handleinputPassword = this.handleinputPassword.bind(this);
  }

  submitChanges(){
    if (this.state.password != ""){
      if (window.confirm("Save Changes")) {
        //get driver with id and change info
        this.setState({btnIndex: true});
        const obj = {
          "driverName": this.state.DriverName,
          "username": this.state.username,
          "password": this.state.password,
          "email": this.state.email,
          "bonusPoints": this.state.bonusPoints,
          "cardId": this.state.CardId,
          "walletId": this.state.walletID
        }
        updateDriverInfo(this.state.driverKey,300,obj).then(res => {
          console.log(res)
        })
        .catch(error => {
          console.log(error.response)
        })
      } 
      else {
          //do nothing
      }
    }
    else {
      return M.toast({html: 'Please Enter your Password' })
    }
    
  }
 
  componentDidMount(){
    M.AutoInit();

    const key = this.state.driverKey
    const id = this.state.driverID
    getDriverInfo(key,id).then(res => {
      console.log(res)
      this.setState({username: res.data["Username"]})
      this.setState({email: res.data["Email"]})
      this.setState({bonusPoints: res.data["BonusPoints"]})
      this.setState({walletID: res.data["WalletId"]})
      // this.setState({password: res.data["Username"]})
      this.setState({DriverName: res.data["DriverName"]})
      this.setState({CardId: res.data["CardId"]})
      // console.log(this.DriverName);
    })
    .catch(() => {
      console.log("Oops, exases")
    })
    document.addEventListener('DOMContentLoaded', function() {
      var elems = document.querySelectorAll('.datepicker');
      var instances = M.Datepicker.init(elems, {format: 'yyyy-mm-dd'});
    });
  }
  
  handleinputDriverName(e){
    this.setState({btnIndex: false});
    this.setState({DriverName: e.target.value})
  }

  handleinputUsername(e){
    this.setState({btnIndex: false});
    this.setState({username: e.target.value})
  }

  handleinputEmail(e){
    this.setState({btnIndex: false});
    this.setState({email: e.target.value})
  }

  handleinputBP(e){
    this.setState({btnIndex: false});
    this.setState({bonusPoints: e.target.value})
  }

  handleinputCard(e){
    this.setState({btnIndex: false});
    this.setState({CardId: e.target.value})
  }

  handleinputWallet(e){
    this.setState({btnIndex: false});
    this.setState({walletID: e.target.value})
  }

  handleinputPassword(e){
    this.setState({btnIndex: false});
    this.setState({password: e.target.value})
  }



   render(){
     return(
      
      <div className="a">
        <div style={{ 
              backgroundImage: `url(${image})`,
              width:'100%',
              backgroundRepeat: 'no-repeat',
              height: '1000px',
              backgroundSize: 'cover',
              }} className = "background">
          <div className="viewUserInfo">
            <div className="row">
              <div className="col s12">
                  <div className="row">
                    <div className="col s6" id='res'>
                      <div className="card blue-grey medium darken-1 hoverable">
                        <div className="card-content white-text">
                          <span className="card-title">Driver's Info.</span>
                        </div>
                        <div className="card-action">
                          <div class="input-field col s6">
                            <input placeholder={this.state.DriverName} value={this.state.DriverName} type="text" class="validate" onChange={this.handleinputDriverName}/>
                            <label for="first_name"> Driver's Name </label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.email} value={this.state.email} type="text" class="validate" onChange={this.handleinputEmail}/>
                            <label for="first_name"> Email</label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.bonusPoints} value={this.state.bonusPoints}  type="text" class="validate" onChange={this.handleinputBP}/>
                            <label for="first_name"> Bonus Points</label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.CardId} value={this.state.CardId} type="text" class="validate" onChange={this.handleinputCard} />
                            <label for="first_name"> Card </label>
                          </div> 
                          <div class="input-field col s6">
                            <input placeholder={this.state.username} value={this.state.username} type="text" class="validate" onChange={this.handleinputUsername}/>
                            <label for="first_name"> Username </label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.walletID} value={this.state.walletID}  type="text" class="validate" onChange={this.handleinputWallet}/>
                            <label for="first_name"> Wallet </label>
                          </div>
                          <div class="input-field col s6">
                            <input placeholder={this.state.password} value={this.state.password}  type="text" class="validate" onChange={this.handleinputPassword}/>
                            <label for="first_name"> Enter Old or New Password </label>
                          </div>

                          <div className="right-align">
                            <button className="waves-effect waves-light btn modal-trigger" 
                                    onClick={this.submitChanges}
                                    type="submit" 
                                    name="action" 
                                    id="change_user_info" 
                                    disabled={this.state.btnIndex}
                                    > Save Changes 
                            </button>
                          </div>
                        </div>
                        
                      </div>
                    </div>
                  </div>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col s6">
              <div className="card small blue-grey darken-1 hoverable" id="energy_per_km">
                <div className="card-content white-text">
                <span className="card-title">Calculate Energy Cost per km.</span>
                  <div class="input-field ">
                    <input placeholder="42 km" id="first_name" type="text" class="validate"/>
                    <label for="first_name"> Total distance traveled in chosen duration </label>
                  </div> 
                  <p>This is supposed to be  retrieved from the car</p>
                </div>
                <div className="card-action">      
                  <input type="button" className="datepicker" value="From" id="user-datepicker-from" />
                  <input type="button" className="datepicker" value="To" id="user-datepicker-to" />
                  <div class="input-field right align">
                    <input placeholder="0.42" id="first_name" type="text" class="validate"/>
                    <label for="first_name"> Calculated Cost </label>
                  </div> 
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default EditUserInfo;