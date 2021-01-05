import React from "react";

class LoginForm extends React.Component{
    constructor(){
        super();
        this.state = { 
            username: '',
            password: '',
            error: null 
        };
        this.handleInput = this.handleInput.bind(this);
    }

    handleInput(e) {
        const name = e.target.name;
        const value = e.target.value;
        this.setState({[name]: value});
    }

    getName(){
        return this.state.name;
    }

    render(){
        return (
            <div className="container">
            <form onSubmit={(e) => e.preventDefault()}>
                <div className="row">
                    <div className="center input-field col s4">
                        <input 
                        name="username" 
                        placeholder="Username"
                        value={this.state.username}
                        onChange={this.handleInput}
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="input-field col s4 center-block">
                        <input 
                        name="password" 
                        type="password" 
                        placeholder="Password"
                        onChange={this.handleInput}
                        />
                    </div>
                </div>
                
                <button className="center btn waves-effect waves-light"> Sign In </button>     
            </form>
            </div>
        )
    }
}


export default LoginForm;