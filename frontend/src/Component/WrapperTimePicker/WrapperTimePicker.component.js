import React from 'react';
import DateTime from 'react-datetime';
import moment from 'moment';
import './react-datatime.css'
class WrapperTimePicker extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            datetime: props.sTime,
            startDate:props.startDate,
            endDate:props.endDate
        }
    }
    validDate = (current) =>{
        const{startDate,endDate} = this.state
        if(moment(current).isAfter(startDate) && moment(current).isBefore(endDate))
        return true;
        return false;
    }
    render(){
        // const {startDate,endDate} = this.state
        return(
            <div style = {{display:'flex',flexDirection:'row',justifyContent:"space-between",margin:'10px 0'}}>
            <div><span>Choose Your Time :</span></div>
            <DateTime required
                  value={this.state.datetime}
                  onChange={event => this.setState({ datetime: moment(event._d).valueOf() },
                                () => this.props.yourChoiceTime(this.state.datetime))
                            }
                  isValidDate={this.validDate}
                />
            {/* <DateTimePicker
            disableClock = {true}
            clearIcon = {null}
            className='class1'
            minutePlaceholder='mm'
            hourPlaceholder='hh'
            disableCalendar={true}
            // onChange= {time => this.setState({time},() =>{
            //         this.props.yourChoiceTime(
            //             moment(`${moment(Number(this.props.startDate)).format('DD.MM.YYYY')} ${this.state.time}`,
            //             "DD.MM.YYYY HH:mm")
            //             .toDate()
            //             .valueOf()
            //         )
            // })}
            // value={this.state.time}
            // minTime = {startDate}
            // maxTime = {endDate}
        /> */}
        </div>
        )
    }
}

export default WrapperTimePicker;