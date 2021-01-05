import React from "react"
//import LoginForm from "./LoginForm"
import ChargingExperience from "./ChargingExperience"

const stations= [
  {position: [37.983810, 23.727539], label: "Athens"},
  {position: [40.629269, 22.947412], label: "Thessaloniki"}
];

class App extends React.Component {
  render() {
    return(
      <div>
        <ChargingExperience stations={stations}/>
      </div>
    );
  }
}

export default App;
