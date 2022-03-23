import jwt_decode from "jwt-decode";
import { URL } from "./Settings";


//URL = "https://www.theagns.com/CA2-Backend";
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
    /*TODO*/
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

  const fetchData = () => {
    const options = makeOptions("GET", true); //True add's the token
    return fetch(URL + "/api/info/user", options).then(handleHttpErrors);
  };

  //Fetches from one endpoint. Only 1 external api call.
  const fetchSingleData = () => {
    const options = makeOptions("GET", true); //True add's the token
    return fetch(URL + "/api/info/fetchSingle", options).then(handleHttpErrors);
  };
  //Fetches from one endpoint. 4 external api call.
  const fetchAlotData = () => {
    const options = makeOptions("GET", true); //True add's the token
    return fetch(URL + "/api/info/fetchSeq", options).then(handleHttpErrors);
  };

  const fetchAlotDataParallel = () => {
    const options = makeOptions("GET", true); //True add's the token
    return fetch(URL + "/api/info/fetchParallel", options).then(
      handleHttpErrors
    );
  };
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
    fetchData,
    fetchSingleData,
    fetchAlotData,
    fetchAlotDataParallel,
    validateAccess,
    handleError,
    registerUser
  };
}
const facade = apiFacade();
export default facade;
