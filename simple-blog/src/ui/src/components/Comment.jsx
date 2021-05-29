import ThumbUpIcon from "@material-ui/icons/ThumbUp";
import ThumbDownIcon from "@material-ui/icons/ThumbDown";

import { Avatar, Button, IconButton, TextField } from "@material-ui/core";
import { useContext, useState } from "react";
import { AppContext } from "../App";
import { authFetch, getUserId, logout } from "../services/AuthProvider";
import { useHistory } from "react-router";

function Comment({ comment, isAuth }) {

  let context = useContext(AppContext);

  let style = isAuth ? {} : { cursor: "not-allowed", pointerEvents: "none" };

  let userId = getUserId();

  let history = useHistory();

  const onLike = () => {};

  const [commentText, setCommentText] = useState(comment.text);

  const [edit, setEdit] = useState({ isShow: false, text: "", error: false });

  const onOpenEdit = () => {
    setEdit({ isShow: !edit.isShow, text: commentText, error: false });
  };

  const onEditFieldChange = (e) => {
    setEdit({ isShow: edit.isShow, text: e.target.value, error: false });
  };

  const onEditSubmit = () => {
    if (edit.text === "") {
      setEdit({ isShow: edit.isShow, text: edit.text, error: true });
    }
    authFetch(`${window.location.origin}/comments/${comment.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        postId: comment.postId,
        text: edit.text,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          return { result: false };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if (body.result === true) {
          setCommentText(edit.text);
          setEdit({ isShow: false, text: "", error: false });
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onDislike = () => {};

  return (
    <div style={{ display: "flex", flexDirection: "row", marginTop: "0.5rem" }}>
      <Avatar src={comment.user.avatarUrl} />
      <div style={{ width: "100%" }}>
        <div style={{ marginLeft: "0.25rem" }}>
          <span
            style={{ fontWeight: 600 }}
          >{`${comment.user.firstName} ${comment.user.lastName}`}</span>
        </div>
        {comment.isEnabled === true ? (
          <div>
            <div style={{ marginLeft: "0.5rem" }}>{commentText}</div>
            <div style={{ display: "flex", flexDirection: "row" }}>
              <div style={{ display: "flex", flexDirection: "row" }}>
                <div
                  style={{
                    display: "flex",
                    flexDirection: "row",
                    marginRight: "0.5rem",
                  }}
                >
                  <div style={{ marginRight: "0.25rem" }}>
                    <IconButton color="primary" onClick={onLike} style={style}>
                      <ThumbUpIcon />
                    </IconButton>
                  </div>
                  <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                    {comment.likes}
                  </div>
                </div>
                <div
                  style={{
                    display: "flex",
                    flexDirection: "row",
                    marginRight: "0.5rem",
                    alignItems: "center",
                  }}
                >
                  <div style={{ marginRight: "0.25rem" }}>
                    <IconButton
                      color="primary"
                      onClick={onDislike}
                      style={style}
                    >
                      <ThumbDownIcon />
                    </IconButton>
                  </div>
                  <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                    {comment.dislikes}
                  </div>
                </div>
                {isAuth && comment.user.id === userId ? (
                  <div style={{ alignSelf: "center" }}>
                    <Button
                      variant="outlined"
                      onClick={onOpenEdit}
                      style={{ marginRight: "1rem" }}
                    >
                      Edit
                    </Button>
                    {edit.isShow ? (
                      <Button
                        variant="outlined"
                        color="primary"
                        onClick={onEditSubmit}
                      >
                        Submit
                      </Button>
                    ) : (
                      <div></div>
                    )}
                  </div>
                ) : (
                  <div></div>
                )}
              </div>
            </div>
            {edit.isShow ? (
              <div>
                <TextField
                  onChange={onEditFieldChange}
                  helperText={edit.error ? "Comment must not be empty" : ""}
                  error={edit.error}
                  value={edit.text}
                  multiline
                  rowsMax={5}
                  variant="outlined"
                  style={{ width: "100%" }}
                />
              </div>
            ) : (
              <div></div>
            )}
          </div>
        ) : (
          <div style={{ marginTop: "1rem", marginBottom: "1rem" }}>
            <span style={{ color: "rgb(150,0,0)" }}>
              The comment was blocked
            </span>
          </div>
        )}
      </div>
    </div>
  );
}

export default Comment;
