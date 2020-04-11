import React from 'react';
import TimePicker from 'react-time-picker';
import moment from 'moment';
class WrapperTimePicker extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            startDate:new moment(Number(props.startDate)).format('HH:mm:ss'),
            endDate:new moment(Number(props.endDate)).format('HH:mm:ss'),
            time:new moment(Number(props.startDate)).format('HH:mm:ss')
        }
    }
    render(){
    console.log(this.state.time);
        const {startDate,endDate} = this.state
        return(
            <div style = {{display:'flex',flexDirection:'row',justifyContent:"space-between"}}>
            <div><span>Choose Your Time :</span></div>
            <TimePicker
            disableClock = {true}
            clearIcon = {null}
            className='class1'
            minutePlaceholder='mm'
            hourPlaceholder='hh'
            onChange= {time => this.setState({time},() =>{
                    this.props.yourChoiceTime(
                        moment(`${moment(Number(this.props.startDate)).format('DD.MM.YYYY')} ${this.state.time}`,
                        "DD.MM.YYYY HH:mm")
                        .toDate()
                        .valueOf()
                    )
            })}
            value={this.state.time}
            minTime = {startDate}
            maxTime = {endDate}
        />
        </div>
        )
    }
}

export default WrapperTimePicker;