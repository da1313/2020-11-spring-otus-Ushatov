import { Button, CircularProgress, TextField } from "@material-ui/core";
import { useState } from "react";
import { useHistory } from "react-router";

function Registration() {
  let history = useHistory();

  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  let page = {
    form: "form",
    progress: "progress",
    success: "success",
  };

  const [emailError, setEmailError] = useState({
    error: false,
    text: "Email",
  });

  const [passwordError, setPasswordError] = useState({
    error: false,
    text: "Password",
  });

  const [status, setStatus] = useState(page.form);

  const onInputChange = ({ target: { name, value } }) => {
    setUserData({ ...userData, [name]: value });
    setEmailError({
      error: false,
      text: "Email",
    });
    setPasswordError({
      error: false,
      text: "Password",
    });
  };

  const register = () => {
    setStatus(page.progress);
    fetch(`${window.location.origin}/users`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(userData),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if (body.result) {
          setStatus(page.success);
        } else {
          if (body.isEmailError) {
            setEmailError({
              error: true,
              text: "This email address is already taken",
            });
          } else if (body.isPasswordError) {
            setPasswordError({
              error: true,
              text: "Password must be 8 characters long",
            });
          }
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const renderDetails = (e) => {
    if (e === page.form) {
      return (
        <div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="firstName"
              value={userData.firstName}
              style={{ width: "100%" }}
              helperText="First name"
              onChange={onInputChange}
            />
          </div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="lastName"
              value={userData.lastName}
              style={{ width: "100%" }}
              helperText="Last name"
              onChange={onInputChange}
            />
          </div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="email"
              value={userData.email}
              error={emailError.error}
              helperText={emailError.text}
              style={{ width: "100%" }}
              onChange={onInputChange}
            />
          </div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="password"
              value={userData.password}
              error={passwordError.error}
              helperText={passwordError.text}
              style={{ width: "100%" }}
              type="password"
              onChange={onInputChange}
            />
          </div>
          {/* <div>captcha</div> */}
          <div style={{ textAlign: "center" }}>
            <Button color="primary" onClick={register}>
              register
            </Button>
          </div>
        </div>
      );
    } else if (e === page.progress) {
      return (
        <div style={{ marginTop: "3rem" }}>
          <CircularProgress />
        </div>
      );
    } else {
      return (
        <div>
          <h3>Please check your email for further instructions</h3>
        </div>
      );
    }
  };

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <h3 style={{ textAlign: "center" }}>Registration</h3>
      </div>
      <hr />
      <div
        style={{
          display: "flex",
          // flexDirection: "row",
          justifyContent: "center",
        }}
      >
        {renderDetails(status)}
      </div>
    </div>
  );
}

export default Registration;
