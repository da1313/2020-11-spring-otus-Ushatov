import '../css/BookComponent.css'
import { useHistory } from "react-router-dom";
import BookService from '../service/BookService';

function BookComponent({ book , onDelete }) {

  let history = useHistory();

  const moveToEditPage = () =>{
    history.push(`/new/${book.id}`);
  }

  const deleteBook = () =>{
    BookService.deleteBook(book.id).then(res =>{
      onDelete(book.id);
    })
  }

  const moveToBookView = () =>{
    history.push(`/book-view/${book.id}`)
  }

  return (
    <div className="bookCont">
      <button className="buttonLink" onClick={moveToBookView}><img src="/image"/></button>
      <div className="title">{book.title}</div>
      <ul>
        <li>Author: {book.author.name}</li>
        <li>
          Genres:{" "}
          {book.genres.map((g, i) => (
            <span key={i}>{g.name} </span>
          ))}
        </li>
        <li>Comments: {book.commentsCount === 0? "-" : book.commentsCount}</li>
        <li>Avg.Score: {book.avgScore === 0 ? "-" : Number.parseFloat(book.avgScore).toFixed(1)}</li>
      </ul>
      <div className="edit">
        <button onClick={moveToEditPage}>Edit</button>
        <button onClick={deleteBook}>Delete</button>
      </div>
    </div>
  );
}

export default BookComponent;
