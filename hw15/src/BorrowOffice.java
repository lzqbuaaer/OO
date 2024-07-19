import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;

import java.util.ArrayList;

public class BorrowOffice {
    private final ArrayList<LibraryBookId> books;

    public BorrowOffice() {
        this.books = new ArrayList<>();
    }

    public void addBook(LibraryBookId book) {
        this.books.add(book);
    }

    public void clear(ArrayList<LibraryMoveInfo> move, BookCorner bookCorner) {
        for (LibraryBookId book : this.books) {
            if (book.isTypeBU() || book.isTypeCU()) {
                if (bookCorner.countNum(book) <= 1) {
                    move.add(new LibraryMoveInfo(book, "bro", "bdc"));
                    continue;
                }
            }
            move.add(new LibraryMoveInfo(book, "bro", "bs"));
        }
        this.books.clear();
    }
}
