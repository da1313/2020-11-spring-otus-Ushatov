import MaterialTable, { MTableToolbar } from "material-table";
import { forwardRef, useContext, useRef, useState } from "react";
import AddBox from "@material-ui/icons/AddBox";
import ArrowDownward from "@material-ui/icons/ArrowDownward";
import Check from "@material-ui/icons/Check";
import ChevronLeft from "@material-ui/icons/ChevronLeft";
import ChevronRight from "@material-ui/icons/ChevronRight";
import Clear from "@material-ui/icons/Clear";
import DeleteOutline from "@material-ui/icons/DeleteOutline";
import Edit from "@material-ui/icons/Input";
import FilterList from "@material-ui/icons/FilterList";
import FirstPage from "@material-ui/icons/FirstPage";
import LastPage from "@material-ui/icons/LastPage";
import Remove from "@material-ui/icons/Remove";
import SaveAlt from "@material-ui/icons/SaveAlt";
import Search from "@material-ui/icons/Search";
import ViewColumn from "@material-ui/icons/ViewColumn";

import EditIcon from "@material-ui/icons/Edit";
import OpenInNewIcon from "@material-ui/icons/OpenInNew";

import { authFetch } from "../services/AuthProvider";
import {
  Checkbox,
  FormControlLabel,
  RadioGroup,
  Radio,
  Button,
  TextField,
} from "@material-ui/core";
import Popup from "reactjs-popup";
import { useHistory } from "react-router";
import { formatDate } from "../services/utils";
import { AppContext } from "../App";
import { logout } from "../services/AuthProvider____";

const tableIcons = {
  Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
  Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
  Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
  Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
  DetailPanel: forwardRef((props, ref) => (
    <ChevronRight {...props} ref={ref} />
  )),
  Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
  Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
  Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
  FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
  LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
  NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
  PreviousPage: forwardRef((props, ref) => (
    <ChevronLeft {...props} ref={ref} />
  )),
  ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
  Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
  SortArrow: forwardRef((props, ref) => <ArrowDownward {...props} ref={ref} />),
  ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
  ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />),
};

