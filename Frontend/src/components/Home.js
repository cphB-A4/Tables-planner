import { Col, Container, Row, Form, Button } from "react-bootstrap";
import React, { useState, useEffect } from "react";
import HowToUse from "../helper/howToUse";
import facade from "../apiFacade";
import EventTable from "./EventTable";
function Home() {

  const defaultEvents = [];

  const [event, setEvent] = useState(null);
   const [eventsToShow, setEventsToShow] = useState(null);
  const [eventId, setEventId] = useState(-1);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [events, setEvents] = useState(...[defaultEvents]);
  

 useEffect(() => {
        facade
          .getAllEventsByUser()
          .then((res) => {
               
            setEventsToShow(res);
            console.log(res);
          })
          .catch((err) => {
            console.log(err);
            if (err.status) {
              err.fullError.then((e) => {
                console.log(e.code + ": " + e.message);
                setSuccess(false);
                setError(e.message);
              });
            } else {
              console.log("Network error");
            }
          });
        },[]);


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

        <label>Date & Time</label>
        <input
          className="form-control"
          type="datetime-local"
          id="time"
          name="time"
        />

        <button onClick={handleSubmit} className="btn btn-dark btn-space mt-3 mb-3">
          Create Event
        </button>
      </Form>
      {eventsToShow !== null ? (
        <EventTable list={eventsToShow} setEventId={setEventId} />
        ) : ("")}
    </>
  );
}

export default Home;
