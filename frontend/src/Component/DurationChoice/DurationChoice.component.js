import React from 'react';

class PlaceChoice extends React.Component {
    state = {
        choice:this.props.dur
    }
    handleClick = e => {

    this.setState({choice:e.target.value})
    this.props.choiceClick(e.target.value)

    }
    render(){
        const {choice}= this.state

       // console.log(choice);
        return(
            <form>
                <div style={{textAlign:'center'}}>
                    <label htmlFor='choice'>Duration:</label>
                    <span><input type='radio' name='choice' value='30' onClick={this.handleClick} defaultChecked={choice === '30'}/>30 mins</span>
                    <span><input type='radio' name='choice' value='60' onClick={this.handleClick} defaultChecked={choice === '60'}/>1 hr</span>
                    <span><input type='radio' name='choice' value='90' onClick={this.handleClick} defaultChecked={choice === '90'}/>1.5hrs</span>
                    <span><input type='radio' name='choice' value='120' onClick={this.handleClick} defaultChecked={choice === '120'}/>2 hrs</span>
                    <span><input type='radio' name='choice' value='150' onClick={this.handleClick} defaultChecked={choice === '150'}/>2.5 hrs</span>
                    <span><input type='radio' name='choice' value='180' onClick={this.handleClick} defaultChecked={choice === '180'}/>3 hrs</span>
                    <span><input type='radio' name='choice' value='210' onClick={this.handleClick} defaultChecked={choice === '210'}/>3.5 hrs</span>
                    <span><input type='radio' name='choice' value='240' onClick={this.handleClick} defaultChecked={choice === '240'}/>4 hrs</span>
                </div>
            </form>
        )
    }
}

export default PlaceChoice;