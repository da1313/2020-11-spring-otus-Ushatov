import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch,
} from "react-router-dom";
import Header from "./components/Header";
import PostList from "./components/PostList";
import Footer from "./components/Footer";
import Post from "./components/Post";
import UserPostList from "./components/UserPostList";
import EditPost from "./components/EditPost";
import { useAuth } from "./services/AuthProvider";
import NotFound from "./components/NotFound";
import Login from "./components/Login";
import UserProfile from "./components/UserProfile";
import Registration from "./components/Registration";
import UserActivate from "./components/UserActivate";
import LeftMenu from "./components/LeftMenu";
import { Autorenew } from "@material-ui/icons";

export const AppContext = React.createContext();

function App() {
  let [isAuth] = useAuth();

  const [showAuth, setShowAuth] = useState(false);

  return (
    <AppContext.Provider
      value={{ showAuth: setShowAuth, avatar: { trigger: false, fetch: null } }}
    >
      <Router>
        <Login show={showAuth} handleClose={setShowAuth} />
        <Header />
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            flexGrow: 1,
            flexShrink: 1,
            flexBasis: "auto",
          }}
        >
          <div style={{width: "25%"}}>
            <div style={{marginRight: "5rem"}}>
              <LeftMenu />
            </div>
          </div>
          <div style={{ width: "50%" }}>
            <Switch>
              <Route exact path="/">
                <PostList />
              </Route>
              <Route path="/post/view/:id">
                <Post />
              </Route>
              <Route path="/post/edit/:pathId">
                {() => {
                  if (isAuth) {
                    return <EditPost isEdit={true} />;
                  } else {
                    setShowAuth(true);
                    return <Redirect to="/" />;
                  }
                }}
              </Route>
              <Route path="/post/new">
                {() => {
                  if (isAuth) {
                    return <EditPost isNew={true} />;
                  } else {
                    setShowAuth(true);
                    return <Redirect to="/" />;
                  }
                }}
              </Route>
              <Route path="/post/user">
                {() => {
                  if (isAuth) {
                    return <UserPostList />;
                  } else {
                    setShowAuth(true);
                    return <Redirect to="/" />;
                  }
                }}
              </Route>
              <Route path="/profile">
                {() => {
                  if (isAuth) {
                    return <UserProfile />;
                  } else {
                    setShowAuth(true);
                    return <Redirect to="/" />;
                  }
                }}
              </Route>
              <Route path="/registration">
                <Registration/>
              </Route>
              <Route path="/user/activate/:id">
                <UserActivate/>
              </Route>
              <Route path="/error">
                <h1>Error</h1>
              </Route>
              <NotFound />
            </Switch>
          </div>
          <div></div>
        </div>
      </Router>
    </AppContext.Provider>
  );
}

export default App;
