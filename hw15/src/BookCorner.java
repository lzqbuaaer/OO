import com.oocourse.library3.LibraryBookId;

import java.util.HashMap;
import java.util.HashSet;

public class BookCorner {
    private final HashSet<LibraryBookId> books;
    private final HashMap<LibraryBookId, Integer> count;

    public BookCorner() {
        this.books = new HashSet<>();
        this.count = new HashMap<>();
    }

    public boolean contain(LibraryBookId book) {
        return this.books.contains(book);
    }

    public void addBook(LibraryBookId book) {
        this.books.add(book);
        this.count.put(book, 0);
    }

    public void borrow(LibraryBookId book) {
        this.books.remove(book);
    }

    public void returned(LibraryBookId book) {
        this.books.add(book);
    }

    public void delete(LibraryBookId book) {
        this.books.remove(book);
        this.count.remove(book);
    }

    public void addNum(LibraryBookId book) {
        this.count.put(book, this.count.get(book) + 1);
    }

    public int countNum(LibraryBookId book) {
        return this.count.get(book);
    }
}
