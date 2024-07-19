import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> persons;
    private DisjointSet disjointSet;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disjointSet = new DisjointSet();
    }

    @Override
    public boolean containsPerson(int id) {
        return this.persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        return this.persons.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (!this.containsPerson(person.getId())) {
            this.persons.put(person.getId(), person);
            this.disjointSet.addPerson(person.getId());
        } else {
            throw new MyEqualPersonIdException(person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            person1.addValue(person2, value);
            person2.addValue(person1, value);
            this.disjointSet.addRelation(id1, id2);
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            boolean needDel = person1.modifyValue(person2, value);
            person2.modifyValue(person1, value);
            if (needDel) {
                this.disjointSet.deleteRelation(id1, id2);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return this.persons.get(id1).queryValue(getPerson(id2));
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return this.disjointSet.isCircle(id1, id2);
        }
    }

    @Override
    public int queryBlockSum() {
        return this.disjointSet.getBlockCnt();
    }

    @Override
    public int queryTripleSum() {
        return this.disjointSet.getTripleCnt();
    }

    public Person[] getPersons() {
        return null;
    }
}
