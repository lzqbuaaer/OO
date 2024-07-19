import com.oocourse.library3.LibraryRequest;
import com.oocourse.library3.LibraryReqCmd;
import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;
import com.oocourse.library3.LibrarySystem;
import com.oocourse.library3.LibraryQcsCmd;
import com.oocourse.library3.annotation.SendMessage;
import com.oocourse.library3.annotation.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Library {
    private final HashMap<LibraryBookId, Integer> shelf;
    private final BorrowOffice borrowOffice;
    private final AppointmentOffice appointmentOffice;
    private LocalDate date;
    private final PeopleTable peopleTable;
    private final BookCorner bookCorner;
    private final HashMap<LibraryBookId, String> donor;

    public Library(Map<LibraryBookId, Integer> map) {
        this.shelf = new HashMap<>();
        this.borrowOffice = new BorrowOffice();
        this.appointmentOffice = new AppointmentOffice();
        for (LibraryBookId id : map.keySet()) {
            this.shelf.put(id, map.get(id));
        }
        this.peopleTable = new PeopleTable();
        this.bookCorner = new BookCorner();
        this.donor = new HashMap<>();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Trigger(from = "bro", to = {"bs", "bdc"})
    @Trigger(from = "ao", to = "bs")
    @Trigger(from = "bs", to = "ao")
    public void open(LocalDate date) {
        setDate(date);
        this.peopleTable.punishOverDue(this.date);
        ArrayList<LibraryMoveInfo> move = new ArrayList<>();
        this.borrowOffice.clear(move, this.bookCorner);
        this.appointmentOffice.clear(move, date, this.peopleTable);
        for (LibraryMoveInfo inf : move) {
            LibraryBookId bookId = inf.getBookId();
            if (bookId.isTypeBU() || bookId.isTypeCU()) {
                if (this.bookCorner.countNum(bookId) == 2) {
                    this.bookCorner.delete(bookId);
                    this.peopleTable.changeCredit(this.donor.get(bookId), 2);
                    if (bookId.isTypeBU()) {
                        this.shelf.put(
                                new LibraryBookId(LibraryBookId.Type.B, bookId.getUid()), 1);
                    } else {
                        this.shelf.put(
                                new LibraryBookId(LibraryBookId.Type.C, bookId.getUid()), 1);
                    }
                } else {
                    this.bookCorner.returned(bookId);
                }
            } else {
                this.shelf.put(bookId, this.shelf.get(bookId) + 1);
            }
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
            orderNewBook(request);
        } else if (type.equals(LibraryRequest.Type.RETURNED)) {
            returned(request);
        } else if (type.equals(LibraryRequest.Type.PICKED)) {
            getOrderedBook(request);
        } else if (type.equals(LibraryRequest.Type.RENEWED)) {
            renewed(request);
        } else {
            donated(request);
        }
    }

    public void queried(LibraryRequest request) {
        LibraryBookId id = request.getBookId();
        if (this.shelf.containsKey(id)) {
            int num = this.shelf.get(id);
            LibrarySystem.PRINTER.info(this.date, id, num);
            return;
        } else if (this.bookCorner.contain(id)) {
            LibrarySystem.PRINTER.info(this.date, id, 1);
            return;
        }
        LibrarySystem.PRINTER.info(this.date, id, 0);
    }

    @Trigger(from = "bs", to = {"user", "bro"})
    @Trigger(from = "bdc", to = {"user", "bro"})
    public void borrowed(LibraryRequest request) {
        String peopleId = request.getStudentId();
        this.peopleTable.addPeople(peopleId);
        LibraryBookId id = request.getBookId();
        if (this.shelf.containsKey(id) && this.shelf.get(id) > 0 && !id.isTypeA()) {
            this.shelf.put(id, this.shelf.get(id) - 1);
            if (id.isTypeB()) {
                if (this.peopleTable.containsBookB(peopleId)
                        || this.peopleTable.getCredit(peopleId) < 0) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookB(peopleId, id, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    return;
                }
            } else {
                if (this.peopleTable.containsBookC(peopleId, id)
                        || this.peopleTable.getCredit(peopleId) < 0) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookC(peopleId, id, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    return;
                }
            }
        } else if (this.bookCorner.contain(id) && !id.isTypeAU()) {
            this.bookCorner.borrow(id);
            if (id.isTypeBU()) {
                if (this.peopleTable.containsBookBU(peopleId)
                        || this.peopleTable.getCredit(peopleId) < 0) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookBU(peopleId, id, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    this.bookCorner.addNum(id);
                    return;
                }
            } else {
                if (this.peopleTable.containsBookCU(peopleId, id)
                        || this.peopleTable.getCredit(peopleId) < 0) {
                    this.borrowOffice.addBook(id);
                } else {
                    this.peopleTable.addBookCU(peopleId, id, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    this.bookCorner.addNum(id);
                    return;
                }
            }
        }
        LibrarySystem.PRINTER.reject(new LibraryReqCmd(this.date, request));
    }

    @SendMessage(from = ":Main", to = ":Library")
    public void orderNewBook(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String peopleId = request.getStudentId();
        this.peopleTable.addPeople(peopleId);
        if (bookId.isTypeB() && !this.peopleTable.containsOrderB(peopleId) &&
                this.peopleTable.getCredit(peopleId) >= 0) {
            if (!this.peopleTable.containsBookB(peopleId)) {
                LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                this.appointmentOffice.addAppointment(peopleId, bookId);
                this.peopleTable.addOrder(peopleId, bookId);
                return;
            }
        } else if (bookId.isTypeC() && !this.peopleTable.containsOrderC(peopleId, bookId) &&
                this.peopleTable.getCredit(peopleId) >= 0) {
            if (!this.peopleTable.containsBookC(peopleId, bookId)) {
                LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                this.appointmentOffice.addAppointment(peopleId, bookId);
                this.peopleTable.addOrder(peopleId, bookId);
                return;
            }
        }
        LibrarySystem.PRINTER.reject(new LibraryReqCmd(this.date, request));
    }

    @Trigger(from = "user", to = "bro")
    public void returned(LibraryRequest request) {
        LibraryBookId id = request.getBookId();
        String peopleId = request.getStudentId();
        this.borrowOffice.addBook(id);
        if (this.peopleTable.overDue(peopleId, id, this.date)) {
            LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request), "overdue");
        } else {
            LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request), "not overdue");
            this.peopleTable.changeCredit(peopleId, 1);
        }
        this.peopleTable.returnBook(peopleId, id);
    }

    @Trigger(from = "ao", to = "user")
    @SendMessage(from = ":Library", to = ":PeopleTable")
    public void getOrderedBook(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String peopleId = request.getStudentId();
        if (bookId.isTypeB()) {
            if (!this.peopleTable.containsBookB(peopleId)) {
                if (this.appointmentOffice.fetch(peopleId, bookId)) {
                    this.peopleTable.addBookB(peopleId, bookId, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    this.peopleTable.finishOrder(peopleId, bookId);
                    return;
                }
            }
        } else if (bookId.isTypeC()) {
            if (!this.peopleTable.containsBookC(peopleId, bookId)) {
                if (this.appointmentOffice.fetch(peopleId, bookId)) {
                    this.peopleTable.addBookC(peopleId, bookId, this.date);
                    LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                    this.peopleTable.finishOrder(peopleId, bookId);
                    return;
                }
            }
        }
        LibrarySystem.PRINTER.reject(new LibraryReqCmd(this.date, request));
    }

    public void renewed(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String peopleId = request.getStudentId();
        if (this.peopleTable.canRenew(peopleId, bookId, this.date)) {
            if (!(this.shelf.get(bookId) == 0 && this.peopleTable.containOrder(bookId))) {
                this.peopleTable.renew(peopleId, bookId);
                LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
                return;
            }
        }
        LibrarySystem.PRINTER.reject(new LibraryReqCmd(this.date, request));
    }

    public void donated(LibraryRequest request) {
        String peopleId = request.getStudentId();
        this.peopleTable.addPeople(peopleId);
        LibraryBookId id = request.getBookId();
        this.bookCorner.addBook(id);
        this.peopleTable.changeCredit(request.getStudentId(), 2);
        this.donor.put(id, request.getStudentId());
        LibrarySystem.PRINTER.accept(new LibraryReqCmd(this.date, request));
    }

    public void handleQcsRequest(LibraryQcsCmd command) {
        String id = command.getStudentId();
        int credit = this.peopleTable.getCredit(id);
        LibrarySystem.PRINTER.info(this.date, id, credit);
    }
}
