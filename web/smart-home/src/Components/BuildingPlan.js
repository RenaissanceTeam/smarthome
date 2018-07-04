import React, { Component } from 'react';
import '../App.css'


class BuildingPlan extends Component {
  
  constructor(prop) {	
  	super(prop)
  	console.log("building plan created " + prop)
    this.onPlanClick = this.onPlanClick.bind(this)
  }

  onPlanClick(e) {
  	let x = e.nativeEvent.offsetX;
  	let y = e.nativeEvent.offsetY;
  	this.props.onClick({x,y});
  }

  render() {
    return (
    	<img id="plan" src={this.props.imgSrc} alt="" onClick={e => this.onPlanClick(e)} />
    	);
  }

}

export default BuildingPlan;
