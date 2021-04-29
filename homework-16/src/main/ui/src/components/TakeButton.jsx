function TakeButton({book, takeBook}){

    if(book._links.take === undefined){
        return(
            <button className={'button-disabled'} disabled={true}>take</button>
        );
    } else{
        return(
            <button onClick={() => takeBook(book)}>take</button>
        );
    }

}

export default TakeButton;