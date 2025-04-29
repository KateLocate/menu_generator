import React from "react";
import { NavLink } from "react-router-dom";

import "./../App.css";

function Navbar() {
  return (
    <nav className="navbar sticky-top bg-body-tertiary px-3 mb-3">
      <h1 className="navbar-brand">Menu Generator</h1>
      <ul className='nav justify-content-center'>
        <li className="nav-item">
          <NavLink to="/" className="nav-link">Home</NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/categories" className="nav-link">Categories</NavLink>
        </li>
        <li className="nav-item">
          <NavLink to="/search" className="nav-link">Search</NavLink>
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;
