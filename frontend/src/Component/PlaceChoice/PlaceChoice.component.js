import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

class PlaceChoice extends React.Component {
    state = {
        choice: this.props.ch
    }
    handleClick = e => this.props.choiceClick(e.target.value)

    render() {
        const {choice} = this.state;
        return (
            <div className='placeholder-div'>
                <form>
                    <div className='form-check'>

                        <input className="form-check-input" type="radio" name='choice' value='cheerer'
                               onClick={this.handleClick} defaultChecked={choice === 'cheerer'}/>
                        <label className="form-check-label" htmlFor="cheerer">
                            Cheerer
                        </label>

                    </div>

                    <div className='form-check'>
                        <input className="form-check-input" type="radio" name='choice' value='runner'
                               onClick={this.handleClick} defaultChecked={choice === 'runner'}/>
                        <label className="form-check-label" htmlFor="cheerer">
                            Runner
                        </label>

                    </div>
                </form>
            </div>
        )
    }
}

export default PlaceChoice;