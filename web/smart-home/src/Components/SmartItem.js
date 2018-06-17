import React, { Component } from 'react';
import '../App.css'


class SmartItem extends Component {
  constructor(x,y) {
  	super();
  	console.log("create smart item at " + x + ", " + y);
  }
  render() {
    return (
    	<img className="smartItem" src={this.props.imgSrc} alt="" />
    	);
  }
}

export default SmartItem;
