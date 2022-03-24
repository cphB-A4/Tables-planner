import { Col, Container, Row, Form, Button } from "react-bootstrap";
import React, { useState } from "react";
import HowToUse from "../helper/howToUse";
import facade from "../apiFacade";
function Home() {

  const [event, setEvent] = useState(null);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);


  const onChange = (evt) => {
    setEvent({
      ...event,
      [evt.target.id]: evt.target.value,
    });
  };

  const handleSubmit = (evt) => {
    evt.preventDefault();
    //check if some inputs are empty. If yes --> error
    facade
      .createEvent(event)
      .then((res) => {
        console.log(res);
        setSuccess("The event is now added!");
        setError(null);
      })
      .catch((err) => {
        console.log(err);
        if (err.status) {
          err.fullError.then((e) => {
            console.log(e.code + ": " + e.message);
            setError("Have you filled in all fields?");
            setSuccess(null);
          });
        } else {
          console.log("Network error");
        }
      });
  };
  return (
    <>
      <HowToUse />
      <h1>Create event</h1>
      <Form onChange={onChange}>
        <Row className="mb-3">
          <Form.Group as={Col}>
            <Form.Label>Title</Form.Label>
            <Form.Control id="title" type="text" placeholder="Enter Title" />
          </Form.Group>
        </Row>
        <Row className="mb-3">
          <Form.Group as={Col}>
            <Form.Label>Event notes</Form.Label>
            <Form.Control id="description" as="textarea" rows={3} />
          </Form.Group>
        </Row>

        <label for="time">Date & Time</label>
        <input
          class="form-control"
          type="datetime-local"
          id="time"
          name="time"
        />

        <button
          onClick={handleSubmit}
          className="btn btn-dark btn-space mt-3"
        >
          Create Event
        </button>
      </Form>
    </>
  );
}

export default Home;
