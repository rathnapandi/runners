import React from 'react';
import {Switch,Route} from 'react-router-dom'
import './App.css';
import Header from './Component/Header/Header.Component';
import Challenge from './Pages/Challenge/Challenge.page'
import runner from './images/runner.jpeg'
const App = () => {
    return(
        <div className='div-app' style={{backgroundImage:`url(${runner})`}}>
            <Header/>
            <Switch>
            <Route exact path='/events' component={Challenge}/>
                <Route exact path='/' render={() =><h1>Hi</h1>}/>
            </Switch>
        </div>
    )
}

export default App;
