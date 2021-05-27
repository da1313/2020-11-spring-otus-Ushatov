import CloseIcon from "@material-ui/icons/Close";

import { Button, IconButton, TextField } from "@material-ui/core";
import { useContext, useState } from "react";
import { AppContext } from "../App";
import "../css/login.css";
import { login } from "../services/AuthProvider";

function Login({ show, handleClose }) {

  let context = useContext(AppContext);

  const loginClassName =
    show === true ? "modal display-block" : "modal display-none";

  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
  });

  const [isError, setError] = useState(false);

  const onChange = ({ target: { name, value } }) => {
    setCredentials({ ...credentials, [name]: value });
    setError(false)
  };

  const onSubmit = (event) => {
    if (event) {
      event.preventDefault();
    }
    fetch("/login", {
      method: "POST",
      body: JSON.stringify(credentials),
    })
      .then((response) => {
        if (response.ok) {
          let authToken =  response.headers.get("Authorization");
          let refreshToken = response.headers.get("Refresh");
          if (authToken === null || refreshToken === null){
            throw new Error();
          }
          return {
            accessToken: authToken,
            refreshToken: refreshToken,
          };
        } else {
          throw new Error();
        }
      })
      .then((token) => {
        login(token);
        setCredentials({
          username: "",
          password: "",
        });
        context.showAuth(false);
        context.avatar.fetch(!context.avatar.trigger);
        console.log(context);
      })
      .catch((err) => setError(true));
  };

  return (
    <div className={loginClassName}>
      <div className="modal-main">
        <div
          style={{
            display: "flex",
            justifyContent: "flex-end",
            position: "absolute",
            right: 0,
            top: 0,
          }}
        >
          <IconButton onClick={handleClose}>
            <CloseIcon />
          </IconButton>
        </div>
        <form onSubmit={onSubmit}>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              margin: "1rem",
            }}
          >
            <h2 style={{ textAlign: "center" }}>Please sign in</h2>
            <div style={{ marginBottom: "0.5rem" }}>
              <TextField
                error={isError}
                variant="outlined"
                name="username"
                value={credentials.username}
                label="Email"
                onChange={onChange}
              />
            </div>
            <div style={{ marginBottom: "0.5rem" }}>
              <TextField
                error={isError}
                helperText={
                  isError ? "incorrect email or password" : ""
                }
                variant="outlined"
                type="password"
                name="password"
                label="Password"
                value={credentials.password}
                onChange={onChange}
              />
            </div>
            <div style={{ marginBottom: "0.5rem" }}>
              <Button type="submit" variant="contained">
                Sign in
              </Button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;
