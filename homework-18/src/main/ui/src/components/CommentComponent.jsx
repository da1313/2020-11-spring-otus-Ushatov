import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router';
import CommentService from '../service/CommentService';
import CreateCommentComponent from './CreateCommentComponent';

function CommentComponent( {id} ){

    let history = useHistory();

    const pageSize = 3;

    const initComments = {
        comments: [],
        totalPages: 0,
        currentPage: 0
    }

    const[comments, setComments] = useState(initComments);

    const getComments = (page) =>{
        if(!id){
            return;
        }
        let req = {
            id: id,
            pageSize: pageSize,
            pageNumber: page
        }
        CommentService.getComments(req).then(r => {
            if (r.data.result !== null && r.data.result === "false"){
                history.push("/error");
                return;
            }
            let updatedComments;
            if(page <= comments.currentPage){
                updatedComments = r.data.commentList;
            } else{
                updatedComments = comments.comments;
                r.data.commentList.forEach(c => updatedComments.push(c));
            }
            setComments({
                comments: updatedComments,
                totalPages: r.data.totalPages,
                currentPage: page}
            )
        });
    }

    const loadMorePages = () =>{
        if(comments.currentPage < comments.totalPages - 1){
            getComments(comments.currentPage + 1);
        }
    }

    const renderMoreButton = (count, page, total) =>{
        if(count === 0){
            return <div>There are no comments</div>;
        } else if(page === total - 1){
            return <div></div>
        } else{
            return <div><button onClick={loadMorePages}>more</button></div>;
        }
    }

    const onCreateComment = (text) =>{
        CommentService.createComment({ id: id, text: text}).then(r =>getComments(0));
    }

    useEffect(() => getComments(0), []);

    return(
        <div className="comment-cont">
            {comments.comments.map(c =>(
                <div className="comment" key={c.id}>
                    <div className="author-cont">
                        <div className="ava-cont">
                            <img src="/image"/>
                        </div>
                        <div>{c.username}</div>
                    </div>
                    <div>
                        <textarea value={c.text} readOnly={true}></textarea>
                    </div>
                </div>
            ))}
            {renderMoreButton(comments.comments.length, comments.currentPage, comments.totalPages)}
            <CreateCommentComponent onCreateComment={onCreateComment} />
        </div>
    );
}

export default CommentComponent;