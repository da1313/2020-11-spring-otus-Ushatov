import ThumbUpIcon from "@material-ui/icons/ThumbUp";
import ThumbDownIcon from "@material-ui/icons/ThumbDown";
import VisibilityIcon from "@material-ui/icons/Visibility";
import ChatBubbleOutlineIcon from "@material-ui/icons/ChatBubbleOutline";

import { useContext, useEffect, useState } from "react";
import InfiniteScroll from "react-infinite-scroll-component";
import { useHistory, useParams } from "react-router";
import { authFetch, useAuth } from "../services/AuthProvider";
import Comment from "./Comment";
import { Avatar, CircularProgress, IconButton } from "@material-ui/core";
import { formatDate } from "../services/utils";
import AddComment from "./AddComment";
import { AppContext } from "../App";
import { logout } from "../services/AuthProvider____";

function Post() {
  let context = useContext(AppContext);

  let { id } = useParams();

  let [isAuth] = useAuth();

  let history = useHistory();

  const [postData, setPostData] = useState({
    post: {
      postStatistics: {},
      user: { firstName: "", lastName: "" },
    },
  });

  const [comments, setComments] = useState({
    commentList: [],
    pageNumber: -1,
    totalPages: 2,
    hasNext: true,
  });

  let voteStyle = (value) => {
    if (isAuth) {
      if (postData.isVote === true) {
        if (value) {
          return postData.voteValue
            ? {
                cursor: "not-allowed",
                pointerEvents: "none",
                color: "rgb(15,148,15)",
              }
            : {};
        } else {
          return postData.voteValue
            ? {}
            : {
                cursor: "not-allowed",
                pointerEvents: "none",
                color: "rgb(15,148,15)",
              };
        }
      } else {
        return {};
      }
    } else {
      return {
        cursor: "not-allowed",
        pointerEvents: "none",
      };
    }
  };

  const fetchPost = () => {
    let url = new URL(`${window.location.origin}/posts/${id}`);
    authFetch(url)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          //todo add redirect
          setPostData({});
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((result) => setPostData(result))
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const fetchComments = () => {
    let nextPage = comments.pageNumber + 1;
    let hasNext = !(nextPage === comments.totalPages);
    let url = new URL(`${window.location.origin}/comments`);
    let params = {
      pageNumber: nextPage,
      pageSize: 12,
      sort: "id",
      postId: id,
    };
    Object.keys(params).forEach((key) =>
      url.searchParams.append(key, params[key])
    );
    authFetch(url)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          //todo add redirect
          return {
            commentList: [],
            pageNumber: 0,
            totalPages: 0,
            hasNext: false,
          };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        setComments({
          commentList: [...comments.commentList, ...body.commentList],
          pageNumber: nextPage,
          totalPages: body.totalPages,
          hasNext: hasNext,
        });
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onLike = () => {
    authFetch(`${window.location.origin}/votes`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        postId: postData.post.id,
        value: true,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          //todo add redirect
          return { result: false };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if (body.result === true) {
          let newPost = { ...postData.post };
          newPost.postStatistics.likeCount =
            newPost.postStatistics.likeCount + 1;
          newPost.postStatistics.dislikeCount =
            newPost.postStatistics.dislikeCount - 1;
          setPostData({
            post: newPost,
            isVote: true,
            voteValue: true,
          });
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onDislike = () => {
    authFetch(`${window.location.origin}/votes`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        postId: postData.post.id,
        value: false,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          //todo add redirect
          return { result: false };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if (body.result === true) {
          let newPost = { ...postData.post };
          newPost.postStatistics.likeCount =
            newPost.postStatistics.likeCount - 1;
          newPost.postStatistics.dislikeCount =
            newPost.postStatistics.dislikeCount + 1;
          setPostData({
            post: newPost,
            isVote: true,
            voteValue: false,
          });
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  useEffect(() => fetchPost(), []);
  useEffect(() => fetchComments(), []);

  return (
    <div>
      <div id="comment-c" style={{ height: "100vh", overflowY: "auto" }}>
        <InfiniteScroll
          dataLength={comments.commentList.length}
          next={fetchComments}
          hasMore={comments.hasNext}
          loader={<CircularProgress />}
          endMessage={
            <div style={{ width: "100%" }}>
              <span>No moaaar!!</span>
            </div>
          }
          scrollableTarget="comment-c"
          style={{ margin: "1rem" }}
        >
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
            }}
          >
            <div style={{ marginRight: "0.25rem" }}>
              <Avatar src={postData.post.avatarUrl} />
            </div>
            <div
              style={{ marginRight: "0.25rem" }}
            >{`${postData.post.user.firstName} ${postData.post.user.lastName}`}</div>
            <div style={{ marginRight: "0.25rem" }}>
              <div>{formatDate(postData.post.publicationTime)}</div>
            </div>
          </div>
          <div>
            <h1>{postData.post.title}</h1>
          </div>
          <div
            style={{ marginBottom: "1rem" }}
            dangerouslySetInnerHTML={{ __html: postData.post.text }}
          ></div>
          <div>
            <div
              style={{ display: "flex", flexDirection: "row", margin: "1rem" }}
            >
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  marginRight: "0.5rem",
                }}
              >
                <div style={{ marginRight: "0.25rem" }}>
                  <IconButton
                    color="primary"
                    onClick={onLike}
                    style={voteStyle(true)}
                  >
                    <ThumbUpIcon />
                  </IconButton>
                </div>
                <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                  {postData.post.postStatistics.likeCount}
                </div>
              </div>
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  marginRight: "0.5rem",
                }}
              >
                <div style={{ marginRight: "0.25rem" }}>
                  <IconButton
                    color="primary"
                    onClick={onDislike}
                    style={voteStyle(false)}
                  >
                    <ThumbDownIcon />
                  </IconButton>
                </div>
                <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                  {postData.post.postStatistics.dislikeCount}
                </div>
              </div>
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  marginRight: "0.5rem",
                }}
              >
                <div
                  style={{
                    marginRight: "0.25rem",
                    paddingBottom: 12,
                    paddingTop: 12,
                  }}
                >
                  <VisibilityIcon />
                </div>
                <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                  {postData.post.postStatistics.viewCount}
                </div>
              </div>
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  marginRight: "0.5rem",
                }}
              >
                <div
                  style={{
                    marginRight: "0.25rem",
                    paddingBottom: 12,
                    paddingTop: 12,
                  }}
                >
                  <ChatBubbleOutlineIcon />
                </div>
                <div style={{ paddingBottom: 12, paddingTop: 12 }}>
                  {postData.post.postStatistics.commentCount}
                </div>
              </div>
            </div>
          </div>
          <hr />
          <div>
            <h2>Comments</h2>
          </div>
          {isAuth ? (
            <div>
              <AddComment
                postId={postData.post.id}
                comments={comments}
                setComments={setComments}
                postData={postData}
                setPostData={setPostData}
              />
            </div>
          ) : (
            <div>
              <h4>Comments allowed only for registered users</h4>
            </div>
          )}
          {comments.commentList.length > 0 ? (
            comments.commentList.map((comment) => (
              <Comment key={comment.id} comment={comment} isAuth={isAuth} />
            ))
          ) : (
            <div>There is no comments</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
}

export default Post;
