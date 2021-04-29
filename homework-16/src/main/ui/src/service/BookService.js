import axios from "axios";
import hypermediaService from "./HypermediaService";

class BookService {

  async getBooks() {
    const hypermedia = await hypermediaService.fetchHypermedia();
    return await axios.get(
      hypermedia.books_taken.href//.replace("http://localhost:8080", "")
      );
  }

  getBook() {}

  takeBook(book) {
    return axios.post(
      book._links.take.href//.replace("http://localhost:8080", "")
    );
  }

  returnBook(book) {
    return axios.post(
      book._links.return.href//.replace("http://localhost:8080", "")
    );
  }
}

export default new BookService();
