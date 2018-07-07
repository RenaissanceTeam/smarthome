import React, { Component } from 'react';
import '../App.css'

import Constants from '../Constants'

class PropertyBlock extends Component {
  

  render() {
    if (this.props.type === Constants.HEADER) {
      return ( 
          <div className="propertyBlock"> {this.props.title} </div>
      	);
    } 

    if (this.props.type === Constants.INPUT_BLOCK) {

      return ( 
          <div className="propertyBlock"> 
            
            <p> {this.props.title}  </p>
            <p className = "inputField"> <input type="text"/> </p>
           </div>
        );
    }

    if (this.props.type === Constants.CHECKABLE) {
      return ( 
          <div className="propertyBlock"> 
            
            <p> {this.props.title}  </p>
            <p className = "inputField"> <input type="checkbox"/> </p>
           </div>
      );
    }


    
    return (
      <div className="propertyBlock"> Unknown block </div>
    );
  
  }
}

export default PropertyBlock;