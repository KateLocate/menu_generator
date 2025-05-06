import React, {useState} from "react";

function Home() {
  const [period, setPeriod] = useState([]);
  const [menuRecipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(false);

  function fetchRecipes (days) {
    setLoading(true);
    fetch(`api/recipes/period/${days}`)
      .then(response => response.json())
      .then(data => {
        setRecipes(data);
        setLoading(false);
      })
      .catch(error => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }

  function RecipeButton ({index, recipe}) {
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

  function DisplayRecipe ({index, recipe}) {
    return (
        <div className="container mt-3">
          <p className="d-inline-flex gap-1" >
            <RecipeButton index={index} recipe={recipe} />
          </p>
          <div className="collapse" id={`collapse-${index+"-"+recipe.id}`}>
            <div className="card card-body">
              {recipe.body}
            </div>
          </div>
        </div>
    )
  }

  function DisplayMenu () {
    return (
        <div>
          {loading && <p>Loading...</p>}
          <ul>
            { menuRecipes.map((recipes, index) => {
              return (
                    <li key={index}>
                      <p>Day {index + 1}</p>
                      { recipes.map((recipe) => {
                        return (<DisplayRecipe key={`${index+"-"+recipe.id}`} index={index} recipe={recipe} />)
                        })
                      }
                    </li>
              )
            })}
          </ul>
        </div>
    )
  }

  function handleSubmit (e) {
    e.preventDefault();
    fetchRecipes(period);
  }

  return (
    <div className="container">
      <h1>Let's decide what to eat!</h1>
      <form className="row row-cols-lg-auto g-3 align-items-center" method="post" onSubmit={handleSubmit}>
        <div className="col-auto">
          <label className="col-form-label">Choose period:</label>
        </div>
        <div className="col-auto">
         <button className="btn btn-primary" type="submit" onClick={e => setPeriod(1)}>day</button>
        </div>
        <div className="col-auto">
         <button className="btn btn-primary" type="submit" onClick={e => setPeriod(7)}>week</button>
        </div>
        <div className="col-auto">
         <button className="btn btn-primary" type="submit" onClick={e => setPeriod(31)}>month</button>
        </div>
      </form>
      <div>
        <DisplayMenu />
      </div>
    </div>
  );
}

export default Home;
