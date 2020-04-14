import React from 'react';
import {NavLink} from 'react-router-dom'
import logo from '../../images/test_logo.PNG';
import bar from '../../images/bar_banner.png';
import './header2.style.css'
const Header2 = ({userName}) =>{
	return(
		<div>
		<div className="div-Header2">
			<div>
			<img src={logo} alt="logo" width="250px" height="150px"/>
			</div>

			<div className="div4-Header2">
				<div className="div4-Header2-div">
                					{userName && <span>{`${userName.firstName} ${userName.lastName}`} </span>}
                					 <NavLink to='/'><span>|</span>Support</NavLink>
                					 <NavLink to='/'><span>|</span>Logout</NavLink>
                				</div>

			</div>
		</div>
		<div style={{marginTop:'10px'}}>
		<NavLink className="button-folder" exact to='/' activeClassName='active-button'>
		Challenge
		</NavLink>
		<NavLink className="button-folder" exact to='/iframe' activeClassName='active-button'>
			Dashboard
		</NavLink>
		</div>
		</div>
	)
}

export default Header2;