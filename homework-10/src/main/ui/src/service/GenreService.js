import axios from 'axios';

class GenreService{

    getGenres(){
        return axios.get("/genres");
    }

}

export default new GenreService();