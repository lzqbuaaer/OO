import com.oocourse.library1.LibraryBookId;

import java.util.HashMap;

public class PeopleTable {
    private HashMap<String, People> peoples;

    public PeopleTable() {
        this.peoples = new HashMap<>();
    }

    public boolean containsPeople(String id) {
        return this.peoples.containsKey(id);
    }

    public void addPeople(String id) {
        if (!containsPeople(id)) {
            this.peoples.put(id, new People(id));
        }
    }

    public boolean containsBookB(String id) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookB();
    }

    public void addBookB(String id, LibraryBookId bookId) {
        this.peoples.get(id).addBookB(bookId);
    }

    public boolean containsBookC(String id, LibraryBookId bookId) {
        return this.peoples.containsKey(id) &&
                this.peoples.get(id).containsBookC(bookId);
    }

    public void addBookC(String id, LibraryBookId bookId) {
        this.peoples.get(id).addBookC(bookId);
    }

    public void returnBook(String id, LibraryBookId bookId) {
        this.peoples.get(id).returnBook(bookId);
    }
}
