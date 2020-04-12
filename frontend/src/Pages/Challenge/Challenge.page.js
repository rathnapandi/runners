/* eslint-disable react/jsx-no-target-blank */
import React from 'react';
import PlaceChoice from '../../Component/PlaceChoice/PlaceChoice.component';
import WrapperCalendar from '../../Component/WrapperCalendar/WrapperCalendar.component';
import WrapperTimePicker from '../../Component/WrapperTimePicker/WrapperTimePicker.component';
import DurationChoice from '../../Component/DurationChoice/DurationChoice.component'

import moment from 'moment-timezone';
import './challenge.css'
class Challenege extends React.Component {
    state = {
        currentuser:null,
        eventInfo:null,
        choice:'',
        duration:'30',
        startTime:undefined,
        startDate:undefined,
        endDate:undefined,
        groups:[],
        items:[]

    }

    eventCallFunc = async (num) => {
        const resp = await fetch('/api/events',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
              },
        })
        const eventInfo = await resp.json()
        const {id,name,description,startDate,endDate} = eventInfo.content[0]
        this.setState({
            eventInfo:{
                id,
                name,
                description,
                startDate,
                endDate
            },
            startTime:startDate
        })
    }
    participantCallFunc = async () =>{
        let groups = [],items= []
        const {eventInfo:{id}} = this.state
        const resp = await fetch(`/api/events/${id}/participants`,{
            method:'GET',
            headers:{
                'Content-Type':'application/json'
            }
        })
        const pList = await resp.json()
        pList.forEach(pItem =>{
            groups.push({
                id:pItem.id,
                title:`${pItem.firstName} ${pItem.lastName}`
            })
            items.push({
                id:pItem.id,
                group:pItem.id,
                title:'Runners Schedule',
                start_time:pItem.startTime,
                end_time:pItem.endTime
            })
        })
        this. setState({groups, items})
    }
    async componentDidMount(){
        const resp = await fetch('/api/users/currentuser',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
              },
        })
        const data = await resp.json()
        const {email,firstName,lastName,countryCode} = data
        this.setState({currentuser: {
            email,
            firstName,
            lastName,
            countryCode
        }})
        await this.eventCallFunc()
        await this.participantCallFunc()
    }
    handleChoice = (choice) => this.setState({choice})

    handleDuration = (duration) => this.setState({duration})

    handleTime = startTime => this.setState({startTime})

    handleClick = async() =>{
        const {eventInfo,currentuser,startTime,duration} = this.state
        const obj = {
            eventId:eventInfo.id,
            eventname:eventInfo.name,
            startTime:startTime,
            endTime:moment(Number(startTime)).add(Number(duration),'minutes').valueOf(),
            ...currentuser
        }
        try{
            const presponse = await fetch(`/api/events/${eventInfo.id}/participants`,{
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
              },
            body:JSON.stringify(obj)
            })
            console.log(await presponse.json());
            await this.participantCallFunc()
        }catch(e){
            console.log(e)
        }
    }
    render(){
        const {choice,eventInfo} = this.state
        return(
            <div className='challenge-div'>
                <ChallengeHead/>
               {eventInfo && <Description description={eventInfo.description}/>}

               {eventInfo && <Timing startDate={eventInfo.startDate} endDate={eventInfo.endDate}/>}
                <PlaceChoice choiceClick = {this.handleChoice}/>
                {
                    choice === 'runner' &&
                    <div className='challenge-div2'>
                        <DurationChoice choiceClick = {this.handleDuration}/>
                      { eventInfo &&
                        <WrapperTimePicker
                            yourChoiceTime = {this.handleTime}
                            startDate={eventInfo.startDate}
                            endDate={eventInfo.endDate}/>
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
            <h3>Challenge Name</h3>
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
    return(
        <div style={{display:'flex',flexDirection:'row',justifyContent:'space-evenly', width:'70%',margin:'10px 0'}}>
            <span>Start time:<span>{moment(Number(startDate)).format('Do MMM YYYY, HH:mm')}</span></span>
            <span>End time:<span>{moment(Number(endDate)).format('Do MMM YYYY, HH:mm')}</span></span>
        </div>
    )
}