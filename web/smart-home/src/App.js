import React, { Component } from 'react';
import BuildingPlan from './Components/BuildingPlan'
import Header from './Components/Header'
import SmartDevices from './Components/SmartDevices'
import InfoPanel from './Components/InfoPanel'
import Constants from './Constants'
import plan from './res/img/plan.png'
import device from './res/img/device.png'

import './App.css';
var shift = 1
var generateKey = (pre) => `${ pre }_${ new Date().getTime() + shift++ }`;

function getPropsForSmartDevice(device) {
		return {
				type: "device",
				title: device.key,
				key: device.key,
				props: [
					{
						"key": generateKey("prop"),
						"title": "prop1",
						"type": Constants.HEADER
					}, 
					{
						"key": generateKey("prop"),
						"title": "prop2",
						"type": Constants.INPUT_BLOCK
					},
					{
						"key": generateKey("prop"),
						"title": "prop3",
						"type": Constants.CHECKABLE
					}
				]
			}
	}
class App extends Component {

	constructor(props) {
		super(props)
		this.addSmartDevice = this.addSmartDevice.bind(this)
		this.saveInfoFromPanel = this.saveInfoFromPanel.bind(this)
		this.onSmartDeviceClick = this.onSmartDeviceClick.bind(this)

	}
	
	

	componentWillMount() {
		this.setState({"devices": []})
		this.setState({"infoPanelProps": null})
	}

	addSmartDevice({x,y}) {
		console.log("App.js: addSmartDevice " + x + " " + y);
		let devices = this.state.devices;
		let newDevice = {
			"key": "device" + x + "-" + y,
			"x": x,
			"y": y,

		}
		devices.push(newDevice);
		this.setState({"devices": devices}, () => console.log(this.state));
		this.setState({"infoPanelProps":  getPropsForSmartDevice(newDevice) })
	}

	saveInfoFromPanel() {
		console.log("Save info");
		this.setState({
			"infoPanelProps": null
		})
	}

	onSmartDeviceClick(x,y) {

		let clickedDevice = this.state.devices.filter(device => (device.x === x && device.y === y))[0]

		this.setState({
			"infoPanelProps": getPropsForSmartDevice(clickedDevice)
		})
	}

	render() {

		return (
			<div> 
				<Header />
				<InfoPanel 
					info = {this.state.infoPanelProps} 
					onOkClicked={this.saveInfoFromPanel} 
					onCancelClicked={() => this.setState(
							{"infoPanelProps": null,
							 "devices": this.state.devices.filter((device) => device.key !== this.state.infoPanelProps.key)
							}
						)}
				/>

				<div style={{position: "relative", left: "35%", top: "160px", margin: "0 0 0 -500px"}}>  
					<BuildingPlan imgSrc={plan} onClick={this.addSmartDevice}/> 
					<SmartDevices devices={this.state.devices} imgSrc={device} onSmartDeviceClick={this.onSmartDeviceClick}/> 
				</div>
			</div>

		);
	}

}

export default App;
