import '../css/PagingComponent.css';

function PagingComponent({ pageParams, onPageChange }) {

  const currentPage = pageParams.currentPage + 1;
  const maxPages = pageParams.maxPages;

  const nextPage = currentPage + 1 > maxPages ? currentPage : currentPage + 1;
  const previousPage = currentPage - 1 < 1 ? currentPage : currentPage - 1;

  const leftOffset = 2;
  const rightOffset = 2;

  const getPageNumbers = () =>{
    let items = [];
    
    let addToRight = (currentPage - leftOffset) < 1 ? Math.abs(currentPage - leftOffset) + 1 : 0;
    let addToLeft = (currentPage + rightOffset) > maxPages ? (currentPage + rightOffset - maxPages) : 0;
    let actulaRightOffset = (currentPage + rightOffset + addToRight) > maxPages ? maxPages : currentPage + rightOffset + addToRight;
    let actualLeftOffset = (currentPage - leftOffset - addToLeft) < 1 ? 1 : currentPage - leftOffset - addToLeft;

    for (let i = actualLeftOffset; i < currentPage; i++) {
      items.push(
        <li key={i}>
          <button  onClick={() => onPageChange(i - 1)}>{i}</button>
        </li>
      );
    }
    items.push(
      <li key={currentPage} >
        <button className="active" onClick={() => onPageChange(currentPage - 1)}>{currentPage}</button>
      </li>
    );
    for (let i = currentPage + 1; i < actulaRightOffset + 1; i++) {
      items.push(
        <li key={i} >
          <button  onClick={() => onPageChange(i - 1)}>{i}</button>
        </li>
      );
    }
    return items;
  }
  
  return (
    <div className="pagingCont">
      <ul>
        <li >
            <button  onClick={() => onPageChange(0)}>First</button>
          </li>
          <li >
            <button  onClick={() => onPageChange(previousPage - 1)}>Previous</button>
          </li>
          {getPageNumbers()}
          <li >
            <button  onClick={() => onPageChange(nextPage - 1)}>Next</button>
          </li>
          <li >
            <button  onClick={() => onPageChange(maxPages - 1)}>Last</button>
          </li>
        </ul>
    </div>
  );
}

export default PagingComponent;
