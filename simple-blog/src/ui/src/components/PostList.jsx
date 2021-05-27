import { useContext, useEffect, useState } from "react";
import PostCard from "./PostCard";
import InfiniteScroll from "react-infinite-scroll-component";
import { authFetch, useAuth } from "../services/AuthProvider";
import { AppContext } from "../App";
import { CircularProgress } from "@material-ui/core";
import { useHistory } from "react-router";

function PostList() {
  let history = useHistory();

  let context = useContext(AppContext);

  // let isAuth = useAuth();

  const [posts, setPosts] = useState({
    postList: [],
    pageNumber: -1,
    totalPages: 2,
    hasNext: true,
  });

  const fetchPosts = () => {
    let nextPage = posts.pageNumber + 1;
    let hasNext = !(nextPage === posts.totalPages);
    let url = new URL(window.location.origin + "/posts");
    let params = {
      pageNumber: nextPage,
      pageSize: 12,
      sort: "id",
    };
    Object.keys(params).forEach((key) =>
      url.searchParams.append(key, params[key])
    );
    authFetch(url)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          context.showAuth(true);
          history.push("/");
          //todo add redirect
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        setPosts({
          postList: [...posts.postList, ...body.postCartDataList],
          pageNumber: nextPage,
          hasNext: hasNext,
          totalPages: body.totalPages,
        });
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  useEffect(() => fetchPosts(), []);

  return (
    <div id="scrollableDiv" style={{ height: "100vh", overflowY: "auto" }}>
      <InfiniteScroll
        dataLength={posts.postList.length}
        next={fetchPosts}
        hasMore={posts.hasNext}
        loader={
          <div
            style={{ width: "100%", display: "flex", justifyContent: "center" }}
          >
            <CircularProgress />
          </div>
        }
        endMessage={
          <div style={{ width: "100%" }}>
            <span>No moaaar!!</span>
          </div>
        }
        scrollableTarget="scrollableDiv"
        style={{
          display: "flex",
          flexWrap: "wrap",
          marginLeft: "auto",
          marginRight: "auto",
        }}
      >
        {posts.postList.map((post) => (
          <PostCard key={post.id} post={post} />
        ))}
      </InfiniteScroll>
    </div>
  );
}

export default PostList;
