import React, { useState } from "react";
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch,
} from "react-router-dom";
import BookTable from "./components/BookTable";
import Login from "./components/Login";
import GenericNotFound from "./components/GenericNotFound";

export const AppContext = React.createContext();

function App() {

  let isAuthenticatedStored = localStorage.getItem("isAuthenticated");
  let isAuthenticated =
    isAuthenticatedStored === null ? false : isAuthenticatedStored;
  let userStored = localStorage.getItem("user");
  
  const [state, setState] = useState({ isAuthenticated, userStored });

  return (
    <div className={"main"}>
      <AppContext.Provider value={{ state, setState }}>
        <Router>
          <Switch>
            <Route exact path="/">
              <Redirect to="/books"/>
            </Route>
            <Route path="/login">
              <Login url={"/books"} />
            </Route>
            <Route
              path="/books"
              render={() =>
                state.isAuthenticated ? <BookTable /> : <Redirect to="/login" />
              }
            />
            <Route path="*" component={GenericNotFound}/>
          </Switch>
        </Router>
      </AppContext.Provider>
    </div>
  );
}

export default App;
