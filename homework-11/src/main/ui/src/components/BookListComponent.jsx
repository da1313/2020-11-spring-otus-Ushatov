import React, { useState, useEffect } from 'react'; 
import bookService from '../service/BookService';
import Book from './BookComponent';
import PagingComponent from './PagingComponent';
import '../css/BookListComponent.css'
import BookListSortComponent from './BookListSortComponent';
import BookListSearchComponent from './BookListSearchComponent';

function BookListComponent(){

  const pageSize = 8;

  let stateDifinition = {
    books: [],
    currentPage: 0,
    maxPages: 0,
    genreId: null,
    sort: "NEW",
    qyery: null
  }

  const [state, setState] = useState(stateDifinition);

  const onPageChange = (pageNumber) =>{
    if(state.query != null){
      setBooksBySearch(state.query, pageNumber, state.sort);
    } else if(state.genreId != null){
      setBooksByGenre(state.genreId, pageNumber, state.sort);
    } else{
      setBooks(pageNumber, state.sort);
    }
  }

  const onGenreChange = (genreId) =>{
    if(genreId == "null"){
      setBooks(0, "NEW");
    } else{
      setBooksByGenre(genreId, 0, "NEW");
    }
  }

  const onSortChange = (sort) =>{
    if(state.query != null){
      setBooksBySearch(state.query, state.currentPage, sort);
    } else if(state.genreId != null){
      setBooksByGenre(state.genreId, state.currentPage, sort);
    } else{
      setBooks(state.currentPage, sort);
    }
  }

  const onSearch = (e) =>{
    if(e) e.preventDefault();
    const query = e.target[0].value;
    if(query){
      setBooksBySearch(query, state.currentPage, state.sort);
    }
  }

  useEffect(() => {
    onPageChange(0);
  }, []);

  const setBooksBySearch = (query, pageNumber, sort) =>{
    let req = {
      pageNumber: pageNumber,
      pageSize: pageSize,
      sort: sort,
      query: query
    }
    bookService.getBooksBySearch(req).then(res=>{
      let newState = {
        books: res.data.bookList,
        currentPage: pageNumber,
        maxPages: res.data.totalPages,
        genreId: null,
        sort: sort,
        query: query
      }
      setState(newState);
    })
  }

  const setBooksByGenre = (genreId, pageNumber, sort) =>{
    let req = {
      pageNumber: pageNumber,
      pageSize: pageSize,
      sort: sort,
      genreId: genreId
    }
    bookService.getBookByGenre(req).then(res=>{
      let newState = {
        books: res.data.bookList,
        currentPage: pageNumber,
        maxPages: res.data.totalPages,
        genreId: genreId,
        sort: sort,
        query: null
      }
      setState(newState);
    })
  }

  const setBooks = (pageNumber, sort) =>{
    let req = {
      pageNumber: pageNumber,
      pageSize: pageSize,
      sort: sort
    }
    bookService.getBooks(req).then(res=>{
      let newState = {
        books: res.data.bookList,
        currentPage: pageNumber,
        maxPages: res.data.totalPages,
        genreId: null,
        sort: sort,
        query: null
      }
      setState(newState);
    })
  }

  const onDelete = (bookId) =>{
    let newState = {
      books: state.books.filter((b) => b.id != bookId),
      currentPage: state.currentPage,
      maxPages: state.maxPages,
      genreId: state.genreId,
      sort: state.sort,
      qyery: state.qyery
    }
    setState(newState);
  }

  return (
    <div className="bookListMainCont">
      <nav className="bookListNav">
        <BookListSortComponent onGenreChange={onGenreChange} onSortChange={onSortChange}/>
        <BookListSearchComponent onSearch={onSearch}/>
      </nav>
      <div className="bookListCont">
        {state.books.map(book => <Book key={book.id} book={book} onDelete={onDelete}/>)}
      </div>
      <PagingComponent onPageChange={onPageChange} pageParams={{currentPage: state.currentPage, maxPages: state.maxPages}}/>
    </div>
  );
}

export default BookListComponent;