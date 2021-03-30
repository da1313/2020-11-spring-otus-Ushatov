import axios from 'axios';

class BookService{

    getBookById(id){
        return axios.get("/books/" + id);
    }

    getBooks(params){
        return axios.get("/books", {params: params});
    }

    createBook(book){
        return axios.post("/books/", book);
    }

    updateBook(book){
        return axios.put("/books/" + book.id, {title: book.title, authorId: book.authorId, genresId: book.genresId});
    }

    deleteBook(id){
        return axios.delete("/books/" + id);
    }

}

export default new BookService();