import {
  Avatar,
  Button,
  IconButton,
  TextField,
  Typography,
} from "@material-ui/core";
import { useCallback, useContext, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import { useHistory } from "react-router";
import { AppContext } from "../App";
import {
  authFetch,
  getUserId,
  logout,
  useAuth,
} from "../services/AuthProvider";

import EditIcon from "@material-ui/icons/Edit";
import CloseIcon from "@material-ui/icons/Close";
import DoneIcon from "@material-ui/icons/Done";

function UserProfile() {
  let context = useContext(AppContext);

  let history = useHistory();

  let [isAuth] = useAuth();

  let userId = getUserId();

  const [avaFile, setAvaFile] = useState();

  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    registrationDate: "",
    role: {
      name: "",
    },
  });

  const [avaUrl, setAvaUrl] = useState("");

  const [emailError, setEmailError] = useState({
    error: false,
    text: "Email",
  });

  const [isEdit, setReadOnly] = useState({
    readOnly: true,
  });

  const [isEditOpen, setEditOpen] = useState(false);

  const onInputChange = ({ target: { name, value } }) => {
    setUserData({ ...userData, [name]: value });
    setEmailError({
      error: false,
      text: "Email",
    });
  };

  const fetchUserData = () => {
    if (!isAuth) return;
    authFetch(`${window.location.origin}/users/${userId}`)
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth();
          history.push("/");
          return { result: {} };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        setUserData(body);
        setAvaUrl(body.avatarUrl);
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onDrop = useCallback((acceptedFiles) => {
    let url = URL.createObjectURL(acceptedFiles[0]);
    setAvaUrl(url);
    // setUserData({...userData, avatarUrl: url});
    setAvaFile(acceptedFiles[0]);
  }, []);

  const onUserSave = () => {
    if (avaFile !== undefined) {
      const formData = new FormData();
      formData.append("image", avaFile);
      formData.append("xScale", 1);
      formData.append("yScale", 1);
      let url = new URL(window.location.origin + "/images/header/upload");
      authFetch(url, { method: "POST", body: formData })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else if (response.status === 403) {
            logout();
            context.showAuth(true);
            history.push("/");
            return { url: "" };
          } else {
            throw new Error("Error code: " + response.status);
          }
        })
        .then((body) => {
          let url = body.url;
          setAvaFile(undefined);
          saveUser(url);
          setEditOpen(false);
          setReadOnly({ readOnly: true });
        })
        .catch((err) => {
          console.log(err);
          history.push("/error");
        });
    }
    if (avaFile === undefined) {
      saveUser();
      setEditOpen(false);
      setReadOnly({ readOnly: true });
    }
  };

  const saveUser = (url) => {
    authFetch(`${window.location.origin}/users/${userId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        firstName: userData.firstName,
        lastName: userData.lastName,
        email: userData.email,
        avatarUrl: url === undefined ? userData.avatarUrl : url,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          logout();
          context.showAuth(true);
          history.push("/");
          return { result: false };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        if (body.result === true) {
          setAvaUrl(url === undefined ? userData.avatarUrl : url);
          //ava change
          if (url !== undefined){
            context.avatar.fetch(!context.avatar.trigger);
          }
        } else {
          setEmailError({
            error: true,
            text: "This email address is already taken",
          });
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
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <h3 style={{ textAlign: "center" }}>User profile</h3>
        <div style={{ display: "flex", justifyContent: "center" }}>
          <div style={{ alignSelf: "center" }}>
            <IconButton onClick={onOpenEdit}>
              {isEditOpen ? (
                <CloseIcon color="primary" />
              ) : (
                <EditIcon color="primary" />
              )}
            </IconButton>
          </div>
          {isEditOpen ? (
            <div style={{ alignSelf: "center" }}>
              <IconButton onClick={onUserSave}>
                <DoneIcon color="primary" />
              </IconButton>
            </div>
          ) : (
            <div></div>
          )}
        </div>
      </div>
      <hr />
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
                src={avaUrl}
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
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="firstName"
              value={userData.firstName}
              inputProps={isEdit}
              style={{ width: "100%" }}
              helperText="First name"
              onChange={onInputChange}
            />
            {isEditOpen ? (
              <EditIcon
                color="primary"
                style={{ marginTop: 10, marginLeft: 10 }}
              />
            ) : (
              <div></div>
            )}
          </div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="lastName"
              value={userData.lastName}
              inputProps={isEdit}
              style={{ width: "100%" }}
              helperText="Last name"
              onChange={onInputChange}
            />
            {isEditOpen ? (
              <EditIcon
                color="primary"
                style={{ marginTop: 10, marginLeft: 10 }}
              />
            ) : (
              <div></div>
            )}
          </div>
          <div style={{ margin: "1rem", display: "flex" }}>
            <TextField
              name="email"
              value={userData.email}
              error={emailError.error}
              helperText={emailError.text}
              inputProps={isEdit}
              style={{ width: "100%" }}
              onChange={onInputChange}
            />
            {isEditOpen ? (
              <EditIcon
                color="primary"
                style={{ marginTop: 10, marginLeft: 10 }}
              />
            ) : (
              <div></div>
            )}
          </div>
          <div style={{ margin: "1rem" }}>
            <TextField
              name="registrationDate"
              value={userData.registrationDate}
              type="datetime-local"
              helperText="Registration date"
              onChange={onInputChange}
            />
          </div>
          {/* <div style={{ margin: "1rem" }}>
            <TextField
              value={userData.role.name}
              helperText="Permission"
              inputProps={isEdit}
              style={{ width: "100%" }}
            />
          </div> */}
        </div>
      </div>
    </div>
  );
}

export default UserProfile;
