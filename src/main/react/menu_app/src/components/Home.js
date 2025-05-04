import React, {useState} from "react";

function Home() {
  const [dayRecipes, setDayRecipes] = useState([]);
  const [weekRecipes, setWeekRecipes] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchDayRecipes = () => {
    setLoading(true);
    fetch("api/recipes/day")
      .then(response => response.json())
      .then(data => {
        setDayRecipes(data);
        setLoading(false);
      })
      .catch(error => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }

  const fetchWeekRecipes = () => {
    setLoading(true);
    fetch("api/recipes/week")
      .then(response => response.json())
      .then(data => {
        setWeekRecipes(data);
        setLoading(false);
      })
      .catch(error => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }

  function RecipeButton ({index="n", recipe}) {
    return (
        <button className="btn btn-primary"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target={`#collapse-${index+"-"+recipe.id}`}
              aria-expanded="false"
              aria-controls={`collapse-${index+"-"+recipe.id}`}>
          {recipe.title}
        </button>
    );
  }

  function DisplayRecipe ({index="n", recipe}) {
    return (
        <div className="container mt-3" key={`${index+"-"+recipe.id}`}>
          <p className="d-inline-flex gap-1" >
            <RecipeButton recipe={recipe} index={index} />
          </p>
          <div className="collapse" id={`collapse-${index+"-"+recipe.id}`}>
            <div className="card card-body">
              {recipe.body}
              {console.log(index+" "+recipe.id)}
            </div>
          </div>
        </div>
    )
  }

  function DisplayDayRecipes ({index="n", recipes}) {
    return (
        <div key={index}>
          <li>
            { recipes.map((recipe) => {
              return (<DisplayRecipe index={index} recipe={recipe} />)
              })
            }
          </li>
        </div>
    )
  }

  function DisplayMenu() {
    return (
        <div>
          {loading && <p>Loading...</p>}
          <ol>
            { weekRecipes.map((dayRecipes, index) => {
              return (
                  <DisplayDayRecipes index={index} recipes={dayRecipes} />
              )
            })}
          </ol>
        </div>
    )
  }


  return (
    <div className="container">
      <div className="row align-items-start">
        <div className="col">
          <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchDayRecipes}>Generate day menu</button>
          {loading && <p>Loading...</p>}
          { dayRecipes.map(recipe => {
              return(
                  <DisplayRecipe recipe={recipe} />
              ) }
          ) }
        </div>

        <div className="col">
          <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchWeekRecipes}>Generate week menu</button>
          <DisplayMenu />
        </div>
      </div>
    </div>
  );
}

export default Home;
