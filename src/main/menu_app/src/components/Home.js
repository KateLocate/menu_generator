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

  return (
    <div className='container'>
      <div className="container-fluid">
        <button type="button" className="btn btn-outline-primary btn-lg" onClick={fetchDayRecipes}>Generate day menu</button>
      </div>

      {loading && <p>Loading...</p>}
      { dayRecipes.map(recipe =>
      <div className="container mt-3" key={recipe.id}>
        <p className="d-inline-flex gap-1" >
          <button className="btn btn-primary"
                  type="button"
                  data-bs-toggle="collapse"
                  data-bs-target={`#collapse-${recipe.id}`}
                  aria-expanded="false"
                  aria-controls={`collapse-${recipe.id}`}>
            {recipe.title}
          </button>
        </p>
        <div className="collapse" id={`collapse-${recipe.id}`}>
          <div className="card card-body">
            {recipe.body}
          </div>
        </div>
      </div>
      ) }
    </div>
  );
}

export default Home;
