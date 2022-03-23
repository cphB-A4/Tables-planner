import {
  Switch,
  Route,
} from "react-router-dom";
import { Col, Container, Form, Row } from "react-bootstrap";

import React, { useState } from "react";
import NoMatch from "./NoMatch";
import ErrorToDisplay from "./ErrorToDisplay";
import facade from "../apiFacade";

function NoUserHeader(props) {
  const { login, errorMsg } = props;
  const init = { username: "", password: "" };
    const initRegister = { newUsername: "", newPassword: "" };
  const [loginCredentials, setLoginCredentials] = useState(init);
  const[toggle, setToggle] = useState(false);
  const [registerCredentials, setRegisterCredentials] = useState(initRegister);
const [registerError, setRegisterError] = useState(null);
const [showRegisterError, setShowRegisterError] = useState(false);


  const handleSubmit = (evt) => {
    evt.preventDefault();
    login(loginCredentials.username, loginCredentials.password);
    //redirects user to home page
  };
  const onChange = (evt) => {
    setLoginCredentials({
      ...loginCredentials,
      [evt.target.id]: evt.target.value,
    });
  };

  const handleChangeRegister = (evt) => {
    setRegisterCredentials({...registerCredentials,[evt.target.id] : evt.target.value})
  } 

  const handleRegister = (evt) => {
    evt.preventDefault();
    console.log(registerCredentials);
    facade.registerUser(registerCredentials).then((res) => {
      console.log('success')
      console.log(res)
      login(registerCredentials.newUsername, registerCredentials.newPassword);//login after register
    }).catch((err) => {
           console.log(err);
           if (err.status) {
             err.fullError.then((e) => {
               console.log(e.code + ": " + e.message);
               setShowRegisterError(true)
                  setRegisterError(e.message);
            
             });
           } else {
             console.log("Network error");
             //setSuccess(true);
           }
         });
  }
  return (
    <div>
      {/* <Header loggedIn={loggedIn} /> */}

      <Switch>
        <Route exact path="/">
          <Container>
            <Row className="rows">
              <Col sm={5} className="columns main-left">
                <h1 className="text-center mt-3">Exam</h1>
                <p>Skriv din beskrivelse her. Add evt billede</p>
              </Col>
              <Col className="columns login-form">
                <h1 className="text-center">Login</h1>
                <Form onChange={onChange}>
                  <Form.Group className="mb-3">
                    <Form.Label>Username</Form.Label>
                    <Form.Control placeholder="Enter username" id="username" />
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                      type="password"
                      placeholder="Password"
                      id="password"
                    />
                  </Form.Group>

                  <button
                    type="submit"
                    className="btn btn-dark btn-space"
                    onClick={handleSubmit}
                  >
                    Login
                  </button>
                  {!toggle ? (
                    <button
                      type="submit"
                      className="btn btn-dark btn-space"
                      onClick={(evt) => {
                        evt.preventDefault();
                        setToggle(true);
                      }}
                    >
                      Register
                    </button>
                  ) : (
                    ""
                  )}

                  {errorMsg ? <ErrorToDisplay errorMsg={errorMsg} /> : ""}
                </Form>
              </Col>
              <Col xs={2} className="columns"></Col>
            </Row>
          </Container>
          
          {toggle ? (
            <Container>
              <Row className="rows">
                <Col className="columns">
                  <h1 className="text-center">Register</h1>
                  <Form onChange={handleChangeRegister}>
                    <Form.Group className="mb-3">
                      <Form.Label>Enter Username</Form.Label>
                      <Form.Control
                        placeholder="Enter username"
                        id="newUsername"
                      />
                    </Form.Group>

                    <Form.Group className="mb-3">
                      <Form.Label>Enter Password</Form.Label>
                      <Form.Control
                        type="password"
                        placeholder="Password"
                        id="newPassword"
                      />
                    </Form.Group>
                    <div className="row justify-content-center">
                      <button
                        type="submit"
                        className="btn btn-dark"
                        onClick={handleRegister}
                      >
                        Register
                      </button>
                    </div>
                    {showRegisterError ? (
                      <ErrorToDisplay errorMsg={registerError} />
                    ) : (
                      <>
                        <br></br>
                        <br></br>
                        <br></br>
                        <br></br>
                      </>
                    )}
                  </Form>
                </Col>
              </Row>
            </Container>
          ) : (
            ""
          )}
        </Route>
        {/* <Route path="/fetch-single">
          <FetchSingle />
        </Route> */}
        <Route path="*">
          <NoMatch />
        </Route>
      </Switch>
    </div>
  );
}

export default NoUserHeader;