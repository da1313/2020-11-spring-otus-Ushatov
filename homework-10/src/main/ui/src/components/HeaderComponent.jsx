import '../css/HeaderComponent.css'
import { useHistory } from "react-router-dom";

function HeaderComponent() {

  let history = useHistory();

  const moveToBookList = () =>{
    history.push("/");
  }

  const moveToCreateBook = () =>{
    history.push("/new");
  }

  return (
    <header>
      <div className="headerTitle"><h3>Book CRUD UI</h3></div>
      <div className="headerNav">
        <button onClick={moveToBookList}>View books</button>
        <button onClick={moveToCreateBook}>Create new</button>
      </div>
    </header>
  );
}

export default HeaderComponent;
