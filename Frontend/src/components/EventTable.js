import { Table } from "react-bootstrap";
import facade from "../apiFacade";
function EventTable({ list, setEventId }) {
  return (
   <Table striped bordered hover>
      {
        <>
          <thead>
            <tr>
              <th>Title</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {list.map((event) => (
              <tr key={event.id}>
                <td>{event.title}</td>
                <td>{event.time}</td>
                
              </tr>
            ))}
          </tbody>
        </>
      }
    </Table>
  );
    }
  

export default EventTable;
