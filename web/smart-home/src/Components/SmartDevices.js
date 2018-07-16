import React, { Component } from 'react';
import SmartItem from './SmartItem'
import '../App.css'

const visibleClassName = "infoWindowVisible"
const invisibleClassName = "infoWindowInvisible"
class SmartDevices extends Component {	
	
  constructor(props) {
  	super(props)
  	this.onHover = this.onHover.bind(this)
  	this.hideInfoWindow = this.hideInfoWindow.bind(this)
  	this.showInfoWindow = this.showInfoWindow.bind(this)
  	this.isHovered = false
  	this.state = {
  		infoWindowClass: invisibleClassName,
  	}
  }

  
  onHover(e) {
  	this.isHovered = true
  	console.log(e.nativeEvent);
  	let x = e.target.offsetLeft + e.target.width
  	let y = e.target.offsetTop + e.target.height

  	setTimeout( () => {
  		if (this.isHovered)  {
  			this.showInfoWindow(x, y)}
		}
  		, 1000)
  }

  hideInfoWindow() {
  	this.isHovered = false
  	console.log("hide info window");
  	this.setState({
  		infoWindowClass: invisibleClassName,
  		x: 0,
  		y: 0,
  	})
  }

  showInfoWindow(x, y) {
  	console.log("show info window at " + x + ", " + y);
  	this.setState({
  		infoWindowClass: visibleClassName,
  		x: x,
  		y: y,
  	})
  }	


	render() {
	    return (
	    	<div className="smartDevices"> 

		    	<div >
			    	{this.props.devices.map( item =>  
			    		<SmartItem 
			    			key={item.key} 
			    			itemKey={item.key}
			    			x={item.x} 
			    			y={item.y} 
			    			imgSrc={this.props.imgSrc}
			    			onClick={this.props.onSmartDeviceClick}
			    			onDragged={this.props.onDragged}
			    			onHover={this.onHover}
			    			hideInfoWindow={this.hideInfoWindow}
			    			/>
			    		)
			    	}
				</div>

				<div className={this.state.infoWindowClass} id="smartItemInfoWindow" style = {{ top: this.state.y, left: this.state.x }}>
					
				</div>
	    	</div>
	    )
	}
}

export default SmartDevices;
