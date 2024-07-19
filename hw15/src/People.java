import com.oocourse.library3.LibraryBookId;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class People {
    private String id;
    private LibraryBookId bookB;
    private LocalDate dateB;
    private final HashMap<String, LibraryBookId> booksC;
    private final HashMap<String, LocalDate> dateC;
    private final HashSet<LibraryBookId> orderBooks;
    private LibraryBookId bookBU;
    private LocalDate dateBU;
    private final HashMap<String, LibraryBookId> booksCU;
    private final HashMap<String, LocalDate> dateCU;
    private int credit;
    private boolean hasOrderB;
    private LocalDate dateLine;

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
        this.credit = 10;
        this.hasOrderB = false;
        this.dateLine = LocalDate.of(2024, 1, 1);
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
        if (book.isTypeB()) {
            this.hasOrderB = true;
        }
    }

    public void finishOrder(LibraryBookId book) {
        this.orderBooks.remove(book);
        if (book.isTypeB()) {
            this.hasOrderB = false;
        }
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
        if (this.credit < 0) {
            return false;
        }
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

    public int getCredit() {
        return this.credit;
    }

    public void changeCredit(int change) {
        this.credit = Math.min(20, this.credit + change);
    }

    public boolean containsOrderB() {
        return this.hasOrderB;
    }

    public void punishOverDue(LocalDate date) {
        if (this.dateB != null && date.isAfter(this.dateB) &&
                !this.dateLine.isAfter(this.dateB)) {
            this.credit -= 2;
        }
        if (this.dateBU != null && date.isAfter(this.dateBU) &&
                !this.dateLine.isAfter(this.dateBU)) {
            this.credit -= 2;
        }
        for (String uid : this.dateC.keySet()) {
            LocalDate thisDate = this.dateC.get(uid);
            if (date.isAfter(thisDate) && !this.dateLine.isAfter(thisDate)) {
                this.credit -= 2;
            }
        }
        for (String uid : this.dateCU.keySet()) {
            LocalDate thisDate = this.dateCU.get(uid);
            if (date.isAfter(thisDate) && !this.dateLine.isAfter(thisDate)) {
                this.credit -= 2;
            }
        }
        this.dateLine = date;
    }
}
