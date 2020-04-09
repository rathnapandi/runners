import React,{useState} from 'react';
import {NavLink} from 'react-router-dom';
import FormInput from '../FormInput/FormInput';
import './Login.style.css';
const Login = ({currentUser,match,location,history,...otherProps}) => {
	const [userInfo,setUser] = useState({username:'',password:''});
	const {username,password} = userInfo;
	const handleChange=(evt)=>{
		const {name,value}=evt.target;
		setUser({...userInfo,[name]:value});
	}
	const handleSubmit = (evt)=>{
		console.log(username,password)
		evt.preventDefault();
		currentUser(username,password);
        setUser({...userInfo,username:'',password:''});
        history.push('/challenge')
	}
	return(
		<div style={{width:"100%",minWidth:"400px"}}>
			<div className="div-login">
				<form onSubmit = {handleSubmit} className="form-login">
				<h2> Log in</h2>
				<FormInput 
					name="username" 
					type="text" 
					value={username}  
					handleChange={handleChange}
					placeholder="Email address or username"
					required
					/>
				<FormInput 
					name="password" 
					type="password" 
					value={password} 
					onChange={handleChange}
					placeholder="Password"
					required/>
				<input type="checkbox" value="Remember Me"/><span className="span-Login">Remember me</span>
				<button className="btn btn-primary">Log in</button>
				</form>
				<div className="div-Login-Navlink">
				<NavLink to='/support'>Need help signing in?</NavLink>
				</div>
				<div style={{border:"1px solid lightgray"}}/>
				<div className="div-span-NavLink">
					<span>Don't have an account?</span>
					<NavLink to='/signup'>Sign Up</NavLink>
				</div>
			</div>
		</div>
	)
}
export default Login;