import React, { Component } from 'react';
import '../App.css'


class SmartItem extends Component {
  constructor(props) {
  	super(props)
  	this.onDragEnd = this.onDragEnd.bind(this)
  	// same as in App.css
  	this.height = 20
  	this.width = 20
  }

  onDragEnd(e) {
  	let x = this.props.x + e.nativeEvent.offsetX - this.width / 2
  	let y = this.props.y + e.nativeEvent.offsetY - this.height / 2
  	// console.log(this.props);
  	this.props.onDragged(this.props.itemKey, x, y)
  }


  render() {
  	

    return ( 
    		<img draggable="true" className="smartItem" src={this.props.imgSrc} alt="" 
    			style = {{ top: this.props.y - this.height/2 , left: this.props.x - this.width/2 }} 
    		    onClick={e => this.props.onClick(this.props.x, this.props.y)} 
    		    onDragEnd={this.onDragEnd}
    		    onMouseOver={e => this.props.onHover(e, this.props.itemKey)}
    		    onMouseOut={this.props.hideInfoWindow}
    		    />
    	);
  }
}

export default SmartItem;
