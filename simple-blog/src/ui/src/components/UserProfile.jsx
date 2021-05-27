import { Avatar, Button, TextField, Typography } from "@material-ui/core";
import { useCallback, useContext, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import { useHistory } from "react-router";
import { AppContext } from "../App";
import { authFetch, getUserId, useAuth } from "../services/AuthProvider";

function UserProfile() {
  let context = useContext(AppContext);

  let history = useHistory();

  let [isAuth] = useAuth();

  let userId = getUserId();

  const [userData, setUserData] = useState({
    role: {
      name: "",
    },
  });

  const [isEdit, setReadOnly] = useState({
    readOnly: true,
  });

  const [isEditOpen, setEditOpen] = useState(false);

  const fetchUserData = () => {
    if (!isAuth) return;
    authFetch(`${window.location.origin}/users/${userId}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          return { result: {} };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        console.log(body);
        setUserData(body);
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onDrop = useCallback((acceptedFiles) => {
    let url = URL.createObjectURL(acceptedFiles[0]);
    setUserData({ ...userData, avatarUrl: url });
    // Do something with the files
    // console.log(acceptedFiles);
    // const formData = new FormData();
    // formData.append("image", acceptedFiles[0]);
    // formData.append("xScale", 1);
    // formData.append("yScale", 1);
    // let url = new URL(window.location.origin + "/images/header/upload");
    // authFetch(url, { method: "POST", body: formData })
    //   .then((response) => {
    //     if (response.ok) {
    //       return response.json();
    //     } else if (response.status === 403) {
    //       context.showAuth(true);
    //       history.push("/");
    //       return { url: "" };
    //     } else {
    //       throw new Error("Error code: " + response.status);
    //     }
    //   })
    //   .then((result) => {
    //     let url = result.url;
    //     setUserData({ ...userData, avatarUrl: url });
    //     saveUser();
    //   })
    //   .catch((err) => {
    //     console.log(err);
    //     history.push("/error");
    //   });
  }, []);

  const saveUser = () => {
    authFetch(`${window.location.origin}/users/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        firstName: userData.firstName,
        lastName: userData.lastName,
        email: userData.email,
        avatarUrl: userData.avatarUrl,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          context.showAuth(true);
          history.push("/");
          return { result: false };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((result) => {
        if (result === true) {
          history.push("/post/user");
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onOpenEdit = () => {
    setEditOpen(!isEditOpen);
    setReadOnly({ readOnly: !isEdit.readOnly });
  };

  const { getRootProps, getInputProps, isDragActive, isDragReject } =
    useDropzone({
      onDrop,
      accept: ["image/jpeg", "image/png"],
      minSize: 0,
      multiple: false,
    });

  const getMessage = () => {
    if (isDragReject) {
      return (
        <div style={{ color: "red", margin: "0.5rem" }}>
          Files not supported
        </div>
      );
    } else if (isDragActive) {
      return (
        <div style={{ color: "black", margin: "0.5rem" }}>
          Drop the files here ...
        </div>
      );
    } else {
      return (
        <div style={{ color: "black", margin: "0.5rem" }}>
          Drag 'n' drop image here, or click to select
        </div>
      );
    }
  };

  useEffect(() => fetchUserData(), []);

  return (
    <div>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "center",
        }}
      >
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            width: 200,
            justifyContent: "center",
            marginRight: "2rem",
          }}
        >
          <div>
            <div>
              <img
                style={{
                  width: "100%",
                  border: "solid",
                  borderWidth: 1,
                  borderRadius: 15,
                  color: "rgb(200,200,200)",
                }}
                src={userData.avatarUrl}
              />
            </div>
            {isEditOpen ? (
              <div
                {...getRootProps()}
                style={{
                  background: "rgb(230,230,230)",
                  border: "dotted",
                  color: "rgb(170,170,170)",
                }}
              >
                <input {...getInputProps()} />
                <div
                  style={{
                    height: 60,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  {getMessage()}
                </div>
              </div>
            ) : (
              <div></div>
            )}
          </div>
        </div>
        <div>
          <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.firstName}
              inputProps={isEdit}
              style={{ width: "100%" }}
              helperText="First name"
            />
          </div>
          <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.lastName}
              defaultValue={userData.lastName}
              inputProps={isEdit}
              style={{ width: "100%" }}
              helperText="Last name"
            />
          </div>
          <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.email}
              helperText="Email"
              inputProps={isEdit}
              style={{ width: "100%" }}
            />
          </div>
          <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.registrationDate}
              type="datetime-local"
              helperText="Registration date"
              inputProps={isEdit}
            />
          </div>
          <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.role.name}
              helperText="Permission"
              inputProps={isEdit}
              style={{ width: "100%" }}
            />
          </div>
        </div>
      </div>
      <div style={{ display: "flex", justifyContent: "center" }}>
        <div style={{ marginRight: "2rem" }}>
          <Button variant="outlined" onClick={onOpenEdit}>
            edit
          </Button>
        </div>
        {isEditOpen ? (
          <div>
            <Button variant="outlined" color="primary">
              save
            </Button>
          </div>
        ) : (
          <div></div>
        )}
      </div>
    </div>
  );
}

export default UserProfile;
