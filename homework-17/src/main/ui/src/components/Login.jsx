import { useContext, useState } from "react";
import { useHistory } from "react-router";
import { AppContext } from "../App";
import loginService from "../service/LoginService";

function Login({ url }) {
  let history = useHistory();

  let context = useContext(AppContext);

  const [state, setState] = useState({});

  const doLogin = () => {
    loginService
      .sendLogin(state.username, state.password, context)
      .then((r) => {
        if (r) {
          history.push(url);
        } else {
          setState({
            username: state.username,
            password: state.password,
            isFailed: true,
          });
        }
      });
  };

  const onUsernameChangeHandler = (e) => {
    let username = e.target.value;
    setState({ username: username, password: state.password });
  };

  const onPasswordChangeHandler = (e) => {
    let password = e.target.value;
    setState({ username: state.username, password: password });
  };

  return (
    <div className="login">
      <div className="header">
        <div>Please sigh in!</div>
      </div>
      <div>
        {state.isFailed ? <span>Bad credentials!</span> : <span></span>}
      </div>
      <div>
        <input
          type="text"
          name="username"
          placeholder="username"
          onChange={onUsernameChangeHandler}
        />
      </div>
      <div>
        <input
          type="password"
          name="password"
          placeholder="password"
          onChange={onPasswordChangeHandler}
        />
      </div>
      <div>
        <button type={"submit"} onClick={doLogin}>
          Sign in
        </button>
      </div>
    </div>
  );
}

export default Login;
