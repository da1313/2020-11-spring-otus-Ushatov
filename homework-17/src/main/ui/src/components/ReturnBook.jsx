function ReturnBook({ book, returnBook }) {
  if (book._links.return === undefined) {
    return (
      <button
        className={"button-disabled"}
        disabled={true}
      >
        return
      </button>
    );
  } else {
    return (
      <button onClick={() => returnBook(book)}>
        return
      </button>
    );
  }
}

export default ReturnBook;
