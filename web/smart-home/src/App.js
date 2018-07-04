import React, { Component } from 'react';
import BuildingPlan from './Components/BuildingPlan'
import Header from './Components/Header'
import SmartDevices from './Components/SmartDevices'
import plan from './res/img/plan.png'
import device from './res/img/device.png'
import './App.css';
var generateKey = (pre) => `${ pre }_${ new Date().getTime() }`;

class App extends Component {

	constructor(props) {
		super(props)
		this.addSmartDevice = this.addSmartDevice.bind(this)
	}
	
	componentWillMount() {
		this.setState({"devices": []})
	}

	addSmartDevice({x,y}) {
		console.log("App.js: addSmartDevice " + x + " " + y);
		let devices = this.state.devices;
		devices.push({
			"key": generateKey("device"),
			"x": x,
			"y": y,

		});
		this.setState({"devices": devices}, () => console.log(this.state));
		console.log("State after adding device: " + this.state.devices);
	}

	render() {

		return (
			<div> 
				<Header />
				<div style={{position: "relative", left: "50%", top: "160px", margin: "0 0 0 -500px"}}>  
					<BuildingPlan imgSrc={plan} onClick={this.addSmartDevice}/> 
					<SmartDevices devices={this.state.devices} imgSrc={device}/> 
				</div>
			</div>

			);
	}

}

export default App;
