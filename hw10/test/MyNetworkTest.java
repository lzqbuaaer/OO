import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Person;
import org.junit.Assert;

import java.util.Random;

import static org.junit.Assert.*;

public class MyNetworkTest {

    @org.junit.Test
    public void queryTripleSum() throws EqualPersonIdException, PersonIdNotFoundException, EqualRelationException, RelationNotFoundException {
        for (int rec = 0; rec < 1; rec++) {
            MyNetwork network = new MyNetwork();
            for (int i = 1; i <= 300; i++) {
                network.addPerson(new MyPerson(i, "1", 1));
            }

            for (int i = 1; i < 300; i++) {
                for (int j = i + 1; j <= 300; j++) {
                    network.addRelation(i, j, 5);
                }
            }

            for (int i = 1; i < 300; i++) {
                for (int j = i + 1; j <= 300; j++) {
                    network.modifyRelation(i, j, -50);
                }
            }

            network.queryBlockSum();
            network.isCircle(1,300);
            System.out.println(network.queryTripleSum());
        }
    }
}