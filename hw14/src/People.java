import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class People {
    private String id;
    private LibraryBookId bookB;
    private LocalDate dateB;
    private HashMap<String, LibraryBookId> booksC;
    private HashMap<String, LocalDate> dateC;
    private HashSet<LibraryBookId> orderBooks;
    private LibraryBookId bookBU;
    private LocalDate dateBU;
    private HashMap<String, LibraryBookId> booksCU;
    private HashMap<String, LocalDate> dateCU;

    public People(String id) {
        this.id = id;
        this.bookB = null;
        this.dateB = null;
        this.booksC = new HashMap<>();
        this.dateC = new HashMap<>();
        this.orderBooks = new HashSet<>();
        this.bookBU = null;
        this.dateBU = null;
        this.booksCU = new HashMap<>();
        this.dateCU = new HashMap<>();
    }

    public boolean containsBookB() {
        return this.bookB != null;
    }

    public void addBookB(LibraryBookId bookB, LocalDate date) {
        this.bookB = bookB;
        this.dateB = date.plusDays(30);
    }

    public boolean containsBookBU() {
        return this.bookBU != null;
    }

    public void addBookBU(LibraryBookId bookBU, LocalDate date) {
        this.bookBU = bookBU;
        this.dateBU = date.plusDays(7);
    }

    public boolean containsBookC(LibraryBookId bookC) {
        return this.booksC.containsKey(bookC.getUid());
    }

    public void addBookC(LibraryBookId bookC, LocalDate date) {
        this.booksC.put(bookC.getUid(), bookC);
        this.dateC.put(bookC.getUid(), date.plusDays(60));
    }

    public boolean containsBookCU(LibraryBookId bookCU) {
        return this.booksCU.containsKey(bookCU.getUid());
    }

    public void addBookCU(LibraryBookId bookCU, LocalDate date) {
        this.booksCU.put(bookCU.getUid(), bookCU);
        this.dateCU.put(bookCU.getUid(), date.plusDays(14));
    }

    public void returnBook(LibraryBookId book) {
        if (book.isTypeB()) {
            this.bookB = null;
            this.dateB = null;
        } else if (book.isTypeC()) {
            this.booksC.remove(book.getUid());
            this.dateC.remove(book.getUid());
        } else if (book.isTypeBU()) {
            this.bookBU = null;
            this.dateBU = null;
        } else {
            this.booksCU.remove(book.getUid());
            this.dateCU.remove(book.getUid());
        }
    }

    public void addOrder(LibraryBookId book) {
        this.orderBooks.add(book);
    }

    public void finishOrder(LibraryBookId book) {
        this.orderBooks.remove(book);
    }

    public boolean containOrder(LibraryBookId book) {
        return this.orderBooks.contains(book);
    }

    public boolean overDue(LibraryBookId book, LocalDate date) {
        if (book.isTypeB()) {
            return date.isAfter(this.dateB);
        } else if (book.isTypeC()) {
            return date.isAfter(this.dateC.get(book.getUid()));
        } else if (book.isTypeBU()) {
            return date.isAfter(this.dateBU);
        } else if (book.isTypeCU()) {
            return date.isAfter(this.dateCU.get(book.getUid()));
        } else {
            return false;
        }
    }

    public boolean canRenew(LibraryBookId book, LocalDate date) {
        if (book.isTypeB()) {
            return !date.isAfter(this.dateB) &&
                    date.isAfter(this.dateB.minusDays(5));
        } else if (book.isTypeC()) {
            LocalDate getC = this.dateC.get(book.getUid());
            return !date.isAfter(getC) &&
                    date.isAfter(getC.minusDays(5));
        } else {
            return false;
        }
    }

    public void renew(LibraryBookId book) {
        if (book.isTypeB()) {
            this.dateB = this.dateB.plusDays(30);
        } else if (book.isTypeC()) {
            LocalDate getC = this.dateC.get(book.getUid());
            this.dateC.put(book.getUid(), getC.plusDays(30));
        }
    }
}
