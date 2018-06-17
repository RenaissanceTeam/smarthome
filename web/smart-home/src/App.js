import React, { Component } from 'react';
import BuildingPlan from './Components/BuildingPlan'
import Header from './Components/Header'
import SmartDevices from './Components/SmartDevices'
import SmartItem from './Components/SmartItem'
import './App.css';

class App extends Component {

	static img = "https://discuss.reactjs.org/uploads/default/7/72c8c07aaf4c414d.png";

	componentWillMount() {
		this.setState({"devices": []})
	}
	addSmartDevice(x,y) {
		console.log(x + " " + y);
		let devices = this.state.devices;
		devices.push(new SmartItem(x,y));
		this.setState({"devices": devices}, () => console.log(this.state));
	}

	render() {

		return (
			<div> 
				<Header />  
				<BuildingPlan imgSrc={App.img} onAddSmartDevice={(x,y) => this.addSmartDevice(x,y)}/> 
				<SmartDevices devices={this.state.devices}/> 
			</div>

			);
		}
	}

	export default App;
