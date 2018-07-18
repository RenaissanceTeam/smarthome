import React, { Component } from 'react';
import BuildingPlan from './Components/BuildingPlan'
import Header from './Components/Header'
import SmartDevices from './Components/SmartDevices'
import InfoPanel from './Components/InfoPanel'
import Constants from './Constants'
import plan from './res/img/plan.png'
import device from './res/img/device.png'
import deviceImg from './res/img/device.png'
import plus from './res/img/plus.png'
 

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
						"title": "Device Title",
						"type": Constants.DEVICE_TITLE,
					},
					// {
					// 	"key": generateKey("prop"),
					// 	"title": "prop1",
					// 	"type": Constants.HEADER
					// }, 
					{
						"key": generateKey("prop"),
						"title": "Device type",
						"type": Constants.OPTIONS,
						"options": ["Smart Light", "Smart curtains", "Server", "Camera", "Thermostat", "Smart switch", "Smart Kitchen Stuff"]
					},
					{
						"key": generateKey("prop"),
						"title": "Can be controlled from Android app? ",
						"type": Constants.CHECKABLE
					},
					{
						"key": generateKey("prop"),
						"title": "Description",
						"type": Constants.INPUT_BLOCK
					},
					{
						"key": generateKey("prop"),
						"title": "Modules",
						"type": Constants.MODULES_BLOCK,
						"options": [
							{title: "Arduino Uno", img: deviceImg},
							{title: "Arduino Nano", img: deviceImg},
							{title: "Raspberry Pi", img: deviceImg},
							{title: "Engine", img: deviceImg},
							{title: "WiFi module", img: deviceImg},
							{title: "Relay", img: deviceImg},

						]
					},
					

				]
			}
	}

function getDefaultPropsValuesSmartDevice(title="default title") {
	return [
			{"title": "Device Title", "value":  title},
			{"title": "Device type", "value":  "Smart Light"},
			{"title": "Description", "value":  "def"},
			{"title": "Can be controlled from Android app? ", "value":  true},
			{"title": "Modules", "value":  []},

	]
}
class App extends Component {

	constructor(props) {
		super(props)
		this.addSmartDevice = this.addSmartDevice.bind(this)
		this.saveInfoFromPanel = this.saveInfoFromPanel.bind(this)
		this.onSmartDeviceClick = this.onSmartDeviceClick.bind(this)
		this.onSmartDeviceDrag = this.onSmartDeviceDrag.bind(this)
			
		// var config = {
		// 	apiKey: "AIzaSyAMPO-acgVmKxnB0sBfv7tzpxo5G-yuhfY",
		// 	authDomain: "smarthome-47922.firebaseapp.com",
		// 	databaseURL: "https://smarthome-47922.firebaseio.com",
		// 	projectId: "smarthome-47922",
		// 	storageBucket: "smarthome-47922.appspot.com",
		// 	messagingSenderId: "700180913950"
		// 	};

		// firebase.initializeApp(config);

		// this.database = firebase.database();

		// console.log(this.database);

	}
	

	componentWillMount() {
		this.setState({"devices": []})
		this.setState({"infoPanelProps": null})
		this.setState({"initialSetup": false})
	}


	addSmartDevice({x,y}) {
		if (this.state.infoPanelProps != null) {
			// console.log("there is a not completely set up device")
			return;
		}

		// console.log("App.js: addSmartDevice " + x + " " + y);
		let devices = this.state.devices;
		let epsilon = 350
		let r = devices.map( device => (Math.pow(device.x - x, 2) + Math.pow(device.y - y, 2) ))
		let isTooClose = r.some( i => i < epsilon)
		if (isTooClose) {
			console.log("too close");
			return;
		}
		let deviceKey = generateDeviceKey(x,y)
		let newDevice = {
			"key": deviceKey,
			"x": x,
			"y": y,
			"infoState": getDefaultPropsValuesSmartDevice(deviceKey)
		}

		devices.push(newDevice);
		// console.log("new device added");
		this.setState({"devices": devices});
		this.setState({"infoPanelProps":  getPropsForSmartDevice(newDevice) })
		this.setState({"initialSetup": true})
	}

	saveInfoFromPanel(info) {
		// console.log(this.state.devices[0].infoState)
		let devices = this.state.devices
		devices.filter(device => device.key === this.state.infoPanelProps.key)[0].infoState = info
		this.setState({
			"devices": devices,
			"infoPanelProps": null,
			initialSetup: false
		})
	}

	onSmartDeviceClick(x,y) {

		let clickedDevice = this.state.devices.filter(device => (device.x === x && device.y === y))[0]

		this.setState({
			"infoPanelProps": getPropsForSmartDevice(clickedDevice)
		})
	}

	onSmartDeviceDrag(key, x,y) {
		// console.log("new " + x + " " + y);
		let draggedDevice = this.state.devices.filter(device => (device.key === key))[0]
		draggedDevice.x = x
		draggedDevice.y = y
		draggedDevice.key = generateDeviceKey(x,y)
		this.setState({
			"devices": this.state.devices,
			"infoPanelProps": null
		})
	}

	render() {
		return (
			<div> 
				<Header />
				<InfoPanel 
					info = {this.state.infoPanelProps} 
					onOkClicked={this.saveInfoFromPanel} 
					onCancelClicked={() => {
						this.setState(
							{
							 "devices": this.state.devices.filter((device) => device.key !== this.state.infoPanelProps.key)
							})
						
						this.setState({ "infoPanelProps": null })
						}
					}
					onHideClicked={() => this.setState({ "infoPanelProps": null })}
					device={ this.state.infoPanelProps === null? 
						null :
						this.state.devices.filter((device) => device.key === this.state.infoPanelProps.key)[0] 
					}
					initialSetup= { this.state.initialSetup }
				/>

				<div style={{position: "relative"}}>  
					<BuildingPlan imgSrc={plan} onClick={this.addSmartDevice}/> 
					<SmartDevices devices={this.state.devices} imgSrc={device} onSmartDeviceClick={this.onSmartDeviceClick} 
					onDragged={this.onSmartDeviceDrag}
					/> 
				</div>
			</div>

		);
	}

}

export default App;
