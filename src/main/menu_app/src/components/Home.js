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
          <div className="container-fluid">
            <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchDayRecipes}>Generate day menu</button>
          </div>
          <div className="container-fluid">
            {loading && <p>Loading...</p>}
            { dayRecipes.map(recipe =>
            <div key={recipe.id}>
                  <p className="d-inline-flex gap-1" >
                    <button className="btn btn-primary" type="button" data-bs-toggle=".collapse" data-bs-target="#collapseWidthExample" aria-expanded="false" aria-controls="collapseRecipe">{recipe.title}</button>
                  </p>
              </div>
              )
            }
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
