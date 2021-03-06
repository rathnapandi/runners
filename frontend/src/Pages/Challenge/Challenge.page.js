/* eslint-disable react/jsx-no-target-blank */
import React from 'react';
import PlaceChoice from '../../Component/PlaceChoice/PlaceChoice.component';
import WrapperCalendar from '../../Component/WrapperCalendar/WrapperCalendar.component';
import WrapperTimePicker from '../../Component/WrapperTimePicker/WrapperTimePicker.component';
import DurationChoice from '../../Component/DurationChoice/DurationChoice.component';

import {Redirect} from 'react-router-dom';

import moment from 'moment-timezone';
import './challenge.css'
import './loader.css'
import { Button } from 'react-bootstrap';

class Challenge extends React.Component {
    state = {
        currentuser: null,
        eventInfo: null,
        selectedEvent: null,
        choice: '',
        id: '',
        prevSetTime: null,
        duration: '30',
        startTime: undefined,
        groups: [],
        items: [],
        isUpdate: false,
        pId: '',
        authToken: null,
        messageDisplay: false,
        active:false
    }

    eventCallFunc = async () => {
        let events = []
        const resp = await fetch('/api/events', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        const eventInfo = await resp.json()
        eventInfo.content.forEach(event => {
            events.push({
                id: event.id,
                name: event.name,
                description: event.description,
                startDate: Number(event.startDate),
                endDate: Number(event.endDate),
                image: event.image
            })
        })
        this.setState({eventInfo: events})
    }
    participantCallFunc = async (eventId) => {


        let groups = [], items = [];
        const { currentuser} = this.state
        const resp = await fetch(`/api/events/${eventId}/participants`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        const pList = await resp.json()
        pList.forEach(pItem => {
            groups.push({
                id: pItem.id,
                title: `${pItem.firstName} ${pItem.lastName}`
            })
            items.push({
                id: pItem.id,
                group: pItem.id,
                title: `${pItem.firstName} ${pItem.lastName}-${moment(Number(pItem.startTime)).format('h:mm a')} to ${moment(Number(pItem.endTime)).format('h:mm a')}`,
                start_time: Number(pItem.startTime),
                end_time: Number(pItem.endTime)
            })
            if (currentuser.email === pItem.email)
                this.setState({
                    isUpdate: true,
                    startTime: Number(pItem.startTime),
                    prevSetTime: {
                        startTime: Number(pItem.startTime),
                        endTime: Number(pItem.endTime)
                    },
                    duration: moment(Number(pItem.endTime)).diff(Number(pItem.startTime), 'minutes').toString(),
                    pId: pItem.id
                })
        })
        this.setState({groups, items})
    }

    async componentDidMount() {
    this.setState({active:true});
        const resp = await fetch('/api/users/currentuser', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })

        if(resp.redirected){
            console.log("session expired")
             window.location.reload();
         }

        const data = await resp.json()
        const {email, id, firstName, lastName, countryCode, oauthToken} = data
        this.setState({
            currentuser: {
                email,
                firstName,
                lastName,
                countryCode
            },
            pId: id,
            authToken: 0
        })
        this.props.sentName({firstName, lastName})
        await this.eventCallFunc()
        this.setState({active:false});
       /*setTimeout(() =>{
console.log('Hello_01');
               //    this.props.history.push('/login.html');
              // return <Redirect to='/login.html' />
             document.location.replace('/login.html');
               window.location.reload();
                },5000)
               //moment(this.state.authToken).diff(moment().valueOf(),'seconds'))*/
    }

    handleChoice = (choice) => this.setState({choice})

    handleDuration = (duration) => this.setState({duration})

    handleTime = startTime => this.setState({startTime})

    handleClick = async () => {
        const {selectedEvent, currentuser, startTime, duration} = this.state
        this.setState({active:true});
        const obj = {
            eventId: selectedEvent.id,
            eventName: selectedEvent.name,
            startTime: String(startTime),
            endTime: String(moment(Number(startTime)).add(Number(duration), 'minutes').valueOf()),
            ...currentuser
        }
        this.setState({active:true});
        try {
            const presponse = await fetch(`/api/events/${selectedEvent.id}/participants`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            })
            await this.participantCallFunc(selectedEvent.id)
        } catch (e) {
            console.log(e)
        }
    }
    handleUpdate = async () => {
        const {selectedEvent, startTime, duration, pId} = this.state
        const obj = {
            eventId: selectedEvent.id,
            eventName: selectedEvent.name,
            startTime: String(startTime),
            endTime: String(moment(Number(startTime)).add(Number(duration), 'minutes').valueOf())
        }
        try {
            await fetch(`/api/events/${selectedEvent.id}/participants/${pId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(obj)
            })
            this.setState({messageDisplay:true,active:false},() => setTimeout(() =>{
                this.setState({messageDisplay: false})
            }, 1500))
            await this.participantCallFunc(selectedEvent.id)
        } catch (e) {
            console.log(e)
        }
    }

    handleStravaDeAuthorize = async () => {

        try {
            await fetch(`/strava/deauthorize`, {
                method: 'GET'

            })
            alert("Strava is disconnected from the App")
        } catch (e) {
            console.log(e)
        }
    }
    handleRedirect = () => {
        window.open("strava/login", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,top=500,left=500,width=400,height=400");
    }
    handleEvent = async (id) => {

        this.setState(state => ({
            selectedEvent: state.eventInfo.find(event => event.id === id),
            startTime: state.eventInfo.find(event => event.id === id).startDate,
            id: state.eventInfo.find(event => event.id === id).id,
            isUpdate: false,
            choice: '',
            duration: '30',
            prevSetTime: null,
            active:true
        }));
        await this.participantCallFunc(id);
        this.setState({active:false})
    }

    render()
    {
        const {choice, eventInfo, selectedEvent, isUpdate, startTime, duration, authToken, messageDisplay, prevSetTime, id} = this.state

        if(this.state.active)
                return(
                    <div style={{display:'flex',textAlign:'center',justifyContent:'center'}}>
                        <div className="loader"></div>
                    </div>
                )
                else

        return (
        <div>
 <div className='chl-div'>
                {eventInfo && <ChallengeHead events={eventInfo} selectEvent={this.handleEvent} id={id}/>}
              <div className='challenge-div'>
                {selectedEvent && <Description description={selectedEvent.description}/>}

                {selectedEvent && <Timing startDate={selectedEvent.startDate} endDate={selectedEvent.endDate}/>}

                {selectedEvent && <PlaceChoice choiceClick={this.handleChoice} ch={choice}/>}
                {
                    choice === 'runner' &&
                    <div className='challenge-div2'>
                        {selectedEvent && <DurationChoice choiceClick={this.handleDuration} dur={duration}/>}
                        {prevSetTime && <div style={{textAlign: 'center', color: 'green', padding: '5px 0'}}>
                            <span>* Your current run Schedule is </span>
                            <span>{`${moment(prevSetTime.startTime).format('MM/D/YYYY h:mm a')} to ${moment(prevSetTime.endTime).format('h:mm a')}`}</span>
                        </div>
                        }

                        {selectedEvent && <WrapperTimePicker
                            yourChoiceTime={this.handleTime}
                            startDate={selectedEvent.startDate}
                            endDate={selectedEvent.endDate}
                            sTime={startTime}
                        />
                        }

                        <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                            {
                            <button class='btn-primary' onClick={this.handleRedirect}>Connect to Strava</button>


                            /*<button style={{
                                background: '#005e85',
                                color: 'white',
                                fontsize: '12px',
                                marginRight: '5px',
                                width: 'max-content'
                            }} onClick={this.handleRedirect}>Connect to Strava</button>*/

                            }
                            <span>&nbsp;&nbsp;</span>
                            {
                            <button class='btn-danger'
                                     onClick={this.handleStravaDeAuthorize}>Disconnect from Strava</button>
                            /* <button style={{background: '#005e85', color: 'white', fontsize: '12px',width: 'max-content'}}
                                                                  onClick={this.handleStravaDeAuthorize}>Disconnect from Strava</button>   */

                             }
                            <span>&nbsp;&nbsp;</span>
                            {
                                isUpdate ?
                                  <button class='btn-success' onClick={this.handleUpdate}>Update</button>
                                   :
                                  <button class='btn-success' onClick={this.handleClick}>Save</button>

                                  /*  <button style={{background: '#005e85', color: 'white', fontsize: '12px'}}
                                            onClick={this.handleUpdate}>Update</button>
                                    :
                                    <button style={{background: '#005e85', color: 'white', fontsize: '12px'}}
                                            onClick={this.handleClick}>Save</button>*/
                            }


                        </div>
                        {messageDisplay &&
                        <div style={{textAlign: 'center', margin: '5px 0'}}>
                            <span>Your run schedule is updated !!</span>
                        </div>
                        }
                    </div>
                }


                {
                    choice === 'cheerer' &&
                    <a href='https://teams.microsoft.com/l/channel/19%3a83d56744389742bb8a1f544e1c4d024f%40thread.tacv2/General?groupId=22fa248e-04a6-4727-825e-5c31b8eb8234&tenantId=300f59df-78e6-436f-9b27-b64973e34f7d'
                       target='_blank'>Checkout Teams Channel</a>
                }
            </div>
             </div>

              {
                                 choice === 'runner' && this.state.items.length > 0 &&
                                 <WrapperCalendar
                                     groups={this.state.groups}
                                     items={this.state.items}
                                     startDate={selectedEvent.startDate}
                                     endDate={selectedEvent.endDate}
                                 />
                             }
                             </div>

        )
    }
}

export default Challenge;

const ChallengeHead = ({events, selectEvent, id}) => {
    return (
        <div style={{display: 'flex', flexWrap: 'wrap'}}>
            {
                events.map((event, i) => {
                    return (
                        <div
                            key={i}
                            onClick={() => selectEvent(event.id)}
                            className={`${id === event.id ? "clicked" : ""}`}
                            style={{
                                color: 'green',
                                margin: '10px',
                                width: '190px',
                                boxShadow: '2px 3px 4px lightblue',
                                textAlign: 'center'
                            }}>
                            <div style={{width: '190px'}}>
                                <img src={`data:image/jpg;base64,${event.image}`} alt=''
                                     style={{height: 'auto', width: '100%'}}/>
                            </div>
                            <span>Event Start time : <span>{moment(Number(event.startDate)).format('Do MMM YYYY, HH:mm')}</span></span>
                            <h3 style={{margin: '0', padding: '10px 5px'}}>{event.name}</h3>
                        </div>
                    )
                })
            }
        </div>
    )
}


const Description = ({description}) => {
    return (
       <div style={{display:'flex',flexDirection:'column',width:'max-content'}}>
           <span><b>{description}</b></span>
        </div>
    )
}

const Timing = ({startDate, endDate}) => {
    return (
        <div style={{display:'flex',flexDirection:'row',justifyContent:'center', width:'100%',margin:'10px 0'}}>
            <span style={{fontWeight:'bold'}}>Start time: <span style={{fontWeight:'normal'}}>{moment(Number(startDate)).format('Do MMM YYYY, HH:mm')}</span></span>
            <span style={{fontWeight:'bold',marginLeft:'25px'}}>End time: <span style={{fontWeight:'normal'}}>{moment(Number(endDate)).format('Do MMM YYYY, HH:mm')}</span></span>
        </div>
    )
}