import React from 'react';
import {NavLink} from 'react-router-dom'
import './header.style.css';
const Header = () => {
    return(
        <header>
            <div>
                <nav>
                    <NavLink exact activeClassName='active' to='/'>Home</NavLink>
                </nav>
                <nav>
                    <NavLink exact activeClassName='active' to='/events'>Challenge</NavLink>
                </nav>
            </div>
        </header>
    )
}

export default Header;