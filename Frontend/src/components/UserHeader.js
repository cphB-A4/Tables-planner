import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useParams,
  NavLink,
} from "react-router-dom";

import React, { useState, useEffect } from "react";
import Header from "./Header";
import Home from "./Home";
import NoMatch from "./NoMatch";
import EditEvent from "./EditEvent";
import facade from "../apiFacade";

function UserHeader(props) {
  const { loggedIn, logout, validateAccess } = props;
  const eventsArr = [
    { id: 1, title: "First", content: "Hello world!" },
    { id: 2, title: "Second", content: "Hello again!" },
  ];
  return (
    <div>
      <Header
        validateAccess={facade.validateAccess()}
        logout={logout}
        loggedIn={loggedIn}
      />
      <Switch>
        
        <Route
          exact
          path="edit-event/:id"
          render={({ match }) => (
            
            <EditEvent
              eventId={eventsArr.find((e) => e.id === match.params.id)}
            />
          )}
        />
        {/* <Route exact path="/edit-event">
          <EditEvent />
        </Route> */}

        <Route exact path="/">
          <Home />
        </Route>
        <Route path="*">
          <NoMatch />
        </Route>
      </Switch>
    </div>
  );
}

export default UserHeader;
