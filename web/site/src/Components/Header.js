import React, { Component } from 'react';
import '../App.css'

class Header extends Component {
  
  render() {

    return (
    		<div id="header"> 
    			<p id="title">Smart house project </p>
    			<div className="save" onClick={this.props.onSaveClicked}> Save </div>
    		</div>
    	);
  }
}

export default Header;