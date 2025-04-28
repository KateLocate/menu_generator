import React, {useState} from "react";

function Home() {
  const [dayRecipes, setDayRecipes] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchDayRecipes = () => {
      setLoading(true);
      fetch('api/recipes/day')
        .then(response => response.json())
        .then(data => {
          setDayRecipes(data);
          setLoading(false);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          setLoading(false);
        });
    };


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
    <div>
      <div className="app">
        <div className='container'>
          <button className='large'>Generate menu!</button>
          { recipes.map(recipe =>
            <div className='recipe' key={recipe.id}>
              <RecipeButton recipe={recipe} />
            </div>
          )};
        </div>
      </div>
    </div>
  );
}

export default Home;
