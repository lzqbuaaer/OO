import com.oocourse.library3.LibraryRequest;
import com.oocourse.library3.LibraryReqCmd;
import com.oocourse.library3.LibraryQcsCmd;
import com.oocourse.library3.LibraryCommand;
import com.oocourse.library3.LibraryOpenCmd;
import com.oocourse.library3.LibraryCloseCmd;

import static com.oocourse.library3.LibrarySystem.SCANNER;

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
            } else if (command instanceof LibraryQcsCmd) {
                library.handleQcsRequest((LibraryQcsCmd) command);
            } else {
                LibraryRequest req = ((LibraryReqCmd) command).getRequest();
                library.handleRequest(req);
            }
        }
    }

}
