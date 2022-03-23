import facade from "../apiFacade";
import React, { useState, useEffect } from "react";
import { Col, Container, Row } from "react-bootstrap";

function FetchSequentially() {
  //const intialValue = { fact: "", length: "" };
  const [data, setData] = useState({});

  const getAlotData = () => {
    facade.fetchAlotData().then((json) => {
      console.log(json);
      setData(json);
    });
  };

  //If you want a catFact right away uncomment this code:
  //   useEffect(() => {
  //     getAlotData();
  //   }, []);
  return (
    <div>
      <Container>
        <Row className="rows">
          <Col xs={2} className="columns"></Col>
          <Col className="columns text-center">
            <h1 className="text-center mt-3">
              Get a lot of random Facts from 4 API's!
            </h1>
            <br></br>

            <button className="btn btn-primary mt-3" onClick={getAlotData}>
              Click me
            </button>
          </Col>
          <Col xs={2} className="columns"></Col>
        </Row>
        {/* <p className="text-center mt-3">{data.boredomDTO.activity}</p>
        <p className="text-center">
          participants: {data.boredomDTO.participants}
        </p>
        <p className="text-center">{data.boredomDTO.type}</p> */}
      </Container>
      {JSON.stringify(data)}
    </div>
  );
}

export default FetchSequentially;
