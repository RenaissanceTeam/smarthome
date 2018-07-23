import React, { Component } from 'react';
import '../App.css'
import plus from '../res/img/plus.png'
import Constants from '../Constants'

var shift = 1
var generateKey = (pre) => `${ pre }_${ new Date().getTime() + shift++ }`;
function myFunction(module, target) {

    let moduleContainer = target.parentElement.parentElement

    let y = target.offsetTop + target.parentElement.offsetTop + moduleContainer.offsetTop
    let x =  target.offsetLeft + target.parentElement.offsetLeft + moduleContainer.offsetLeft

    let selectOptions = document.getElementById("myDropdown")
    selectOptions.style.top = y + "px"
    selectOptions.style.left = x + "px"
    console.log(selectOptions);
    selectOptions.classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}


class PropertyBlock extends Component {
  constructor(props) {
    super(props)
    this.onOptionSelected = this.onOptionSelected.bind(this)
    this.index = -1;

  }

  onOptionSelected(value ) {
    console.log("selected " + value +" for index " + this.index);

    let currentState = this.props.value;
    currentState[this.index] = this.props.options.filter(o => o.title === value)[0]
    this.props.onChange(currentState)
  }

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
            
            <div className="propertyTitle"> {this.props.title}  </div>
            <div className = "inputField"> 
              <input type="text" onChange={(e) => this.props.onChange(e.target.value)} defaultValue= {this.props.value}/> 
            </div>
           </div>
        );
    }

    if (this.props.type === Constants.CHECKABLE) {
      return ( 
          <div className="propertyBlock"> 
            
            <div className="propertyTitle"> {this.props.title}  </div>
            <div className = "inputField"> 
              <input type="checkbox" onChange={e => this.props.onChange(e.target.checked)} defaultChecked={this.props.value} /> 
            </div>
           </div>
      );
    }

    if (this.props.type === Constants.MODULES_BLOCK) {

      return  (
        <div className = "propertyBlock modulesBlock">
          <div > {this.props.title} </div>

          <div className="dropdown">
            <div id="myDropdown" className="dropdown-content" >
              {this.props.options.map((module) => <a key={generateKey("dropdown")} onClick={e => this.onOptionSelected(e.nativeEvent.target.textContent)} >{module.title}</a>)}
            </div>
          </div>

          {this.props.value.map( (module, index) => 
              <div key={generateKey("module")} className="moduleBlock">
                <div className="moduleBlockImageContainer" >
                  <div 
                      className="editButton dropbtn" 
                      onClick={e => {
                          myFunction(module, e.nativeEvent.target); this.index = index;
                          }}>
                        e
                  </div>
                  <img alt="" className="moduleBlockImage" src={module.img}/>
                </div>

                <div>{module.title}</div>
              </div>
          )}


          <div className="moduleBlock">
            <img alt="" className="moduleBlockImage" 
                  src={plus}
                  onClick={e => this.props.onChange([...this.props.value, {title: "new module", img: plus} ])}/>
          </div>

        </div>
        )
    }
    if (this.props.type === Constants.OPTIONS) {
      let index = 0

      return (
        <div className="propertyBlock">
          <div className="propertyTitle"> {this.props.title} </div>
          <div className="inputField">
            <select onChange={e => this.props.onChange(e.target.value)} defaultValue={this.props.value} > 
            {
              this.props.options.map( option => 
                <option key={index++} value= {option.title} > {option.title} </option>
              )
            }
            </select>
          </div>
        </div>
      )
    }

    return (
      <div className="propertyBlock"> Unknown block </div>
    );
  
  }
}

export default PropertyBlock;
