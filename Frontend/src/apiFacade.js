import jwt_decode from "jwt-decode";
import { URL } from "./Settings";

function handleHttpErrors(res) {
  if (!res.ok) {
    return Promise.reject({ status: res.status, fullError: res.json() });
  }
  return res.json();
}

function handleError(error, setError) {
  if (error.status) {
    error.fullError.then((data) => setError(data));
  } else {
    setError({ code: 500, message: "Some unknown error happened" });
  }
}

function apiFacade() {
  /* Insert utility-methods from a latter step (d) here (REMEMBER to uncomment in the returned object when you do)*/

  const setToken = (token) => {
    localStorage.setItem("jwtToken", token);
  };
  const getToken = () => {
    return localStorage.getItem("jwtToken");
  };

  //Decode token

  const validateAccess = () => {
    var decoded = jwt_decode(getToken());
    const { roles } = decoded;
    console.log(roles);
    //  console.log(decoded);
    return roles;
  };

  const loggedIn = () => {
    const loggedIn = getToken() != null;
    return loggedIn;
  };
  const logout = () => {
    localStorage.removeItem("jwtToken");
  };

  const login = (user, password) => {
  
    const options = makeOptions("POST", true, {
      username: user,
      password: password,
    });
    return fetch(URL + "/api/login", options)
      .then(handleHttpErrors)
      .then((res) => {
        setToken(res.token);
      });
  };

const registerUser = (registerCredentials) => {
     const options = makeOptions('POST',true,registerCredentials);
     console.log(registerCredentials)
     return fetch(URL + "/api/register", options)
       .then(handleHttpErrors)
       .then((res) => {});
   }

const createEvent = (eventDetails) => {
  const options = makeOptions("POST", true, eventDetails);
  console.log(eventDetails);
   return fetch(URL + "/api/user/createEvent", options)
     .then(handleHttpErrors);
}

const getAllEventsByUser = () => {
  const options = makeOptions("GET", true);
  
  return fetch(URL + "/api/user/get-all-events-by-user", options)
    .then(handleHttpErrors)
}


 
  const makeOptions = (method, addToken, body) => {
    var opts = {
      method: method,
      headers: {
        "Content-type": "application/json",
        Accept: "application/json",
      },
    };
    if (addToken && loggedIn()) {
      opts.headers["x-access-token"] = getToken();
    }
    if (body) {
      opts.body = JSON.stringify(body);
    }

    return opts;
  };

  return {
    makeOptions,
    setToken,
    getToken,
    loggedIn,
    login,
    logout,
    validateAccess,
    handleError,
    registerUser,
    createEvent,
    getAllEventsByUser
  };
}
const facade = apiFacade();
export default facade;
