import axios from 'axios';

class CommentService{

    getComments(req){
        return axios.get("/comments", {params: req});
    }

    createComment(req){
        return axios.post("/comments", req);
    }

}

export default new CommentService();