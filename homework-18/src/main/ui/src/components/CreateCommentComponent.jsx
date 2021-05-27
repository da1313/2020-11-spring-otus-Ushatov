import React, { useState } from 'react';

function CreateCommentComponent( { onCreateComment } ){

    const[text, setText] = useState("");

    const changeTextHandler = (e) =>{
        let text = e.target.value;
        setText(text);
    }

    return(
        <div className="add-comment">
            <div>Add new...</div>
            <div>
                <textarea onChange={(e) =>changeTextHandler(e)}></textarea>
            </div>
            <div><button onClick={() => onCreateComment(text)}>Add</button></div>
        </div>
    );
}

export default CreateCommentComponent;