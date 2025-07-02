import React, {useState} from "react";

import "./App.css";

const App = () => {
  const [menuRecipes, setRecipes] = useState([]);
  const [loading, setLoading] = useState(false);

  function fetchRecipes (days, types) {
    setLoading(true);
    const params = new URLSearchParams(types);
    fetch(`api/recipes/menu/${days}/types?${params}`)
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
          <ul className="list-group list-group-flush p-3">
            { menuRecipes.map((recipes, index) => {
              return (
                    <li key={index} className="list-group-item">
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


  function MenuForm () {
    const [period, setPeriod] = useState([]);
    const [types, setTypes] = useState({
      breakfast: false,
      snack: false,
      dinner: false,
      dessert: false,
    });

    function handleSubmit (e) {
      e.preventDefault();
      fetchRecipes(period, types);
    }

    return (
      <div>
        <form className="row row-cols-lg-auto g-3 align-items-center" method="post" onSubmit={handleSubmit}>
          <div className="col-auto">
            <label className="col-form-label">Choose types and period:</label>
          </div>
          <div className="form-check">
            <input className="form-check-input" type="checkbox" id="breakfast" value={types.breakfast}
              onChange={e => { setTypes({...types, breakfast: !types.breakfast}); }} />
            <label className="form-check-label">
              breakfast
            </label>
          </div>
          <div className="form-check">
            <input className="form-check-input" type="checkbox" id="snack" value={types.snack}
              onChange={e => { setTypes({...types, snack: !types.snack}); }} />
            <label className="form-check-label">
              snack
            </label>
          </div>
          <div className="form-check">
            <input className="form-check-input" type="checkbox" id="dinner" value={types.dinner}
              onChange={e => { setTypes({...types, dinner: !types.dinner}); }} />
            <label className="form-check-label">
              dinner
            </label>
          </div>
          <div className="form-check">
            <input className="form-check-input" type="checkbox" id="dessert" value={types.dessert}
              onChange={e => { setTypes({...types, dessert: !types.dessert}); }} />
            <label className="form-check-label">
              dessert
            </label>
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
      </div>
    )
  }

  return (
      <div className="app">
        <div className="text-bg-primary p-3 mb-4">
          <h1 className="display-6">Menu Generator</h1>
        </div>
        <div className="container-fluid p-3 mb-4">
          <h1 className="h3 mb-4">Let's decide what to eat!</h1>
          <MenuForm />
          <div>
            <DisplayMenu />
          </div>
        </div>
      </div>

  );
}

export default App;
