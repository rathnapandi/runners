import React from 'react';
import {Switch,Route} from 'react-router-dom'
import Challenge from './Pages/Challenge/Challenge.page'
import Login from './Component/Login/Login.component'
class App extends React.Component{
  state = {
    currentuser:null
  }
  setCurrentuser = (name,password) => {
    const currentuser = {
      name,
      password
    }
    this.setState({currentuser})
  }
  render(){
    return(
        <div>
            <Switch>
            <Route path='/challenge' render = {() =><Challenge currentuser = {this.state.currentuser}/>}/>
              <Route path='/' render = {({...otherProps}) =><Login currentUser = {this.setCurrentuser} {...otherProps}/>}/>
            </Switch>
        </div>
      )
  }
}

export default App;
