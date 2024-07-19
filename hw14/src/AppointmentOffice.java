import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AppointmentOffice {
    private HashMap<LocalDate, HashMap<String, HashSet<LibraryBookId>>> books;
    private HashMap<String, HashSet<LibraryBookId>> bookB;
    private HashMap<String, HashSet<LibraryBookId>> bookC;

    public AppointmentOffice() {
        this.books = new HashMap<>();
        this.bookB = new HashMap<>();
        this.bookC = new HashMap<>();
    }

    public void addBook(LocalDate date, String peopleId, LibraryBookId bookId) {
        if (this.books.containsKey(date)) {
            HashMap<String, HashSet<LibraryBookId>> dateBook = this.books.get(date);
            if (dateBook.containsKey(peopleId)) {
                dateBook.get(peopleId).add(bookId);
            } else {
                HashSet<LibraryBookId> bookSet = new HashSet<>();
                bookSet.add(bookId);
                dateBook.put(peopleId, bookSet);
            }
        } else {
            HashSet<LibraryBookId> bookSet = new HashSet<>();
            bookSet.add(bookId);
            HashMap<String, HashSet<LibraryBookId>> dateBook = new HashMap<>();
            dateBook.put(peopleId, bookSet);
            this.books.put(date, dateBook);
        }
    }

    public HashMap<String, HashSet<LibraryBookId>> getBookB() {
        return this.bookB;
    }

    public HashMap<String, HashSet<LibraryBookId>> getBookC() {
        return this.bookC;
    }

    public void addAppointment(String peopleId, LibraryBookId bookId) {
        if (bookId.isTypeB()) {
            if (this.bookB.containsKey(peopleId)) {
                this.bookB.get(peopleId).add(bookId);
            } else {
                HashSet<LibraryBookId> book = new HashSet<>();
                book.add(bookId);
                this.bookB.put(peopleId, book);
            }
        } else {
            if (this.bookC.containsKey(peopleId)) {
                this.bookC.get(peopleId).add(bookId);
            } else {
                HashSet<LibraryBookId> book = new HashSet<>();
                book.add(bookId);
                this.bookC.put(peopleId, book);
            }
        }
    }

    public boolean fetch(String peopleId, LibraryBookId bookId) {
        LocalDate date = null;
        for (LocalDate d : this.books.keySet()) {
            HashMap<String, HashSet<LibraryBookId>> book = this.books.get(d);
            if (book.containsKey(peopleId) && book.get(peopleId).contains(bookId)) {
                if (date == null || date.isAfter(d)) {
                    date = d;
                }
            }
        }
        if (date == null) {
            return false;
        } else {
            this.books.get(date).get(peopleId).remove(bookId);
            return true;
        }
    }

    public void clear(ArrayList<LibraryMoveInfo> move, LocalDate date, PeopleTable table) {
        ArrayList<LocalDate> day = new ArrayList<>();
        for (LocalDate d : this.books.keySet()) {
            if (ChronoUnit.DAYS.between(d, date) > 4) {
                HashMap<String, HashSet<LibraryBookId>> book = this.books.get(d);
                for (String peopleId : book.keySet()) {
                    for (LibraryBookId bookId : book.get(peopleId)) {
                        move.add(new LibraryMoveInfo(bookId, "ao", "bs"));
                        table.finishOrder(peopleId, bookId);
                    }
                }
                day.add(d);
            }
        }
        for (LocalDate d : day) {
            this.books.remove(d);
        }
    }

    public void clearAppointed() {
        this.bookB.clear();
        this.bookC.clear();
    }
}
