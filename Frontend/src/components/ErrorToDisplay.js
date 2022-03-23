import { Alert } from "react-bootstrap";

function Error({ errorMsg, errorCode }) {
  return (
    <div>
      <Alert className="mt-4" variant={"danger"}>
        Code: {errorCode} : {errorMsg}
      </Alert>
    </div>
  );
}

export default Error;
