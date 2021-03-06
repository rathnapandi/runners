import React from 'react';
import {Switch,Route} from 'react-router-dom'
import './App.css';
import Header2 from './Component/Header2/Header2.component';

import Challenge from './Pages/Challenge/Challenge.page';
import Iframe from './Pages/Iframe/Iframe';
import Footer2 from './Component/Footer2/Footer2.component';


class App extends React.Component{
    state = {
        userName:null
    }
    handleName = (obj) =>{
        this.setState({userName:obj})
    }
    render(){
    return(
        <div className='div-app'>
            <Header2 userName={this.state.userName}/>
            <Switch>
            <Route path='/Dashboard' component={Iframe}/>

            <Route path='/' render={({...props}) =><Challenge sentName ={this.handleName} {...props}/>}/>
            </Switch>

        </div>
    )
    }
}

export default App;
