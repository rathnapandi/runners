import React from 'react';

class PlaceChoice extends React.Component {
    state = {
        choice: this.props.dur
    }
    handleClick = e => {

        this.setState({choice: e.target.value})
        this.props.choiceClick(e.target.value)

    }

    render() {
        const {choice} = this.state

        // console.log(choice);
        return (

            <form>
                <div className="form-group">

                    <label><b> Duration: </b></label>
                    {/*<span><input type='radio' name='choice' value='30' onClick={this.handleClick} defaultChecked={choice === '30'}/> 30 Minutes </span>*/}
                    {/*<span><input type='radio' name='choice' value='60' onClick={this.handleClick} defaultChecked={choice === '60'}/> 1 Hour </span>*/}
                    {/*<span><input type='radio' name='choice' value='90' onClick={this.handleClick} defaultChecked={choice === '90'}/> 1.5 Hours </span>*/}
                    {/*<span><input type='radio' name='choice' value='120' onClick={this.handleClick} defaultChecked={choice === '120'}/>2 Hours</span>*/}
                    {/*<span><input type='radio' name='choice' value='150' onClick={this.handleClick} defaultChecked={choice === '150'}/>2.5 Hours</span>*/}
                    {/*<span><input type='radio' name='choice' value='180' onClick={this.handleClick} defaultChecked={choice === '180'}/>3 Hours</span>*/}
                    {/*<span><input type='radio' name='choice' value='210' onClick={this.handleClick} defaultChecked={choice === '210'}/>3.5 Hours</span>*/}
                    {/*<span><input type='radio' name='choice' value='240' onClick={this.handleClick} defaultChecked={choice === '240'}/>4 Hours</span>*/}


                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="30" onClick={this.handleClick} defaultChecked={choice === '30'}/>
                        <label className="form-check-label" htmlFor="inlineRadio1">30 Minutes</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="60" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio2">1 Hour</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="90" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio3">1.5 Hours</label>
                    </div>


                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="120" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio1">2 Hours</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="150" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio2">2.5 Hours</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="180" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio3">3 Hours</label>
                    </div>

                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="210" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio2">3.5 Hours</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="radio" name="choice"
                               value="240" onClick={this.handleClick}/>
                        <label className="form-check-label" htmlFor="inlineRadio3">4 Hours</label>
                    </div>


                </div>
            </form>
        )
    }
}

export default PlaceChoice;