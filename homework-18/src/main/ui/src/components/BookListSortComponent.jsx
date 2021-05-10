import React, { useState, useEffect } from 'react'; 
import { useHistory } from 'react-router';
import genreService from '../service/GenreService';

function BookListSortComponent({onGenreChange, onSortChange}){

  let history = useHistory();

  const [genres, setState] = useState([]);

  const fetchGenres = () =>{
    genreService.getGenres().then(res => {
      if (res.data.result !== null && res.data.result === "false"){
        history.push("/error");
        return;
      }
      setState(res.data);
    })
  }

  
  useEffect(() => fetchGenres(), []);

  return(
    <div className="bookListNavSort">
      <ul>
        <li>
          <select onChange={(e) => {
              onGenreChange(e.target.value)
            }}>
            <option value="null">All</option>
            {genres.map(g =>{
              return <option value={g.id} key={g.id}>{g.name}</option>
            })}
          </select>
        </li>
        <li><button onClick={()=>onSortChange("NEW")}>New</button></li>
        <li><button onClick={()=>onSortChange("BEST")}>Best</button></li>
        <li><button onClick={()=>onSortChange("POPULAR")}>Popular</button></li>
      </ul>
    </div>
  );
}

export default BookListSortComponent;