import React, { Component } from 'react';
import '../App.css'
import PropertyBlock from './PropertyBlock'
import Constants from '../Constants'


class InfoPanel extends Component {

	constructor (props) {
		super(props)
		this.parseInfoFromPanel = this.parseInfoFromPanel.bind(this)
		this.onPropertyChanged = this.onPropertyChanged.bind(this)
		this.getPropValueFromState = this.getPropValueFromState.bind(this)
		
		this.info = []
		this.lastDevice = null
	}

	parseInfoFromPanel() {
		this.props.onOkClicked(this.info)
	}

	onPropertyChanged(title, value) {
		let allInfo = this.info
		console.log("property changed, info: " + allInfo);
		allInfo.filter( prop => prop.title === title )[0].value = value
		this.info = allInfo
		this.render()
	}

	getPropValueFromState(title) {
		let prop = this.info.filter( prop=> prop.title === title)[0]
		if (prop == null) {
			return null
		}
		return prop.value
	}

	render() {

		if (! this.props.info) {
			return null;
		} 

		if (this.lastDevice !== this.props.device.key) {
			console.log("saving new info about device");
			this.info = this.props.device.infoState
			this.lastDevice = this.props.device.key
		}

		if (this.props.info.type === "device") {
			let device = this.props.info
			return (
				<div id="infoPanel">
				<PropertyBlock type={Constants.HEADER} title={device.title} />
				{ device.props.map( ({key, title, type}) => <PropertyBlock key={key} type={type} title = {title}
									value={this.getPropValueFromState(title)} 
									onChange={ value => this.onPropertyChanged(title, value)} /> ) }


				<button className="cancelButton" onClick={() => this.props.onCancelClicked()}> CANCEL </button>
				<button className="okButton" onClick={() => this.parseInfoFromPanel() }> OK </button>
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
