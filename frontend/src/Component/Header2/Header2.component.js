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
			<img className="div-logo" src={logo} alt="logo"/>
			</div>

			<div className="div4-Header2">
				<div className="div4-Header2-div">
                					{userName && <span>{`${userName.firstName} ${userName.lastName}`} </span>}
                					 <a href='https://teams.microsoft.com/l/channel/19%3a83d56744389742bb8a1f544e1c4d024f%40thread.tacv2/General?groupId=22fa248e-04a6-4727-825e-5c31b8eb8234&tenantId=300f59df-78e6-436f-9b27-b64973e34f7d' target='_blank'><span>|</span>Support</a>
                					 <a href='/logout'><span>|</span>Logout</a>
                				</div>

			</div>
		</div>
		<div style={{marginTop:'10px'}}>
		<NavLink className="button-folder" exact to='/' activeClassName='active-button'>
		Challenge
		</NavLink>
		<NavLink className="button-folder" exact to='/Dashboard' activeClassName='active-button'>
			Dashboard
		</NavLink>

		</div>
		</div>
	)
}

export default Header2;