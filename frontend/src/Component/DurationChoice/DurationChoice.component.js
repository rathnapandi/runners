import React from 'react';

class PlaceChoice extends React.Component {
    state = {
        choice:this.props.dur
    }
    handleClick = e => this.props.choiceClick(e.target.value)
    render(){
        const {choice}= this.state
        console.log(choice);
        return(
            <form>
                <div style={{textAlign:'center'}}>
                    <label htmlFor='choice'>Duration:</label>
                    <span><input type='radio' name='choice' value='30' onClick={this.handleClick} defaultChecked={choice === '30'}/>30 min</span>
                    <span><input type='radio' name='choice' value='60' onClick={this.handleClick} defaultChecked={choice === '60'}/>1 hr</span>
                    <span><input type='radio' name='choice' value='90' onClick={this.handleClick} defaultChecked={choice === '90'}/>1.5hrs</span>
                    <span><input type='radio' name='choice' value='120' onClick={this.handleClick} defaultChecked={choice === '120'}/>2 hrs</span>
                </div>
            </form>
        )
    }
}

export default PlaceChoice;