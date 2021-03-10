import React from "react";
import Popup from "reactjs-popup";
import { userPay } from './API';
import { BsCreditCard as Card } from "react-icons/bs";
import { FaPaypal as Ewallet } from "react-icons/fa";
import { MdPayment } from "react-icons/md";

import 'reactjs-popup/dist/index.css';

class Payment extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      driverID: localStorage.getItem("driverID"),

      payment: this.props.payment,
      disabled: this.props.disabled,

      type: '',
      credential: null,
      
      error: '',
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleSelect = this.handleSelect.bind(this);
  }

  componentDidUpdate(prevProps) {
    if(prevProps.disabled !== this.props.disabled) {
      this.setState({disabled: this.props.disabled});
    }
  }
  handleSubmit(e, close){
    e.preventDefault();

    if(this.state.type === '' || this.state.credential === null) {
      this.setState({error: 'All fields required'})
    }

    else{
      this.props.doNext(e, this.state.type);

      /*
      userPay(this.state.driverID, this.state.type, this.state.credential)
        .then(res => {
          close();
          window.location.reload();
        })
        .catch(err => {
          if(err.response) {
            this.setState({error: err.response.status});
          }
          else{
            this.setState({error: 'Undefined Error'});
          }
        })*/

      
      //post payment to backend
    }

    
  }
  handleSelect(e) {
    this.setState({type: e.target.value});
  }

  render() {
    return (
      <div>
        <Popup
          onClose={this.handlePopupClose}
          trigger={open => <button type="button" open={open} disabled={this.state.disabled} className="btn-flat red-text"> Pay <MdPayment/> </button>}
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
                      disabled={this.state.type === ''}
                      name={this.state.type}
                      placeholder={this.state.type}
                      value={this.state.username}
                      onChange={e => {this.setState({credential: e.target.value})}}
                    />
                  </div>
                  <select className="browser-default" onChange={this.handleSelect}>
                    <option value="" disabled selected>Choose payment method </option>
                    <option value="credit card" class="left">Credit Card </option>
                    <option value="wallet" class="left">E-Wallet </option>
                  </select>
                  <div className="row">
                    <div className="red-text col s7">
                      {this.state.error != null ? this.state.error : ''}
                    </div>
                    <div className="col s5">
                      <button
                        className="btn waves-effect waves-light"
                        type="submit"
                        onClick={this.handleSubmit}
                      >
                        Submit
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

export default Payment;
