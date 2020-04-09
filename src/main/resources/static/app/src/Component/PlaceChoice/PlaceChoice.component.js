import React from 'react';

class PlaceChoice extends React.Component {
    handleClick = e => this.props.choiceClick(e.target.value)
    render(){
        return(
            <div>
                <form>
                    <div>
                        <label htmlFor='choice'>Choice:</label>
                        <span><input type='radio' name='choice' value='cheerer' onClick={this.handleClick}/>Cheerer</span>
                        <span><input type='radio' name='choice' value='runner' onClick={this.handleClick}/>Runner</span>
                    </div>
                </form>
            </div>
        )
    }
}

export default PlaceChoice;