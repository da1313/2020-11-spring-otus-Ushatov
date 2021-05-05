import axios from 'axios'

class HypermediaService {
  async fetchHypermedia() {
    let hypermediaStored = localStorage.getItem("hypermedia");
    let hypermedia =
      hypermediaStored === null
        ? null
        : JSON.parse(localStorage.getItem("hypermedia"));

    if (hypermedia === null) {
      const response = await axios.get("/hypermedia");
      localStorage.setItem("hypermedia", JSON.stringify(response.data._links));
      return response.data._links;
    } else {
      return hypermedia;
    }
  }
}

export default new HypermediaService();
