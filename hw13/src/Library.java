import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;
import com.oocourse.library1.LibraryRequest;
import com.oocourse.library1.LibrarySystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Library {
    private HashMap<LibraryBookId, Integer> shelf;
    private BorrowOffice borrowOffice;
    private AppointmentOffice appointmentOffice;
    private LocalDate date;
    private PeopleTable peopleTable;

    public Library(Map<LibraryBookId, Integer> map) {
        this.shelf = new HashMap<>();
        this.borrowOffice = new BorrowOffice();
        this.appointmentOffice = new AppointmentOffice();
        for (LibraryBookId id : map.keySet()) {
            this.shelf.put(id, map.get(id));
        }
        this.peopleTable = new PeopleTable();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void open(LocalDate date) {
        setDate(date);
        ArrayList<LibraryMoveInfo> move = new ArrayList<>();
        this.borrowOffice.clear(move);
        this.appointmentOffice.clear(move, date);
        for (LibraryMoveInfo inf : move) {
            LibraryBookId bookId = inf.getBookId();
            this.shelf.put(bookId, this.shelf.get(bookId) + 1);
        }
        send(move, this.appointmentOffice.getBookB());
        send(move, this.appointmentOffice.getBookC());
        this.appointmentOffice.clearAppointed();
        LibrarySystem.PRINTER.move(this.date, move);
    }

    public void send(ArrayList<LibraryMoveInfo> move,
                     HashMap<String, HashSet<LibraryBookId>> books) {
        for (String peopleId : books.keySet()) {
            for (LibraryBookId bookId : books.get(peopleId)) {
                if (this.shelf.get(bookId) > 0) {
                    this.shelf.put(bookId, this.shelf.get(bookId) - 1);
                    move.add(new LibraryMoveInfo(bookId, "bs", "ao", peopleId));
                    this.appointmentOffice.addBook(this.date, peopleId, bookId);
                }
            }
        }
    }

    public void close() {
        LibrarySystem.PRINTER.move(this.date, new ArrayList<>());
    }

    public void handleRequest(LibraryRequest request) {
        LibraryRequest.Type type = request.getType();
        if (type.equals(LibraryRequest.Type.QUERIED)) {
            queried(request);
        } else if (type.equals(LibraryRequest.Type.BORROWED)) {
            borrowed(request);
        } else if (type.equals(LibraryRequest.Type.ORDERED)) {
            ordered(request);
        } else if (type.equals(LibraryRequest.Type.RETURNED)) {
            returned(request);
        } else {
            picked(request);
        }
    }

    public void queried(LibraryRequest request) {
        LibraryBookId id = request.getBookId();
        if (this.shelf.containsKey(id)) {
            int num = this.shelf.get(id);
            LibrarySystem.PRINTER.info(this.date, id, num);
            return;
        }
        LibrarySystem.PRINTER.info(this.date, id, 0);
    }

    public void borrowed(LibraryRequest request) {
        String peopleId = request.getStudentId();
        this.peopleTable.addPeople(peopleId);
        LibraryBookId id = request.getBookId();
        if (this.shelf.get(id) > 0 && !id.isTypeA()) {
            this.shelf.put(id, this.shelf.get(id) - 1);
            if (id.isTypeB()) {
                if (this.peopleTable.containsBookB(peopleId)) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookB(peopleId, id);
                    LibrarySystem.PRINTER.accept(this.date, request);
                    return;
                }
            } else {
                if (this.peopleTable.containsBookC(peopleId, id)) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookC(peopleId, id);
                    LibrarySystem.PRINTER.accept(this.date, request);
                    return;
                }
            }
        }
        LibrarySystem.PRINTER.reject(this.date, request);
    }

    public void ordered(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String peopleId = request.getStudentId();
        this.peopleTable.addPeople(peopleId);
        if (bookId.isTypeB()) {
            if (!this.peopleTable.containsBookB(peopleId)) {
                LibrarySystem.PRINTER.accept(this.date, request);
                this.appointmentOffice.addAppointment(peopleId, bookId);
                return;
            }
        } else if (bookId.isTypeC()) {
            if (!this.peopleTable.containsBookC(peopleId, bookId)) {
                LibrarySystem.PRINTER.accept(this.date, request);
                this.appointmentOffice.addAppointment(peopleId, bookId);
                return;
            }
        }
        LibrarySystem.PRINTER.reject(this.date, request);
    }

    public void returned(LibraryRequest request) {
        LibraryBookId id = request.getBookId();
        this.borrowOffice.addBook(id);
        LibrarySystem.PRINTER.accept(this.date, request);
        this.peopleTable.returnBook(request.getStudentId(), id);
    }

    public void picked(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String peopleId = request.getStudentId();
        if (bookId.isTypeB()) {
            if (!this.peopleTable.containsBookB(peopleId)) {
                if (this.appointmentOffice.fetch(peopleId, bookId)) {
                    this.peopleTable.addBookB(peopleId, bookId);
                    LibrarySystem.PRINTER.accept(this.date, request);
                    return;
                }
            }
        } else if (bookId.isTypeC()) {
            if (!this.peopleTable.containsBookC(peopleId, bookId)) {
                if (this.appointmentOffice.fetch(peopleId, bookId)) {
                    this.peopleTable.addBookC(peopleId, bookId);
                    LibrarySystem.PRINTER.accept(this.date, request);
                    return;
                }
            }
        }
        LibrarySystem.PRINTER.reject(this.date, request);
    }
}