function UserPostList() {
  let history = useHistory();

  let tableRef = useRef();

  let context = useContext(AppContext);

  const status = {
    moderated: "MODERATED",
    published: "PUBLISHED",
    declined: "DECLINED",
    inactive: "INACTIVE",
    future: "FUTURE",
    all: "ALL",
  };

  const [state, setState] = useState(status.published);

  const [isConfirmOpen, setConfirmOpen] = useState({
    isOpen: false,
    id: null,
  });

  // const [post, setPost] = useState({});

  const closeConfirm = () => {
    setConfirmOpen(false);
  };

  const statusChangeHandler = (event) => {
    setState(event.target.value);
    tableRef.current.onQueryChange();
  };

  const onDelete = () => {
    let id = isConfirmOpen.id;
    setConfirmOpen({ isOpen: false, id: null });
    authFetch(`${window.location.origin}/posts/${id}`, {
      method: "DELETE",
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
          tableRef.current.onQueryChange();
        }
      })
      .catch((err) => {
        console.log(err);
        history.push("/error");
      });
  };

  const moveToPostPage = (id) => {
    history.push("/post/view/" + id);
  };

  const moveToEditPage = (id) => {
    history.push("/post/edit/" + id);
  };

  const moveToNewPost = () => {
    history.push("/post/new");
  };

  return (
    <div>
      <Popup open={isConfirmOpen.isOpen} onClose={closeConfirm}>
        <div className="modal">
          <div className="modal-main" style={{ width: 300 }}>
            <div style={{ margin: "2rem" }}>
              <div style={{ marginBottom: "2rem" }}>
                <h2 style={{ textAlign: "center" }}>
                  The post will be permanently deleted!
                </h2>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-around",
                }}
              >
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => onDelete()}
                >
                  Confirm
                </Button>
                <Button
                  variant="contained"
                  color="default"
                  onClick={() => setConfirmOpen({ isOpen: false, id: null })}
                >
                  Cancel
                </Button>
              </div>
            </div>
          </div>
        </div>
      </Popup>
      <MaterialTable
        tableRef={tableRef}
        style={{ boxShadow: "none" }}
        columns={[
          {
            title: "Header",
            field: "postCardImageUrl",
            render: (rowData) => (
              <img
                style={{ width: "100%", borderRadius: 15 }}
                src={rowData.postCardImageUrl}
              />
            ),
            width: null,
            cellStyle: { width: "10%" },
            editable: "never",
          },
          {
            title: "Active",
            field: "isActive",
            render: (rowData) => (
              <Checkbox color="primary" checked={rowData.isActive} />
            ),
            width: null,
            cellStyle: { width: "5%" },
            editComponent: (props) => (
              <div>
                <Checkbox
                  color="primary"
                  checked={props.rowData.isActive}
                  onChange={(e) => props.onChange(e.target.checked)}
                />
              </div>
            ),
          },
          {
            title: "Publication time",
            field: "publicationTime",
            width: null,
            cellStyle: { width: "25%" },
            render: (rowData) => formatDate(rowData.publicationTime),
            editComponent: (props) => (
              <div>
                <TextField
                  type="datetime-local"
                  value={props.rowData.publicationTime}
                  onChange={(e) => props.onChange(e.target.value)}
                />
              </div>
            ),
          },
          {
            title: "Title",
            field: "title",
            width: null,
            cellStyle: { width: "60%" },
            editable: "never",
          },
        ]}
        components={{
          Toolbar: (props) => (
            <div>
              <MTableToolbar {...props} />
              <div
                style={{
                  display: "flex",
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                }}
              >
                <RadioGroup
                  style={{ flexDirection: "row" }}
                  aria-label="Post status"
                  value={state}
                  onChange={statusChangeHandler}
                  // color="primary"
                >
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.published}
                    control={<Radio color="primary" />}
                    label="Published"
                  />
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.moderated}
                    control={<Radio color="primary" />}
                    label="Moderated"
                  />
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.declined}
                    control={<Radio color="primary" />}
                    label="Declined"
                  />
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.inactive}
                    control={<Radio color="primary" />}
                    label="Inactive"
                  />
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.future}
                    control={<Radio color="primary" />}
                    label="Future"
                  />
                  <FormControlLabel
                    labelPlacement="top"
                    value={status.all}
                    control={<Radio color="primary" />}
                    label="All"
                  />
                </RadioGroup>
                <div>
                  <Button
                    color="primary"
                    variant="contained"
                    onClick={moveToNewPost}
                  >
                    New post
                  </Button>
                </div>
              </div>
            </div>
          ),
        }}
        data={(query) =>
          new Promise((resolve, reject) => {
            let url = new URL(window.location.origin + "/posts/user");
            let params = {
              pageNumber: query.page,
              pageSize: query.pageSize,
              status: state,
              sort: "id",
            };
            Object.keys(params).forEach((key) =>
              url.searchParams.append(key, params[key])
            );
            authFetch(url)
              .then((response) => {
                if (response.ok) {
                  return response.json();
                } else if (response.status === 403) {
                  logout();
                  context.showAuth(true);
                  history.push("/");
                  return {
                    postByUserList: [],
                    page: 0,
                    totalElements: 0,
                  };
                } else {
                  throw new Error("Error code: " + response.status);
                }
              })
              .then((result) => {
                resolve({
                  data: result.postByUserList,
                  page: result.page,
                  totalCount: result.totalElements,
                });
              })
              .catch((err) => {
                console.log(err);
                history.push("/error");
              });
          })
        }
        editable={{
          onRowUpdate: (newData, oldData) =>
            new Promise((resolve, reject) => {
              let url = `${window.location.origin}/posts/${newData.id}`;

              authFetch(url, {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json;charset=utf-8",
                },
                body: JSON.stringify({
                  title: null,
                  text: null,
                  publicationTime: newData.publicationTime,
                  isActive: newData.isActive,
                  postCardImageUrl: null,
                }),
              }).then((response) => {
                if (response.ok) {
                  resolve();
                } else if (response.status === 403) {
                  logout();
                  context.showAuth();
                  history.push("/");
                } else {
                  throw new Error("Error code: " + response.status);
                }
              });
            }).catch((err) => {
              console.log(err);
              history.push("/error");
            }),
        }}
        actions={[
          {
            icon: () => <DeleteOutline color="primary" />,
            tooltip: "Delete post",
            onClick: (event, rowData) =>
              setConfirmOpen({ isOpen: true, id: rowData.id }),
          },
          {
            icon: () => <EditIcon />,
            tooltip: "Edit page",
            onClick: (event, rowData) => moveToEditPage(rowData.id),
          },
          {
            icon: () => <OpenInNewIcon />,
            tooltip: "View",
            onClick: (event, rowData) => moveToPostPage(rowData.id),
          },
        ]}
        icons={tableIcons}
        title="All posts list"
        options={{
          actionsColumnIndex: 5,
        }}
      />
    </div>
  );
}

export default UserPostList;
