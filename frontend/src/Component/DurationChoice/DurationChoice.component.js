import React from 'react';

class PlaceChoice extends React.Component {

state ={}
    handleClick = e => this.props.choiceClick(e.target.value)
    render(){
        return(
            <form>
                <div>
                    <label htmlFor='choice'>Duration:</label>
                    <span><input type='radio' name='choice' value='30' onClick={this.handleClick} defaultChecked/>30 min</span>
                    <span><input type='radio' name='choice' value='60' onClick={this.handleClick}/>1 hr</span>
                    <span><input type='radio' name='choice' value='90' onClick={this.handleClick}/>1.5hrs</span>
                    <span><input type='radio' name='choice' value='120' onClick={this.handleClick}/>2 hrs</span>
                </div>
            </form>
        )
    }
}

export default PlaceChoice;