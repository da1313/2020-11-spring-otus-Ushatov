import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router";
import { AppContext } from "../App";
import ReturnBook from "./ReturnBook";
import TakeButton from "./TakeButton";
import bookService from "../service/BookService";
import loginService from "../service/LoginService";

function BookTable() {
  let init = { books: [] };

  let history = useHistory();

  const [state, setState] = useState(init);

  let context = useContext(AppContext);

  const fetchBooks = () => {

    bookService.getBooks().then((response) => {
      if (response.data.is_auth !== null && response.data.is_auth === false){
        localStorage.clear();
        history.push("/login");
        return;
      } 
      let books = response.data._embedded.books;
      setState({ books });
    });

  };

  useEffect(() => {
    fetchBooks();
  }, []);

  const takeBook = (book) => {
    bookService.takeBook(book).then((response) => {
      if (response.data.is_auth !== null && response.data.is_auth === false){
        localStorage.clear();
        history.push("/login");
        return;
      } 
      if (response.data.result) {
        fetchBooks();
      } else {
        console.log("error!");
      }
    })
  };

  const returnBook = (book) => {
    bookService.returnBook(book).then((response) => {
      if (response.data.is_auth !== null && response.data.is_auth === false){
        localStorage.clear();
        history.push("/login");
        return;
      } 
      if (response.data.result) {
        fetchBooks();
      } else {
        console.log("error!");
      }
    })
  };

  const onLogoutClick = () => {
    loginService.sendLogout();
    history.push("/login");
  };

  return (
    <div>
      <div className="header">
        <div>Ridiculously simple library simulation!(but made on HATEOAS)</div>
      </div>
      <div className={"nav"}>
        <div className={"login-info"}>
          <span>Logged in as:{context.state.user}</span>
        </div>
        <div>
          <button onClick={onLogoutClick}>log out</button>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Genre</th>
            <th>Quantity</th>
            <th>Take</th>
            <th>Return</th>
          </tr>
        </thead>
        <tbody>
          {state.books.map((b, i) => {
            return (
              <tr key={i}>
                <td>{b.title}</td>
                <td>{b.author.name}</td>
                <td>{b.genre.name}</td>
                <td>{b.quantity}</td>
                <td>
                  <TakeButton book={b} takeBook={takeBook} />
                </td>
                <td>
                  <ReturnBook book={b} returnBook={returnBook} />
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default BookTable;
