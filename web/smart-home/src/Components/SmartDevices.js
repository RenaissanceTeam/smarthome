import React, { Component } from 'react';
import SmartItem from './SmartItem'
import '../App.css'


class SmartDevices extends Component {
	

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
		    			/>
		    		)
		    	}
	    	</div>
	    )
	}
}

export default SmartDevices;
