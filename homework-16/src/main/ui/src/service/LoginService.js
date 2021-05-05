import axios from "axios";

class LoginService {

  async sendLogin(username, password, context) {
    let formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);
    const response = await axios.post("/login", formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });
    let isSucceed = response.data.result;
    if (isSucceed) {
      localStorage.setItem("isAuthenticated", true);
      localStorage.setItem("user", response.data.user);
      localStorage.setItem("date", new Date().getTime());
      context.setState({
        isAuthenticated : true,
        user: response.data.user
        });
      return true;
    } else {
      return false;
    }
  }

  sendLogout(){
    axios.get("/logout").then(response => {
      if(response.data.result){
        localStorage.clear();
      }
    });
  }

}

export default new LoginService();
