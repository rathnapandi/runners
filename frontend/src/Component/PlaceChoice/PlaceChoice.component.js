import React from 'react';

class PlaceChoice extends React.Component {
    state = {
        choice:this.props.ch
    }
    handleClick = e => this.props.choiceClick(e.target.value)
    render(){
        const {choice} = this.state;
        return(
            <div className='placeholder-div'>
                <form>
                    <div>
                        <label htmlFor='choice'><b>Choice:</b></label>
                        <span><input type='radio' name='choice' value='cheerer' onClick={this.handleClick} defaultChecked={choice === 'cheerer'}/>Cheerer</span>
                        <span><input type='radio' name='choice' value='runner' onClick={this.handleClick} defaultChecked={choice === 'runner'}/>Runner</span>
                    </div>
                </form>
            </div>
        )
    }
}

export default PlaceChoice;