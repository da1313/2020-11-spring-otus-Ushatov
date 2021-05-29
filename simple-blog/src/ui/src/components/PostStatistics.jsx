import ThumbUpIcon from "@material-ui/icons/ThumbUp";
import ThumbDownIcon from "@material-ui/icons/ThumbDown";
import VisibilityIcon from "@material-ui/icons/Visibility";
import ChatBubbleOutlineIcon from "@material-ui/icons/ChatBubbleOutline";

function PostStatistics({ post }) {
  const formatDate = (d) => {
    let [month, date, year] = new Date(d + "Z")
      .toLocaleDateString("en-US")
      .split("/");
    return `${date}.${month}.${year}`;
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "row",
        margin: "0.5rem",
        justifyContent: "space-around",
      }}
    >
      <div style={{ display: "flex", flexDirection: "row" }}>
        <div style={{ marginRight: "0.25rem" }}>
          <ThumbUpIcon />
        </div>
        <div>{post.postStatistics.likeCount}</div>
      </div>
      <div style={{ display: "flex", flexDirection: "row" }}>
        <div style={{ marginRight: "0.25rem" }}>
          <ThumbDownIcon />
        </div>
        <div>{post.postStatistics.dislikeCount}</div>
      </div>
      <div>{formatDate(post.publicationTime)}</div>
      <div style={{ display: "flex", flexDirection: "row" }}>
        <div style={{ marginRight: "0.25rem" }}>
          <VisibilityIcon />
        </div>
        <div>{post.postStatistics.viewCount}</div>
      </div>
      <div style={{ display: "flex", flexDirection: "row" }}>
        <div style={{ marginRight: "0.25rem" }}>
          <ChatBubbleOutlineIcon />
        </div>
        <div>{post.postStatistics.commentCount}</div>
      </div>
    </div>
  );
}

export default PostStatistics;
