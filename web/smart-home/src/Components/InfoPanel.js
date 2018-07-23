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
		
		this.state = {
			info: null,
			lastDevice: null	
		}
	}

	componentWillReceiveProps(props) {
		if (props.info !== null && props.device != null) {
			this.setState({info: props.device.infoState, lastDevice: props.device.key})
		}
	}

	parseInfoFromPanel() {
		this.props.onOkClicked(this.state.info)
	}

	onPropertyChanged(title, value) {
		// console.log(value);

		// completely rewrite value of one item (filtered by title)
		let allInfo = this.state.info
		allInfo.filter( prop => prop.title === title )[0].value = value
		if (title === "Device type") {
			this.props.onNewDeviceType(this.props.device.key, value)
		}
		this.setState({info: allInfo})
	}

	getPropValueFromState(title) {
		let prop = this.state.info.filter( prop=> prop.title === title)[0]
		if (prop == null) {
			console.log("prop is null for " + title);
			return null
		}
		return prop.value
	}

	render() {
		if (! this.props.info) { // not the same as info in state
			return null;
		} 

		if (this.props.info.type === "device") {
			let device = this.props.info
			let submit = (init) => init ?  
						<div className="submitField">
							<button className="cancelButton" onClick={() => this.props.onCancelClicked()}> CANCEL </button>  
							<button className="okButton" onClick={() => this.parseInfoFromPanel() }> OK </button>
						</div>
						: 
						<div className="submitField">
							<button className="okButton" onClick={() => this.parseInfoFromPanel() }> UPDATE </button>
							<button className="cancelButton" onClick={() => this.props.onCancelClicked() }> DELETE </button>
							<button className="hideButton" onClick={() => this.props.onHideClicked() }> HIDE </button>
						</div>
			return (
				<div id="infoPanel">
				{ device.props.map( ({key, title, type, options}) => <PropertyBlock key={key} type={type} title = {title} options={options}
									value={this.getPropValueFromState(title)} 
									onChange={ value => this.onPropertyChanged(title, value)} /> ) }
				{submit(this.props.initialSetup)}
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
