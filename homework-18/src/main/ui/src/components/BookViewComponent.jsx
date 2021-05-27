import React, { useState, useEffect } from 'react';
import { useHistory, useParams } from "react-router-dom";
import '../css/BookViewComponent.css'
import BookService from '../service/BookService';
import CommentComponent from './CommentComponent';
import CreateCommentComponent from './CreateCommentComponent';
import ScoreComponent from './ScoreComponent';

function BookViewComponent(){

    let history = useHistory();

    let {id} = useParams();

    const [state, setState] = useState({});

    const getBook = () =>{
        BookService.getBookById(id).then(r1 => {
            if (r1.data.result !== null && r1.data.result === "false"){
                history.push("/error");
                return;
            }
            setState(r1.data);
        });
    }

    useEffect(() => getBook(),[]);

    return(
        <div>
            <div className="book-view-cont">
                <div className="book-image-cont">
                    <img src="/image"/>
                </div>
                <div className="book-view-details-cont">
                    <div>Title: {state.title && state.title}</div>
                    <div>Author: {state.author && state.author.name}</div>
                    <div>Genres: {state.genres && state.genres.map(g => <span key={g.id}>{g.name}&nbsp;</span>)}</div>
                    <ScoreComponent scoreCounts={state.scoreCounts === undefined ? [0,0,0,0,0] : state.scoreCounts}
                     avgScore={state.avgScore && state.avgScore}/>
                </div>
            </div>
            <CommentComponent id={id}/>
        </div>
    );
}

export default BookViewComponent;