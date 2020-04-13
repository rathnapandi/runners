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
        prevSetTime:null,
        duration:'30',
        startTime:undefined,
        startDate:undefined,
        endDate:undefined,
        groups:[],
        items:[],
        isUpdate:false,
        pId:'',
        authToken:null,
        messageDisplay:false
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
                title:`${pItem.firstName} ${pItem.lastName}-${moment(pItem.startTime).format('h:mm a')} to ${moment(pItem.endTime).format('h:mm a')}`,
                start_time:pItem.startTime,
                end_time:pItem.endTime
            })
            if(currentuser.email === pItem.email)
            this.setState({isUpdate:true,
                startTime:pItem.startTime,
                prevSetTime:{
                    startTime:pItem.startTime,
                    endTime:pItem.endTime
                },
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
        const {email,id,firstName,lastName,countryCode,oauthToken:{expires_at}} = data
        this.setState({
            currentuser: {
            email,
            firstName,
            lastName,
            countryCode
            },
            pId:id,
            authToken:expires_at
        })
        this.props.sentName({firstName,lastName})
        await this.eventCallFunc()
        await this.participantCallFunc()
    }
    handleChoice = (choice) => this.setState({choice})

    handleDuration = (duration) => this.setState({duration})

    handleTime = startTime => this.setState({startTime})

    handleClick = async() =>{
        const {eventInfo,currentuser,startTime,duration} = this.state
        console.log(eventInfo.name);

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
            this.setState({messageDisplay:true},() => setTimeout(() =>{
                this.setState({messageDisplay:false})
            },1500))
            await this.participantCallFunc()
        }catch(e){
            console.log(e)
        }
    }
    handleRedirect = () =>{
        window.open("strava/login", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=400,height=400");
    }
    render(){
        const {choice,eventInfo,isUpdate,startTime,duration,authToken,messageDisplay,prevSetTime} = this.state
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
                       {eventInfo && <DurationChoice choiceClick = {this.handleDuration} dur={duration}/>}

                      <div style={{display:'flex',flexDirection:'row',alignItems:'center',justifyContent:'center'}}>
                       {eventInfo&& <WrapperTimePicker
                            yourChoiceTime = {this.handleTime}
                            startDate={eventInfo.startDate}
                            endDate={eventInfo.endDate}
                            sTime={startTime}
                        />
                       }
                        {prevSetTime && <div style={{position:'relative',left:'20px',color:'green'}}>
                            <span>Your current run schedule</span>
                            <span>{`${moment(prevSetTime.startTime).format('MM/D/YYYY h:mm a')} to ${moment(prevSetTime.endTime).format('h:mm a')}`}</span>
                        </div>
                        }
                        </div>

                      <div style={{display:'flex',flexDirection:'column'}}>
                       { authToken === 0 && <button style={{marginBottom:'5px'}} onClick={this.handleRedirect}>Connect to Strava</button>}
                       {
                           isUpdate ?
                           <button onClick = {this.handleUpdate}>Update</button>
                           :
                           <button onClick = {this.handleClick}>Save</button>
                       }

                      </div>
                     { messageDisplay &&
                        <div style={{textAlign:'center',margin:'5px 0'}}>
                        <span>Your run schedule is updated !!</span>
                        </div>
                     }
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
                    <a href='https://teams.microsoft.com/l/channel/19%3a83d56744389742bb8a1f544e1c4d024f%40thread.tacv2/General?groupId=22fa248e-04a6-4727-825e-5c31b8eb8234&tenantId=300f59df-78e6-436f-9b27-b64973e34f7d' target='_blank'>Checkout Teams Channel</a>
                }
            </div>
        )
    }
}

export default Challenege;

const ChallengeHead = () => {
    return(
        <div style={{color:'green',margin:'10px'}}>
            <h3 style={{margin:'0',padding:'10px 5px'}}>FitTogether</h3>
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