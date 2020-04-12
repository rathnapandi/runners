import React from 'react';
import {Switch,Route} from 'react-router-dom'
import './App.css';
import Header2 from './Component/Header2/Header2.component';
import Footer2 from './Component/Footer2/Footer2.component';
import Challenge from './Pages/Challenge/Challenge.page';
import Iframe from './Pages/Iframe/Iframe';
import runner from './images/runner.jpeg'
const App = () => {
    return(
        <div className='div-app'>
            <Header2/>
            <Switch>
            <Route path='/iframe' component={Iframe}/>
            <Route path='/' render={({...props}) =><Challenge {...props}/>}/>
            </Switch>
            <Footer2/>
        </div>
    )
}

export default App;
