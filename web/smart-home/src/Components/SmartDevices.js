import React, { Component } from 'react';
import '../App.css'


class SmartDevices extends Component {
  

  
  render() {
  	console.log("render SmartDevices layer " + this.props.devices);
    return (
    	<div>
    		{this.props.devices.map((x) => x)}
    	</div>
  )};
}

export default SmartDevices;
