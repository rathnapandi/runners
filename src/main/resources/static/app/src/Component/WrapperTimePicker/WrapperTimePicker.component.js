import React from 'react';
import TimePicker from 'react-time-picker'
import moment from 'moment';
class WrapperTimePicker extends React.Component {
    state = {
        time:new moment(1586357308722).format('HH:mm:ss')
    }
    render(){
        return(
            <TimePicker
            onChange= {time => this.setState({time},() =>{
                    this.props.yourChoiceTime(
                        moment(`${moment(1586357308722).format('DD.MM.YYYY')} ${this.state.time}`,
                        "DD.MM.YYYY HH:mm")
                        .toDate()
                        .valueOf()
                    )
            })}
            value={this.state.time}
            minTime = {new moment(1586357308722).format('HH:mm:ss')}
            maxTime = {new moment(1586357308722).add(6,'h').format('HH:mm:ss')}
        />
        )
    }
}

export default WrapperTimePicker;