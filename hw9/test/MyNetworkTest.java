import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.main.Person;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MyNetworkTest {

    @Test
    public void queryTripleSum() throws PersonIdNotFoundException, EqualRelationException, EqualPersonIdException {
        for (int m = 1; m <= 20; m++) {
            MyNetwork[] nets = createNet();
            MyNetwork oldNet = nets[0];
            MyNetwork net = nets[1];
            int result = 0;
            Person[] persons = net.getPersons();
            for (int i = 0; i < persons.length; i++) {
                for (int j = i + 1; j < persons.length; j++) {
                    for (int k = j + 1; k < persons.length; k++) {
                        if (net.getPerson(persons[i].getId()).isLinked(net.getPerson(persons[j].getId()))
                                && net.getPerson(persons[i].getId()).isLinked(net.getPerson(persons[k].getId()))
                                && net.getPerson(persons[k].getId()).isLinked(net.getPerson(persons[j].getId()))) {
                            result++;
                        }
                    }
                }
            }
            assertEquals(result, net.queryTripleSum());
            Person[] oldPersons = oldNet.getPersons();
            persons = net.getPersons();
            assertEquals(persons.length, oldPersons.length);
            for (int i = 0; i < persons.length; i++) {
                MyPerson person = (MyPerson) persons[i];
                boolean f = false;
                for (int j = 0; j < oldPersons.length; j++) {
                    if (person.strictEquals(oldPersons[j])) {
                        f = true;
                        break;
                    }
                }
                assertTrue(f);
            }
        }
    }

    public MyNetwork[] createNet() throws EqualPersonIdException, PersonIdNotFoundException, EqualRelationException {
        MyNetwork net1 = new MyNetwork();
        MyNetwork net2 = new MyNetwork();
        Random random = new Random();
        for (int i = 1; i <= 50; i++) {
            net1.addPerson(new MyPerson(i, "person" + i, 20 + i));
            net2.addPerson(new MyPerson(i, "person" + i, 20 + i));
        }
        for (int i = 1; i <= 50; i++) {
            for (int j = i + 1; j <= 50; j++) {
                if (random.nextInt(2) == 1) {
                    net1.addRelation(i, j, 100 * i + j);
                    net2.addRelation(i, j, 100 * i + j);
                }
            }
        }
        return new MyNetwork[]{net1, net2};
    }
}