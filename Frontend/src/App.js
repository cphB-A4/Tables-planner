import logo from "./logo.svg";
import "./App.css";
import Header from "./components/Header";
import facade from "./apiFacade";
import "bootstrap/dist/css/bootstrap.min.css";

import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useParams,
  NavLink,
  useHistory,
} from "react-router-dom";
import Home from "./components/Home";
import NoUserHeader from "./components/NoUserHeader";
import UserHeader from "./components/UserHeader";

function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  const [errorMsg, setErrorMsg] = useState(null);
  const history = useHistory();

  const logout = () => {
    facade.logout();
    setLoggedIn(false);
  };
  const login = (user, pass) => {
    facade
      .login(user, pass)
      .then((res) => {
        //If user login succesfully it redirects to homepage
        setLoggedIn(true);
        setErrorMsg("");
        const path = `/`;
        history.push(path);
      })
      .catch((err) => {
        if (err.status) {
          err.fullError.then((e) => {
            console.log(e.code + ": " + e.message);
            setErrorMsg(e.code + ": " + e.message);
          });
        } else {
          console.log("Network error");
        }
      });
  };

  //logedInState propdrilling ned til Header component. Lifting state up
  return (
    <div className="App">
      {/* <Header loggedIn={loggedIn} /> */}

      {!loggedIn ? (
        <NoUserHeader errorMsg={errorMsg} loggedIn={loggedIn} login={login} />
      ) : (
        <div>
          {/* <button onClick={logout}>Logout</button> */}
          <UserHeader
            validateAccess={facade.validateAccess()}
            logout={logout}
            loggedIn={loggedIn}
          />
          {}
        </div>
      )}
      {/* <Switch>
        <Route exact path="/">
          <Home />
        </Route>
        <Route path="/login">
          <Login />
        </Route>
        <Route path="/fetch-single">
          <FetchSingle />
        </Route>
      </Switch> */}
    </div>
  );
}

export default App;
