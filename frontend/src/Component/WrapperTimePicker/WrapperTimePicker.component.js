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
        if(moment(current).isSameOrAfter(startDate) && moment(current).isSameOrBefore(endDate))
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

        </div>
        )
    }
}

export default WrapperTimePicker;