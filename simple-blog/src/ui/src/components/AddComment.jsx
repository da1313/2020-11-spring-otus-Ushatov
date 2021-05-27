import { Button, TextField } from "@material-ui/core";
import { useContext, useState } from "react";
import { useHistory } from "react-router";
import { AppContext } from "../App";
import { authFetch } from "../services/AuthProvider";

function AddComment({ postId, comments, setComments, postData, setPostData }) {
  let context = useContext(AppContext);

  let history = useHistory();

  const [text, setText] = useState("");

  const [isError, setError] = useState(false);

  const addComment = () => {
    if (text === "") {
      setError(true);
      return;
    }
    authFetch(`${window.location.origin}/comments`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        postId: postId,
        text: text,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          context.showAuth(true);
          history.push("/");
          //todo add redirect
          setComments({
            commentList: [],
            pageNumber: 0,
            totalPages: 0,
            hasNext: false,
          });
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if(body.result === false){
          return;
        }
        setComments({
          commentList: [body, ...comments.commentList],
          pageNumber: comments.nextPage,
          totalPages: comments.totalPages,
          hasNext: comments.hasNext,
        });
        let newPost = { ...postData.post };
        newPost.postStatistics.commentCount =
          newPost.postStatistics.commentCount + 1;
        setPostData({
          post: newPost,
          isVote: postData.isVote,
          voteValue: postData.voteValue,
        });
      })
      .catch((err) => {
        console.log(typeof err);
        console.log(err);
        history.push("/error");
      });
  };

  const onTextChange = (event) => {
    setError(false);
    setText(event.target.value);
  };

  return (
    <div>
      <div>
        <TextField
          helperText={isError ? "Comment can't be empty" : ""}
          error={isError}
          onChange={onTextChange}
          multiline
          rowsMax={5}
          variant="outlined"
          style={{ width: "100%" }}
        />
      </div>
      <div
        style={{ marginTop: "1rem", marginBottom: "1rem", textAlign: "center" }}
      >
        <Button variant="contained" color="primary" onClick={addComment}>
          Submit
        </Button>
      </div>
    </div>
  );
}

export default AddComment;
