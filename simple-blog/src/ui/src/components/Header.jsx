import { useContext, useEffect, useState } from "react";
import { AppContext } from "../App";
import { authFetch, getUserId, logout, useAuth } from "../services/AuthProvider";
import "react-dropdown/style.css";
import Popup from "reactjs-popup";
import "../css/user-popup-menu.css";
import { useHistory } from "react-router";
import { Avatar } from "@material-ui/core";
import AccountCircleIcon from '@material-ui/icons/AccountCircle';

function Header() {
  let context = useContext(AppContext);

  let history = useHistory();

  let [isAuth] = useAuth();

  const [tr, setTr] = useState(false);

  const [userAvaUrl, setUserAvaUrl] = useState("");

  context.avatar.trigger = tr; 
  context.avatar.fetch = setTr;

  const onAuthClick = () => {
    context.showAuth(true);
  };

  const moveToMyPosts = () => {
    history.push("/post/user");
  };

  const moveToProfile = () => {
    history.push("/profile");
  };

  const doLogout = () => {
    logout();
    history.push("/");
  };

  const fetchUserAvatar = () => {
    console.log(isAuth);
    console.log("fetch ava");
    if (!isAuth) return;
    authFetch(`${window.location.origin}/users/avatar/${getUserId()}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          context.showAuth(true);
          history.push("/");
          return { body: {url: ""} };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        setUserAvaUrl(body.url);
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  useEffect(() => fetchUserAvatar(), [tr]);

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "row",
        justifyContent: "space-between",
        flexGrow: 0,
        flexShrink: 1,
        flexBasis: "auto",
      }}
    >
      <div>
        <h1>Simple blog</h1>
      </div>
      <div
        style={{ display: "flex", alignItems: "center", marginRight: "3rem" }}
      >
        <div style={{ marginRight: "1rem" }}>
          {/* <img src={SearchLogo} style={{ width: "4vh" }} /> */}
        </div>
        {isAuth === true ? (
          <Popup
            trigger={<Avatar src={userAvaUrl} />}
            on="hover"
            position="bottom center"
            closeOnDocumentClick
            mouseEnterDelay={300}
            mouseEnterDelay={0}
            arrow={false}
          >
            <div className="user-popup-menu">
              <div className="user-popup-menu-item">
                <button className="btn" onClick={moveToMyPosts}>
                  My posts
                </button>
              </div>
              <div className="user-popup-menu-item">
                <button className="btn" onClick={moveToProfile}>
                  Profile
                </button>
              </div>
              <div className="user-popup-menu-item">Statistics</div>
              <div
                className="user-popup-menu-item"
                style={{ borderBottom: "none" }}
              >
                <button className="btn" onClick={doLogout}>
                  Logout
                </button>
              </div>
            </div>
          </Popup>
        ) : (
          <div onClick={onAuthClick} style={{ cursor: "pointer" }}>
            <AccountCircleIcon fontSize="large"/>
          </div>
        )}
      </div>
    </div>
  );
}

export default Header;
