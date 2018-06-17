import React, { Component } from 'react';
import SmartItem from './SmartItem'
import '../App.css'


class BuildingPlan extends Component {
  
  constructor(prop) {	
  	super(prop);
  	console.log("building plan created " + prop);
  }

  onPlanClick(e) {
  	let x = e.nativeEvent.offsetX;
  	let y = e.nativeEvent.offsetY;
  	// console.log(x);
  	// console.log(y); 
  	this.props.onAddSmartDevice(x,y);
  }

  render() {
    return (
    	<img id="plan" src={this.props.imgSrc} alt="" onClick={(e) => this.onPlanClick(e)} />
    	);
  }
}

export default BuildingPlan;
