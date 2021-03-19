import axios from 'axios';

class AuthorService{

    getAuthors(){
        return axios.get("/authors");
    }

}

export default new AuthorService();