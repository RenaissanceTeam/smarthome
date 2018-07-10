import React, { Component } from 'react';
import '../App.css'


class SmartItem extends Component {
  // constructor(props) {
  // 	// console.log("SmartItem.js: props " + props)
  // 	// super(props);
  // 	// let x,y
  // 	// x = props.x
  // 	// y = props.y
  // 	// console.log("create smart item at " + x + ", " + y);
  // }

  render() {
    return ( 
    		<img draggable="true" className="smartItem" src={this.props.imgSrc} alt="" style = {{ top: this.props.y, left: this.props.x }} 
    		    onClick={e => this.props.onClick(this.props.x, this.props.y)} onContextMenu={e => this.props.onRightClick(e)} 
    		    />
    	);
  }
}

export default SmartItem;
