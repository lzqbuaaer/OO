import com.oocourse.library1.LibraryCommand;
import com.oocourse.library1.LibraryRequest;

import static com.oocourse.library1.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) {
        Library library = new Library(SCANNER.getInventory());
        while (true) {
            LibraryCommand<?> command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            if (command.getCmd().equals("OPEN")) {
                library.open(command.getDate());
            } else if (command.getCmd().equals("CLOSE")) {
                library.close();
            } else {
                LibraryRequest request = (LibraryRequest) command.getCmd();
                library.handleRequest(request);
            }
        }
    }

}
