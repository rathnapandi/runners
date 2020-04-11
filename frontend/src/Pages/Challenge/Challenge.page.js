import React from 'react';
import PlaceChoice from '../../Component/PlaceChoice/PlaceChoice.component';
import WrapperCalendar from '../../Component/WrapperCalendar/WrapperCalendar.component';
import WrapperTimePicker from '../../Component/WrapperTimePicker/WrapperTimePicker.component';
import DurationChoice from '../../Component/DurationChoice/DurationChoice.component'
//import {database} from '../../firebase/firebase.utils'
import moment from 'moment-timezone';
import './challenge.css'
//import runner from '../../images/runner.jpeg'
class Challenege extends React.Component {
    state = {
        id:'',
        email:'',
        choice:'',
        description:'',
        duration:'30',
        startTime:undefined,
        startDate:undefined,
        endDate:undefined,
        groups:[],
        items:[]
    }

    utilsFunc = async (num) => {
        let groups = [],items = []
        const resp2 = await fetch('/api/events',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
              },
        })
        const events = await resp2.json()
        events.content.forEach((event,id) => {
            if(!event.participants)
            return;
            groups.push({
                id,
                title:event.name
            })
            items.push({
                id,
                group:id,
                title:'Runners Schedule',
                start_time: event.participants.startTime,
                end_time:event.participants.endTime
            })
        })
        if(num === 1)
        this.setState({groups,
        items,
        id:events.content[0].id,
        description:events.content[0].description,
        startDate:moment(events.content[0].startDate).valueOf(),
        endDate:moment(events.content[0].endDate).valueOf(),
        startTime:moment(events.content[0].startDate).valueOf()
        })
        else
        this.setState({groups,items})
    }
    async componentDidMount(){
        // await this.utilsFunc()
        const resp = await fetch('/api/users/currentuser',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
              },
        })
        const data = await resp.json()
        const {email} = data
        this.setState({email})
        await this.utilsFunc(1)
    }
    handleChoice = (choice) => this.setState({choice})

    handleDuration = (duration) => this.setState({duration})

    //handleTime = startTime => this.setState({startTime})

    handleTime = startTime => {
             this.setState({startTime})
            }


    handleClick = async() =>{

        const {id,email,duration,startTime} = this.state

console.log(moment(startTime).add(Number(duration),'minutes').valueOf());

        // const region = moment.tz.guess()
     //   if(!startTime)
       // return;
        const obj = {
            email,
            startTime,
            endTime:moment(startTime).add(Number(duration),'minutes').valueOf()
        }
        try{

        await fetch(`/api/events/${id}/participant`,{
            method:'PUT',
            headers: {
                'Content-Type': 'application/json'
              },
            body:JSON.stringify(obj)
            })
            await this.utilsFunc(2)
        }catch(e){
            console.log(e)
        }
        
    }
    render(){
        const {choice} = this.state
        return(

            <div className='challenge-div'>
                <ChallengeHead/>
               {this.state.description && <Description description={this.state.description}/>}
                 {    this.state.startDate &&
                                  <Timing startDate={this.state.startDate} endDate={this.state.endDate}/>
                 }
                <PlaceChoice choiceClick = {this.handleChoice}/>
                {
                    choice === 'runner' &&
                    <div className='challenge-div2'>
                        <DurationChoice choiceClick = {this.handleDuration}/>
                      { this.state.startDate &&
                        <WrapperTimePicker yourChoiceTime = {this.handleTime} startDate={this.state.startDate} endDate={this.state.endDate}/>
                      }

                     <div style={{display:'flex',flexDirection:'row',justifyContent:'space-evenly'}}>
                        <button><a href='strava/login'target='_blank'>Connect</a></button>
                        <button onClick = {this.handleClick}>Save</button>
                      </div>
                    </div>
                }
                {
                    choice === 'runner' &&  this.state.items.length > 0 && 
                    <WrapperCalendar 
                        groups ={this.state.groups}
                        items = {this.state.items}
                    />
                }
                
                {
                    choice === 'cheerer' &&
                    <a href='link.com' target='_blank'>Click Here</a>
                }
            </div>
        )
    }
}

export default Challenege;

const ChallengeHead = () => {
    return(
        <div>
<h3>Challenge Name </h3>
        </div>
    )
}

const Description = ({description}) => {
    return(
        <div style={{display:'flex',flexDirection:'column',width:'40%'}}>
             <span>Description:</span>
             <span>{description}</span>
        </div>
    )
}
const Timing = ({startDate,endDate}) =>{
   console.log(moment(Number(startDate)).format('HH:mm'));
   return(
        <div style={{display:'flex',flexDirection:'row',justifyContent:'space-evenly', width:'70%',margin:'10px 0'}}>
            <span>Start time:<span>{moment(Number(startDate)).format('HH:mm')}</span></span>
            <span>End time:<span>{moment(Number(endDate)).format('HH:mm')}</span></span>
         </div>
     )
 }