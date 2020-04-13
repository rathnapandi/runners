import React from 'react';
import {NavLink} from 'react-router-dom'
import logo from '../../images/test_logo.PNG';
import bar from '../../images/bar_banner.png';
import './header2.style.css'
const Header2 = ({userName}) =>{
	const text1 = "If you want to go fast, go alone,"
	const text2 = "If you want to go far, go together!"
	return(
		<div>
		<div className="div-Header2">
			<div>
			<img src={logo} alt="logo" width="100px" height="auto"/>
			</div>
			<div className="div3-Header2">
			<img src={bar} alt="bar_image" width="100px" height="auto"/>
			</div>
			<div className="div4-Header2">
				<div className="div4-Header2-div">
                					{userName && <span>{`${userName.firstName} ${userName.lastName}`} </span>}
                					 <NavLink to='/'><span>|</span>Support</NavLink>
                					 <NavLink to='/'><span>|</span>Logout</NavLink>
                				</div>
				<div className="div4-Header2-div2">

					<p>{text1.toUpperCase()}</p>
					<p>{text2.toUpperCase()}</p>
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