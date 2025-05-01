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

  function RecipeButton ({recipe}) {
    return (
        <button className="btn btn-primary"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target={`#collapse-${recipe.id}`}
              aria-expanded="false"
              aria-controls={`collapse-${recipe.id}`}>
          {recipe.title}
        </button>
    );
  }

  return (
    <div className="container">
      <div className="row align-items-start">
        <div className="col">
          <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchDayRecipes}>Generate day menu</button>
          {loading && <p>Loading...</p>}
          { dayRecipes.map(recipe =>
          <div className="container mt-3" key={recipe.id}>
            <p className="d-inline-flex gap-1" >
              <RecipeButton recipe={recipe} />
            </p>
            <div className="collapse" id={`collapse-${recipe.id}`}>
              <div className="card card-body">
                {recipe.body}
              </div>
            </div>
          </div>
          ) }
        </div>

        <div className="col">
          <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchWeekRecipes}>Generate week menu</button>
          {loading && <p>Loading...</p>}
          <ol>
          { weekRecipes.map((weekDayRecipes) => {
            return (<div><li>
              { weekDayRecipes.map((recipe) => {
                return (<div className="container mt-3" key={recipe.id}>
                      <p className="d-inline-flex gap-1" >
                        <RecipeButton recipe={recipe} />
                      </p>
                      <div className="collapse" id={`collapse-${recipe.id}`}>
                        <div className="card card-body">
                          {recipe.body}
                        </div>
                      </div>
                    </div>)
                })
              }
              </li></div>)
            })}
            </ol>
        </div>
      </div>
    </div>
  );
}

export default Home;
