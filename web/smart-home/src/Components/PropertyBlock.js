import React, { Component } from 'react';
import '../App.css'

import Constants from '../Constants'

class PropertyBlock extends Component {
  

  render() {
    if (this.props.type === Constants.HEADER) {
      return ( 
          <div className="propertyBlock"> {this.props.value} </div>
      	);
    } 

    if (this.props.type === Constants.DEVICE_TITLE) {
      return <div 
                className="propertyBlock deviceTitle" 
                contentEditable="true" 
                onInput={e => this.props.onChange(e.nativeEvent.target.textContent) }
              > {this.props.value}  </div>
    }

    if (this.props.type === Constants.INPUT_BLOCK) {
      return ( 
          <div className="propertyBlock"> 
            
            <p> {this.props.title}  </p>
            <p className = "inputField"> <input type="text" onChange={(e) => this.props.onChange(e.target.value)} defaultValue= {this.props.value}/> </p>
           </div>
        );
    }

    if (this.props.type === Constants.CHECKABLE) {
      return ( 
          <div className="propertyBlock"> 
            
            <p> {this.props.title}  </p>
            <p className = "inputField"> 
              <input type="checkbox" onChange={(e) => this.props.onChange(e.target.checked)} defaultChecked={this.props.value} /> 
            </p>
           </div>
      );
    }

    if (this.props.type === Constants.OPTIONS) {
      let index = 0

      return (
        <div className="propertyBlock">
          <p> {this.props.title} </p>
          <p className="inputField">
            <select onChange={e => this.props.onChange(e.target.value)} defaultValue={this.props.value} > 
            {
              this.props.options.map( option => 
                <option key={index++} value= {option} > {option} </option>
              )
            }
            </select>
          </p>
        </div>
      )
    }

    return (
      <div className="propertyBlock"> Unknown block </div>
    );
  
  }
}

export default PropertyBlock;
