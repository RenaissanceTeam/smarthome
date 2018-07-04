import React, { Component } from 'react';
import '../App.css'
import PropertyBlock from './PropertyBlock'
import Constants from '../Constants'


class InfoPanel extends Component {
  render() {
  	console.log("Rendering infoPanel");
  	if (! this.props.info) {
  		return null;
  	} 

  	if (this.props.info.type === "device") {
  		let device = this.props.info
  		return (
  			<div id="infoPanel">
	    	<PropertyBlock type={Constants.HEADER} title={device.title} />
	    	{ device.props.map( ({key, title, type}) => <PropertyBlock key={key} type={type} title = {title} /> ) }


	    	<button className="cancelButton" onClick={() => this.props.onCancelClicked()}> CANCEL </button>
	    	<button className="okButton" onClick={() => this.props.onOkClicked() }> OK </button>
    	</div>
  			)
  	}
    return (
    	<div id="infoPanel">
	    	
	    	<input type="submit"/>
    	</div>
    	)
	}
}

export default InfoPanel;
