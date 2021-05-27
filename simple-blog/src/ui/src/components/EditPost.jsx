import { useContext, useEffect, useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { useHistory, useParams } from "react-router";
import { authFetch } from "../services/AuthProvider";
import DateTimePicker from "react-datetime-picker";
import { Button } from "@material-ui/core";
import { AppContext } from "../App";

let quillObject;

const imageHandler = () => {
  const input = document.createElement("input");

  input.setAttribute("type", "file");
  input.setAttribute("accept", "image/*");
  input.click();

  input.onchange = async () => {
    const file = input.files[0];
    //check file !==null?
    const formData = new FormData();
    formData.append("image", file);
    const res = await uploadFile(formData);
  };
};

const uploadFile = async (formData) => {
  let url = new URL(window.location.origin + "/images/upload");
  authFetch(url, { method: "POST", body: formData })
  .then((response) => {
    if (response.ok) {
      response.json().then((body) => {
        let url = body.url;
        const range = quillObject.getEditorSelection();
        quillObject.getEditor().insertEmbed(range.index, "image", url);
      });
    } else {
      console.log("!!!");
    }
  });
};

const modules = {
  toolbar: {
    container: [
      [{ header: "1" }, { header: "2" }, { font: [] }],
      [{ size: [] }],
      ["bold", "italic", "underline", "strike", "blockquote"],
      [
        { list: "ordered" },
        { list: "bullet" },
        { indent: "-1" },
        { indent: "+1" },
        { align: [] },
      ],
      ["link", "image", "video"],
      ["clean"],
    ],
    handlers: {
      image: imageHandler,
    },
  },
  clipboard: {
    // toggle to add extra line breaks when pasting HTML:
    matchVisual: false,
  },
};

function EditPost({ postId, isEdit, isNew }) {
  let context = useContext(AppContext);

  let { pathId } = useParams();

  let id = postId === undefined ? pathId : postId;

  let params = {
    title: "",
    isActive: false,
    publicationTime: new Date(),
    postCardImageUrl: "",
    text: "",
  };

  const setUp = () => {
    if (isEdit) {
      authFetch(`${window.location.origin}/posts/user/${id}`)
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else if (response.status === 403) {
            context.showAuth(true);
            history.push("/");
            //todo add redirect
          } else {
            throw new Error("Error code: " + response.status);
          }
        })
        .then((result) => {
          setState(result.text);
          setHeaderImageUrl(result.postCardImageUrl);
          setPostData({
            title: result.title,
            isActive: result.isActive,
          });
          setPubDatetime(result.publicationTime);
        }).catch(err =>{
          console.log(err);
          history.push("/error");
        });
    }
    if (isNew) {
      setState(params.text);
      setHeaderImageUrl(params.postCardImageUrl);
      setPostData({
        title: params.title,
        isActive: params.isActive,
      });
      setPubDatetime(params.publicationTime);
    }
  };

  let history = useHistory();

  const [state, setState] = useState(params.text);

  const [postData, setPostData] = useState({
    title: params.title,
    isActive: params.isActive,
  });

  const [pubDatetime, setPubDatetime] = useState(params.publicationTime); /////

  const [headerImageUrl, setHeaderImageUrl] = useState(params.postCardImageUrl);

  const onTitleChange = (event) => {
    setPostData({ ...postData, title: event.target.value });
  };

  const onIsActiveChange = (event) => {
    setPostData({ ...postData, isActive: event.target.checked });
  };

  const handleChange = (value) => {
    setState(value);
  };

  const onSubmit = () => {
    let url = isEdit
      ? `${window.location.origin}/posts/${id}`
      : `${window.location.origin}/posts`;
    let method = isEdit ? "PUT" : "POST";

    authFetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify({
        title: postData.title,
        text: state,
        publicationTime: pubDatetime,
        isActive: postData.isActive,
        postCardImageUrl: headerImageUrl,
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
      .then((body) => {
        if (body.result === true) {
          history.push("/post/user");
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const onHeaderImageChange = (event) => {
    const formData = new FormData();
    formData.append("xScale", 5);
    formData.append("yScale", 3);
    formData.append("image", event.target.files[0]);
    let url = new URL(window.location.origin + "/images/header/upload");
    authFetch(url, { method: "POST", body: formData })
      .then((response) => {
        if (response.ok) {
          return response.json();
        } else if (response.status === 403) {
          context.showAuth(true);
          history.push("/");
          return { url: "" };
        } else {
          throw new Error("Error code: " + response.status);
        }
      })
      .then((body) => {
        let url = body.url;
        setHeaderImageUrl(url);
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  useEffect(() => setUp(), [id]);

  return (
    <div>
      <div>
        <div>
          <div style={{ textAlign: "center" }}>
            {isEdit ? <h3>Update post</h3> : <h3>Create new post</h3>}
          </div>
          <hr />
        </div>
        <div style={{ display: "flex", flexDirection: "row" }}>
          <div
            style={{
              width: "50%",
              margin: "0.5rem",
              display: "flex",
              flexDirection: "column",
              justifyContent: "space-between",
            }}
          >
            <div style={{ margin: "0.5rem" }}>
              <div>Title</div>
              <textarea
                name="title"
                style={{ width: "100%", resize: "none" }}
                rows="5"
                value={postData.title}
                onChange={onTitleChange}
              ></textarea>
            </div>
            <div style={{ margin: "0.5rem" }}>
              <div>Publication time</div>
              <DateTimePicker
                onChange={setPubDatetime}
                value={new Date(pubDatetime)}
              />
            </div>
            <div style={{ margin: "0.5rem" }}>
              <input
                type="checkbox"
                name="isActive"
                checked={postData.isActive}
                onChange={onIsActiveChange}
              />
              <span>Is this post will be active?</span>
            </div>
          </div>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "space-between",
              width: "50%",
              margin: "0.5rem",
            }}
          >
            <div style={{ margin: "0.5rem" }}>
              Header image<span>(will be centered and clipped to 5x3)</span>
            </div>
            <div>
              <img
                src={headerImageUrl}
                style={{ width: "100%", borderRadius: 15 }}
              />
              <div style={{ margin: "0.5rem" }}>
                <input
                  type="file"
                  accept="/image*"
                  onChange={onHeaderImageChange}
                />
              </div>
            </div>
          </div>
        </div>

        <ReactQuill
          ref={(el) => (quillObject = el)}
          value={state}
          modules={modules}
          onChange={handleChange}
        />
      </div>
      <div
        style={{ margin: "1rem", display: "flex", justifyContent: "center" }}
      >
        <div>
          <Button variant="contained" color="primary" onClick={onSubmit}>
            Save
          </Button>
        </div>
      </div>
    </div>
  );
}

export default EditPost;
