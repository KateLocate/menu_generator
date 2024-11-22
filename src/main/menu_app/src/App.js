import React, { useEffect, useState } from 'react';

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

  function Recipe({recipe, visible}) {
    if (visible) {
      return <p>{recipe.body}</p>;
    }
    return null;
  }

  function RecipeButton({recipe}) {
    const [visible, setVisible] = useState(false);

    function handleRecipeButtonClick() {
      setVisible(!visible);
      console.log(visible)
    }

    return (
      <div>
        <button className='recipeDetails' onClick={handleRecipeButtonClick}>
          {recipe.title}
        </button>
        <p>
          <Recipe visible={visible} recipe={recipe}/>
        </p>
      </div>
    );
  }



  return (
    <div className="app">
      <h1>Menu Generator</h1>
      <div className='container'>
        { recipes.map(recipe =>
          <div className='recipe' key={recipe.id}>
            <RecipeButton recipe={recipe} />
          </div>
        )};
      </div>
    </div>
  );
}

export default App;
