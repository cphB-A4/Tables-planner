import { NavLink, Route } from "react-router-dom";

function Header({ loggedIn, logout, validateAccess }) {
  return (
    <ul className="header">
      {console.log(loggedIn)}
      <li>
        <NavLink exact activeClassName="active" to="/">
          Home
        </NavLink>
      </li>

      <li>
        <NavLink exact activeClassName="active" to="/fetch-single">
          FetchSingle
        </NavLink>
      </li>
      {!loggedIn ? (
        <li>
          <NavLink exact activeClassName="active" to="/login">
            Login
          </NavLink>
        </li>
      ) : (
        <>
          {validateAccess === "user" ? (
            <li>
              <NavLink exact activeClassName="active" to="/fetch-sequentially">
                Fetch sequentially
              </NavLink>
            </li>
          ) : (
            ""
          )}

          {validateAccess === "admin" ? (
            <li>
              <NavLink exact activeClassName="active" to="/fetch-parallelly">
                Fetch parallelly
              </NavLink>
            </li>
          ) : (
            ""
          )}

          <li>
            {/*Logout is never active. Once you click you gets to the homepage*/}
            <NavLink
              exact
              activeClassName="none"
              to="/"
              onClick={() => logout()}
            >
              Logout
            </NavLink>
          </li>
        </>
      )}
    </ul>
  );
}

export default Header;
