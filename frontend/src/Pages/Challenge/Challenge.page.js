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
        items:[],
        isUpdate:false,
        pId:''
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
        const {eventInfo:{id},currentuser} = this.state
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
            if(currentuser.email === pItem.email)
            this.setState({isUpdate:true,
                startTime:pItem.startTime,
                duration:moment(pItem.endTime).diff(pItem.startTime,'minutes').toString(),
                pId:pItem.id
            })
        })
        this.setState({groups, items})
    }
    async componentDidMount(){
        const resp = await fetch('/api/users/currentuser',{
            method:'GET',
            headers: {
                'Content-Type': 'application/json'
              },
        })
        const data = await resp.json()
        const {email,id,firstName,lastName,countryCode} = data
        this.setState({
            currentuser: {
            email,
            firstName,
            lastName,
            countryCode
            },
            pId:id
        })
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
    handleUpdate = async() =>{
        const {eventInfo,startTime,duration,pId} = this.state
        const obj = {
            eventId:eventInfo.id,
            startTime,
            endTime:moment(Number(startTime)).add(Number(duration),'minutes').valueOf()
        }
        try{
            await fetch(`/api/events/${eventInfo.id}/participants/${pId}`,{
                method:'PUT',
                headers:{
                    'Content-Type':'application/json'
                },
                body:JSON.stringify(obj)
            })
            await this.participantCallFunc()
        }catch(e){
            console.log(e)
        }
    }
    handleRedirect = () =>{
        window.open("strava/login", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=400,height=400");
    }
    render(){
        const {choice,eventInfo,isUpdate,startTime,duration} = this.state
        console.log(duration,startTime);
        return(
            <div className='challenge-div'>
                <ChallengeHead/>
               {eventInfo && <Description description={eventInfo.description}/>}

               {eventInfo && <Timing startDate={eventInfo.startDate} endDate={eventInfo.endDate}/>}

                {eventInfo && <PlaceChoice choiceClick = {this.handleChoice}/>}
                {
                    choice === 'runner' &&
                    <div className='challenge-div2'>
                        <DurationChoice choiceClick = {this.handleDuration} dur={duration}/>
                      { eventInfo &&
                        <WrapperTimePicker
                            yourChoiceTime = {this.handleTime}
                            startDate={eventInfo.startDate}
                            endDate={eventInfo.endDate}
                            sTime={startTime}
                        />
                    }
                      <div style={{display:'flex',flexDirection:'column'}}>
                        <button style={{marginBottom:'5px'}} onClick={this.handleRedirect}>Connect</button>
                       {
                           isUpdate ?
                           <button onClick = {this.handleUpdate}>Update</button>
                           :
                           <button onClick = {this.handleClick}>Save</button>
                       }

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
        <div style={{color:'green',margin:'10px'}}>
            <h3 style={{margin:'0',padding:'10px 5px'}}>Challenge Name</h3>
        </div>
    )
}

const Description = ({description}) => {
    return(
        <div style={{display:'flex',flexDirection:'column',width:'max-content',background:'lightblue',boxShadow:'6px 4px 5px',padding:'5px 10px',borderRadius:'10px'}}>
            <span style={{color:'green',margin:'5px 0',textAlign:'center'}}>Description:</span>
            <span>{description}</span>
        </div>
    )
}

const Timing = ({startDate,endDate}) =>{
    return(
        <div style={{display:'flex',flexDirection:'row',justifyContent:'space-evenly', width:'50%',margin:'10px 0',background:'lightblue',padding:'10px',borderRadius:'10px'}}>
            <span>Start time:<span style={{color:'green'}}>{moment(Number(startDate)).format('Do MMM YYYY, HH:mm')}</span></span>
            <span >End time:<span style={{color:'green'}}>{moment(Number(endDate)).format('Do MMM YYYY, HH:mm')}</span></span>
        </div>
    )
}