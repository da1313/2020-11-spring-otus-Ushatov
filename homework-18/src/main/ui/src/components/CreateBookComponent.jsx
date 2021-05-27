import React, { useState, useEffect } from 'react';
import { useHistory } from "react-router-dom";
import { useParams } from 'react-router-dom';
import '../css/CreateBookComponent.css'
import AuthorService from '../service/AuthorService';
import BookService from '../service/BookService';
import GenreService from '../service/GenreService';

function CreateBookComponent(){

    let history = useHistory();

    let { id } = useParams();

    const stateDifinition = {
        authors: [],
        genres:[],
        book:{
            id: id,
            title: "",
            authorId: "",
            genresId: []
        }
    }

    const [state, setState] = useState(stateDifinition);

    const fetchData = () =>{
        console.log(state);
        AuthorService.getAuthors().then(r1 =>{
            if (r1.data.result !== null && r1.data.result === "false"){
                history.push("/error");
                return;
            }
            GenreService.getGenres().then(r2 =>{
                if (r2.data.result !== null && r2.data.result === "false"){
                    history.push("/error");
                    return;
                }
                if(id){
                    BookService.getBookById(id).then(r3 =>{
                        if (r3.data.result !== null && r3.data.result === "false"){
                            history.push("/error");
                            return;
                        }
                        let book = {
                            id: r3.data.id,
                            title: r3.data.title,
                            authorId: r3.data.author.id,
                            genresId: r3.data.genres.map(g => g.id)
                        }
                        setState({authors: r1.data, genres: r2.data, book: book});
                    });
                }
                let book = {
                    id: state.book.id,
                    title: state.book.title,
                    authorId: r1.data[0].id,
                    genresId: state.book.genresId
                }
                setState({authors: r1.data, genres: r2.data, book: book});
            })
        })
    }

    const onTitleChangeHandler = (e) =>{
        let book = state.book;
        book.title = e.target.value;
        setState({authors: state.authors, genres: state.genres, book: book});
    }

    const onAuthorChangeHandler = (e) =>{
        let book = state.book;
        book.authorId = e.target.value;
        console.log(book.authorId);
        setState({authors: state.authors, genres: state.genres, book: book});
    }

    const onGenreChangeHandler = (e) =>{
        let book = state.book;
        let genreId = e.target.value;
        let isChecked = e.target.checked;
        if (isChecked === true){
            book.genresId.push(e.target.value);
        } else{
            book.genresId = book.genres.filter(i => i !== genreId);
        }
        setState({authors: state.authors, genres: state.genres, book: book});
    }

    const saveBook = (e) =>{
        e.preventDefault();
        if(state.book.id){
            BookService.updateBook(state.book).then(() => history.push("/"));
        } else{
            BookService.createBook(state.book).then(() => history.push("/"));
        }
    }

    const moveToBookList = (e) =>{
        e.preventDefault();
        history.push("/");
    }

    useEffect(()=>fetchData(), []);

    return(
        <div className="formCont">
            <h3>Create book</h3>
            <form>
                <div className="formGroup">
                    <label>Title</label>
                    <input placeholder="Title" type="text" onChange={onTitleChangeHandler} value={state.book.title}/>
                </div>
                <div className="formGroup">
                    <label>Author</label>
                    <select onChange={onAuthorChangeHandler} value={state.book.authorId}>
                        {state.authors.map(a =><option key={a.id} value={a.id}>{a.name}</option>)}
                    </select>
                </div>
                <div className="formCheckbox">
                    <label>Genre</label>
                    {state.genres.map(g =>{
                        return(
                            <div key={g.id}>
                                <input type="checkbox" value={g.id} checked={state.book.genresId.includes(g.id)} onChange={onGenreChangeHandler} />
                                <span>{g.name}</span>
                            </div>)
                })}
                </div>
                <div className="formGroupAccept">
                    <button onClick={e => saveBook(e)}>Save</button>
                    <button onClick={e => moveToBookList(e)}>Cancel</button>
                </div>
            </form>
        </div>
    );
}

export default CreateBookComponent;