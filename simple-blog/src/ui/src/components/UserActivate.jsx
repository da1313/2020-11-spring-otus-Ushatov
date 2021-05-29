import { CircularProgress } from "@material-ui/core";
import { useEffect, useState } from "react";
import { useHistory, useParams } from "react-router";

function UserActivate() {
  let { id } = useParams();

  let history = useHistory();

  let progress = {
    activating: "activating",
    complete: "complete",
    rejected: "rejected",
  };

  const [status, setStatus] = useState(progress.rejected);

  const verifyToken = () => {
    fetch(`${window.location.origin}/users/activate/${id}`, {
      method: "PUT",
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
          setStatus(progress.complete);
        } else {
          setStatus(progress.rejected)
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const renderProgress = (p) => {
    if (p === progress.activating) {
      return (
        <div>
          <CircularProgress />
        </div>
      );
    } else if (p === progress.complete) {
      return <h3>Account succsecifuly activated! You can now log in!</h3>;
    } else {
      return (
        <h3>
          Account has already been activated or the activation link is out of
          date
        </h3>
      );
    }
  };

  useEffect(() => verifyToken(), []);

  return (
    <div style={{ display: "flex", justifyContent: "center" }}>
      {renderProgress(status)}
    </div>
  );
}

export default UserActivate;
