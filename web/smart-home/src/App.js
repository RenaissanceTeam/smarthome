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
var generateDeviceKey = (x,y) =>  "device" + x + "-" + y;

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
					},
					{
						"key": generateKey("prop"),
						"title": "prop3",
						"type": Constants.CHECKABLE
					}
				]
			}
	}

function getDefaultPropsValuesSmartDevice() {
	return [
		{"title": "prop1", "value":  null},
			{"title": "prop2", "value":  "def"},
			{"title": "prop3", "value":  true},
			{"title": "prop4", "value":  false}
	]
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
		if (this.state.infoPanelProps != null) {
			// console.log("there is a not completely set up device")
			return;
		}
		// console.log("App.js: addSmartDevice " + x + " " + y);
		let devices = this.state.devices;
		let newDevice = {
			"key": generateDeviceKey(x,y),
			"x": x,
			"y": y,
			"infoState": getDefaultPropsValuesSmartDevice()
		}

		devices.push(newDevice);
		// console.log("new device added");
		this.setState({"devices": devices}, () => console.log(this.state));
		this.setState({"infoPanelProps":  getPropsForSmartDevice(newDevice) })
	}

	saveInfoFromPanel(info) {
		console.log("Save info " + info);
		let devices = this.state.devices
		devices.filter(device => device.key === this.state.infoPanelProps.key)[0].infoState = info
		this.setState({
			"devices": devices,
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
					device={ this.state.infoPanelProps === null? 
						null :
						 this.state.devices.filter((device) => device.key === this.state.infoPanelProps.key)[0] }
				/>

				<div style={{position: "relative", left: "35%", top: "160px", margin: "0 0 0 -500px"}}>  
					<BuildingPlan imgSrc={plan} onClick={this.addSmartDevice}/> 
					<SmartDevices devices={this.state.devices} imgSrc={device} onSmartDeviceClick={this.onSmartDeviceClick} 
					onDrag={this.onSmartDeviceDrag} onDragEnd={this.onSmartDeviceDrag}/> 
				</div>
			</div>

		);
	}

}

export default App;
