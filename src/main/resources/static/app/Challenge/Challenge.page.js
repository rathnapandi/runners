import React from 'react';
import PlaceChoice from '../Component/PlaceChoice/PlaceChoice.component';
import WrapperCalendar from '../Component/WrapperCalendar/WrapperCalendar.component';
import WrapperTimePicker from '../Component/WrapperTimePicker/WrapperTimePicker.component';
import DurationChoice from '../Component/DurationChoice/DurationChoice.component'
import {database} from '../firebase/firebase.utils'
import moment from 'moment-timezone';
class Challenege extends React.Component {
    state = {
        choice:'',
        duration:'',
        timeslot:1586357308722,
        groups:[],
        items:[]
    }

    utilsFunc = async () => {
        var groups = [],items = []
        await database.ref('/CHALLENGE')
        .on('value',async snap =>{
            const userKey = Object.keys(snap.val())
            userKey.forEach((key,id) =>{
                groups.push({
                    id,
                    title:snap.val()[key].name
                })
                items.push({
                    id,
                    group:id,
                    title:'Challenge Time',
                    start_time:snap.val()[key].timeslot,
                    end_time:moment(snap.val()[key].timeslot).add(Number(snap.val()[key].duration),'minutes').valueOf()
                })
            })
        })
        this.setState({groups,items})
    }
    async componentDidMount(){
        await this.utilsFunc()
    }
    handleChoice = (choice) => this.setState({choice})
    handleDuration = (duration) => this.setState({duration})
    handleTime = timeslot => this.setState({timeslot})
    handleClick = async() =>{
        const region = moment.tz.guess()
        const {currentuser:{name}} = this.props
        const {duration,timeslot} = this.state
        const obj = {
            name,
            timeslot,
            duration,
            region
        }
        await database.ref(`CHALLENGE/${name}`).set(obj)
        await this.utilsFunc()
    }
    render(){
        const {choice} = this.state
        return(
            <div>
                <ChallengeHead/>
                <Description/>
                <PlaceChoice choiceClick = {this.handleChoice}/>
                {
                    choice === 'runner' &&
                    <div>
                        <DurationChoice choiceClick = {this.handleDuration}/>
                        <WrapperTimePicker yourChoiceTime = {this.handleTime}/>
                        <button onClick = {this.handleClick}>Save</button>
                        { this.state.items.length > 0 && <WrapperCalendar 
                                                            groups ={this.state.groups}
                                                            items = {this.state.items}
                                                        />}
                    </div>
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

        </div>
    )
}

const Description = () => {
    return(
        <div>

        </div>
    )
}