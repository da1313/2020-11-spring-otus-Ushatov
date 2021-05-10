import '../css/BookListSearchComponent.css'

function BookListSearchComponent({onSearch}){
    return (
      <div className="searchFormCont">
        <form onSubmit={(e)=>onSearch(e)}>
          <input type="search" placeholder="Search"/>
          <button type="submit">Search</button>
        </form>
      </div>
    );
}

export default BookListSearchComponent;