import React, { Component } from 'react';
import SmartItem from './SmartItem'
import '../App.css'


class SmartDevices extends Component {
	constructor(props) {
		super(props)
		this.onRightClick = this.onRightClick.bind(this)
	}

	onRightClick(e) {
		e.preventDefault()
		console.log("pkm")
	}

	render() {
	    return (
	    	<div className="smartDevices">
		    	{this.props.devices.map( item =>  
		    		<SmartItem 
		    			key={item.key} 
		    			x={item.x} 
		    			y={item.y} 
		    			imgSrc={this.props.imgSrc}
		    			onClick={this.props.onSmartDeviceClick}
		    			onRightClick={this.onRightClick}
		    			/>
		    		)
		    	}
	    	</div>
	    )
	}
}

export default SmartDevices;
