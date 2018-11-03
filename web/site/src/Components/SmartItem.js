import React, { Component } from 'react';
import '../App.css'


class SmartItem extends Component {
  constructor(props) {
  	super(props)
  	this.onDragEnd = this.onDragEnd.bind(this)
  	// same as in App.css
  	this.height = 20
  	this.width = 20
  	this.ref = React.createRef()
  }

  onDragEnd(e) {
  	let x = this.props.x + e.nativeEvent.offsetX - this.width / 2
  	let y = this.props.y + e.nativeEvent.offsetY - this.height / 2
  	// console.log(this.props);
  	this.props.onDragged(this.props.itemKey, x, y)
  }


  componentDidMount() {
  	let x = parseInt(this.ref.current.style.left)
  	let y = parseInt(this.ref.current.style.top)

  	let scaler = 1.1
  	this.ref.current.animate( [
  				{height: this.height + "px", width: this.width + "px", top: y + "px", left: x + "px", opacity: 0.6},
  				{height: this.height* scaler + "px", width: this.width * scaler + "px", top: (y - (this.height * scaler - this.height) / 2) + "px",
  							 left: (x - (this.width * scaler - this.width) / 2) + "px", opacity: 1},
  				{height: this.height + "px", width: this.width + "px", top: y + "px", left: x + "px", opacity: 0.6},

  				] , { duration: 1500, iterations: Infinity } )

  }

  render() {
  	let smartItem = <img draggable="true" className="smartItem" src={this.props.imgSrc} alt="" 
		    			style = {{ top: this.props.y - this.height/2 , left: this.props.x - this.width/2 }} 
		    		    onClick={e => this.props.onClick(this.props.x, this.props.y)} 
		    		    onDragEnd={this.onDragEnd}
		    		    onMouseOver={e => this.props.onHover(e, this.props.itemKey)}
		    		    onMouseOut={this.props.hideInfoWindow}
		    		    ref = {this.ref} 
    		   		/>

    return (smartItem);
  }
}

export default SmartItem;
