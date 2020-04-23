import React from 'react';
import Timeline from 'react-calendar-timeline'
import 'react-calendar-timeline/lib/Timeline.css'
import moment from 'moment'


class WrapperCalendar extends React.Component{
    render(){
        const {groups,items,startDate,endDate} = this.props
    return(
        <div style={{width:'95vw', marginBottom:'35px'}}>
                <Timeline 
                groups={groups}
                items={items}
                defaultTimeStart={Number(startDate)}
                defaultTimeEnd={Number(endDate)}
            />
        </div>
    )
    }
}
export default WrapperCalendar;