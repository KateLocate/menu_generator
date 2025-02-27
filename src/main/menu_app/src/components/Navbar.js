import React from "react";
import { NavLink } from "react-router-dom";

import './../App.css';

function Navbar() {
  return (
  <div className='container'>
    <nav className='navbar'>
      <ul className='navList'>
        <li>
          <NavLink href="/" className='navItem'>Home</NavLink>
        </li>
        <li>
          <NavLink href="/categories" className='navItem'>Categories</NavLink>
        </li>
        <li>
          <NavLink href="/search" className='navItem'>Search</NavLink>
        </li>
      </ul>
    </nav>
  </div>
  );
}

export default Navbar;
