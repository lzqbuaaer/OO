import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.annotation.SendMessage;

import java.time.LocalDate;
import java.util.HashMap;

public class PeopleTable {
    private final HashMap<String, People> peoples;

    public PeopleTable() {
        this.peoples = new HashMap<>();
    }

    public boolean containsPeople(String id) {
        return this.peoples.containsKey(id);
    }

    @SendMessage(from = ":Library", to = ":PeopleTable")
    public void addPeople(String id) {
        if (!containsPeople(id)) {
            this.peoples.put(id, new People(id));
        }
    }

    public boolean containsBookB(String id) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookB();
    }

    public void addBookB(String id, LibraryBookId bookId, LocalDate date) {
        this.peoples.get(id).addBookB(bookId, date);
    }

    public boolean containsBookBU(String id) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookBU();
    }

    public void addBookBU(String id, LibraryBookId bookId, LocalDate date) {
        this.peoples.get(id).addBookBU(bookId, date);
    }

    public boolean containsBookC(String id, LibraryBookId bookId) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookC(bookId);
    }

    public void addBookC(String id, LibraryBookId bookId, LocalDate date) {
        this.peoples.get(id).addBookC(bookId, date);
    }

    public boolean containsBookCU(String id, LibraryBookId bookId) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookCU(bookId);
    }

    public void addBookCU(String id, LibraryBookId bookId, LocalDate date) {
        this.peoples.get(id).addBookCU(bookId, date);
    }

    public void returnBook(String id, LibraryBookId bookId) {
        this.peoples.get(id).returnBook(bookId);
    }

    public void addOrder(String id, LibraryBookId bookId) {
        this.peoples.get(id).addOrder(bookId);
    }

    public void finishOrder(String id, LibraryBookId bookId) {
        this.peoples.get(id).finishOrder(bookId);
    }

    public boolean containOrder(LibraryBookId bookId) {
        for (String id : this.peoples.keySet()) {
            if (this.peoples.get(id).containOrder(bookId)) {
                return true;
            }
        }
        return false;
    }

    public boolean overDue(String id, LibraryBookId bookId, LocalDate date) {
        return this.peoples.get(id).overDue(bookId, date);
    }

    public boolean canRenew(String id, LibraryBookId bookId, LocalDate date) {
        return this.peoples.get(id).canRenew(bookId, date);
    }

    public void renew(String id, LibraryBookId bookId) {
        this.peoples.get(id).renew(bookId);
    }

    public int getCredit(String id) {
        if (this.peoples.containsKey(id)) {
            return this.peoples.get(id).getCredit();
        }
        return 10;
    }

    public void changeCredit(String id, int change) {
        this.peoples.get(id).changeCredit(change);
    }

    public boolean containsOrderB(String id) {
        return this.peoples.get(id).containsOrderB();
    }

    public boolean containsOrderC(String id, LibraryBookId bookId) {
        return this.peoples.get(id).containOrder(bookId);
    }

    public void punishOverDue(LocalDate date) {
        for (String id : this.peoples.keySet()) {
            this.peoples.get(id).punishOverDue(date);
        }
    }
}
