import facade from "../apiFacade";
import React, { useState, useEffect } from "react";
import { Col, Container, Row } from "react-bootstrap";
import ErrorToDisplay from "./ErrorToDisplay";

function FetchSingle() {
  //const intialValue = { fact: "", length: "" };
  const [data, setData] = useState({});
  const [error, setError] = useState();

  const getRandomCatFact = () => {
    facade
      .fetchSingleData()
      .then((json) => {
        console.log(json);
        setData(json);
      })
      .catch((error) => facade.handleError(error, setError));
  };

  //If you want a catFact right away uncomment this code:
  /*useEffect(() => {
    getRandomCatFact();
  }, []);*/

  return (
    <div>
      <Container>
        <Row className="rows">
          <Col xs={2} className="columns"></Col>
          <Col className="columns text-center">
            <h1 className="text-center mt-3">Get random cat fact API!</h1>
            <br></br>
            <h5>{data.fact}</h5>
            <p>Length of fact: {data.length}</p>

            <button className="btn btn-primary mt-3" onClick={getRandomCatFact}>
              Click me
            </button>
          </Col>
          <Col xs={2} className="columns"></Col>
        </Row>
        {error && (
          <ErrorToDisplay errorMsg={error.message} errorCode={error.code} />
        )}
      </Container>
    </div>
  );
}

export default FetchSingle;
