import com.oocourse.library2.LibraryCloseCmd;
import com.oocourse.library2.LibraryCommand;
import com.oocourse.library2.LibraryOpenCmd;
import com.oocourse.library2.LibraryRequest;
import com.oocourse.library2.LibraryReqCmd;

import static com.oocourse.library2.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) {
        Library library = new Library(SCANNER.getInventory());
        while (true) {
            LibraryCommand command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            if (command instanceof LibraryOpenCmd) {
                library.open(command.getDate());
            } else if (command instanceof LibraryCloseCmd) {
                library.close();
            } else {
                LibraryRequest req = ((LibraryReqCmd) command).getRequest();
                library.handleRequest(req);
            }
        }
    }

}
