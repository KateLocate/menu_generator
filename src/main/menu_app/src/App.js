import React, { useEffect, useState } from 'react';
import logo from './logo.svg';
import './App.css';

const App = () => {

  const [recipes, setGroups] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetch('api/recipes')
      .then(response => response.json())
      .then(data => {
        setGroups(data);
        setLoading(false);
      })
  }, []);

  if (loading) {
    return <p>Loading...</p>;
  }

  function Recipe({ recipe, visible }) {
    if (visible) {
      return <p>{ recipe.body }</p>;
    }
    return null;
  }

  function RecipeButton({ recipe }) {
    const [visible, setVisible] = useState(false);

    function handleRecipeButtonClick() {
      setVisible(!visible);
      console.log(visible)
    }

    return (
      <div >
        <button onClick={handleRecipeButtonClick}>
          { recipe.title }
        </button>
        <p style={{fontSize:"0.5em"}}>
          <Recipe visible={ visible } recipe={ recipe }/>
        </p>
      </div>
    );
  }



  return (
    <div className="App">
      <header className="App-header">
        <div className="App-intro">
          <h2>Menu Generator</h2>
          { recipes.map(recipe =>
            <div key={ recipe.id }>
              <RecipeButton recipe={ recipe } />
            </div>
          ) }
        </div>
      </header>
    </div>
  );
}

export default App;
