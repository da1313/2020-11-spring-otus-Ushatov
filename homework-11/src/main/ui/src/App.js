import React from "react";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import BookListComponent from "./components/BookListComponent";
import BookViewComponent from "./components/BookViewComponent";
import CreateBookComponent from "./components/CreateBookComponent";
import HeaderComponent from "./components/HeaderComponent";
import "./css/App.css"


function App() {

  return (
    <div>
      <Router>
        <HeaderComponent/>
        <div className="mainCont">
          <Switch>
            <Route exact path="/" component={BookListComponent}></Route>
            <Route path="/new/:id" component={CreateBookComponent}></Route>
            <Route path="/new/" component={CreateBookComponent}></Route>
            <Route path="/book-view/:id" component={BookViewComponent}></Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
