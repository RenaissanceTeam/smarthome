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
  		info: []
  	}
  }

  
  onHover(e, itemKey) {
  	this.isHovered = true
  	let device = this.props.devices.filter((device) => device.key === itemKey)[0]

  	let x = e.target.offsetLeft + e.target.width
  	let y = e.target.offsetTop + e.target.height

  	setTimeout( () => {
  		if (this.isHovered)  {
  			this.showInfoWindow(x, y, device.infoState)}
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

  showInfoWindow(x, y, info) {
  	console.log("show info window at " + x + ", " + y );
  	this.setState({
  		infoWindowClass: visibleClassName,
  		x: x,
  		y: y,
  		info: info
  	}, e => console.log(e))
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
					{this.state.info["prop2"]}
				</div>
	    	</div>
	    )
	}
}

export default SmartDevices;
