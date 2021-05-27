import { useHistory } from "react-router";
import PostStatistics from "./PostStatistics";

function PostCard({ post }) {
  let history = useHistory();

  const handleClick = () => {
    history.push("/post/view/" + post.id);
  };

  return (
    <div
      style={{
        width: 375,
        border: "solid",
        borderWidth: 1,
        borderRadius: 15,
        borderColor: "rgb(228, 228, 228)",
        margin: "0.5rem",
      }}
    >
      <div>
        <button className="btn" onClick={handleClick}>
          <div>
            <img
              src={post.postCardImageUrl}
              style={{ borderRadius: 10, width: "100%" }}
            />
          </div>
        </button>
      </div>
      <div style={{marginLeft: "0.5rem", marginRight: "0.5rem"}}>
        <span style={{ fontWeight: 600 }}>{post.title}</span>
      </div>
      <div>
        <PostStatistics post={post} />
      </div>
    </div>
  );
}

export default PostCard;
