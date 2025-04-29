import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Navbar from "./components/Navbar";
import Home from "./components/Home";
import Categories from "./components/Categories";
import Search from "./components/Search";


import './App.css';

const App = () => {
  return (
    <div className="app">
      <Router>
        <Navbar />
        <div className="container-fluid">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/categories" element={<Categories />} />
            <Route path="/search" element={<Search />} />
          </Routes>
        </div>
      </Router>
    </div>
  );
}

export default App;
