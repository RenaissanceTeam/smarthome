import React, { Component } from 'react';
import '../App.css'


class SmartItem extends Component {
  constructor(props) {
  	super(props)
  	this.onDragEnd = this.onDragEnd.bind(this)
  }

  onDragEnd(e) {
  	let x = this.props.x + e.nativeEvent.offsetX
  	let y = this.props.y + e.nativeEvent.offsetY
  	console.log(this.props);
  	this.props.onDragged(this.props.itemKey, x, y)
  }

  render() {
    return ( 
    		<img draggable="true" className="smartItem" src={this.props.imgSrc} alt="" style = {{ top: this.props.y, left: this.props.x }} 
    		    onClick={e => this.props.onClick(this.props.x, this.props.y)} 
    		    onDragEnd={this.onDragEnd}
    		    />
    	);
  }
}

export default SmartItem;
