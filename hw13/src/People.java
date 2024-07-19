import com.oocourse.library1.LibraryBookId;

import java.util.HashMap;

public class People {
    private String id;
    private LibraryBookId bookB;
    private HashMap<String, LibraryBookId> booksC;

    public People(String id) {
        this.id = id;
        this.bookB = null;
        this.booksC = new HashMap<>();
    }

    public boolean containsBookB() {
        return this.bookB != null;
    }

    public void addBookB(LibraryBookId bookB) {
        this.bookB = bookB;
    }

    public boolean containsBookC(LibraryBookId bookC) {
        return this.booksC.containsKey(bookC.getUid());
    }

    public void addBookC(LibraryBookId bookC) {
        this.booksC.put(bookC.getUid(), bookC);
    }

    public void returnBook(LibraryBookId book) {
        if (book.isTypeB()) {
            this.bookB = null;
        } else {
            this.booksC.remove(book.getUid());
        }
    }
}
