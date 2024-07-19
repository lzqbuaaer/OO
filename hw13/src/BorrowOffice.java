import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;

import java.util.ArrayList;

public class BorrowOffice {
    private ArrayList<LibraryBookId> books;

    public BorrowOffice() {
        this.books = new ArrayList<>();
    }

    public void addBook(LibraryBookId book) {
        this.books.add(book);
    }

    public void clear(ArrayList<LibraryMoveInfo> move) {
        for (LibraryBookId book : this.books) {
            move.add(new LibraryMoveInfo(book, "bro", "bs"));
        }
        this.books.clear();
    }
}
