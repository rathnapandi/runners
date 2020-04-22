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
    //console.log(current);
        const{startDate,endDate} = this.state

        if(moment(current).isSameOrAfter(startDate,'minute') && moment(current).isSameOrBefore(endDate,'minute'))

        return true;
        return false;
    }
    render(){

        return(
            <div style = {{display:'flex',flexDirection:'row',margin:'10px 0'}}>
            <div><span style={{fontWeight:'bold'}}>Choose Your Start Time :</span></div>
            <DateTime required
                  value={this.state.datetime}
                  onChange={event => this.setState({ datetime: moment(event._d).valueOf() },
                                () => this.props.yourChoiceTime(this.state.datetime))
                            }
                  isValidDate={this.validDate}
                  defaultValue={this.state.datetime}
                />

        </div>
        )
    }
}

export default WrapperTimePicker;