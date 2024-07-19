import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualTagIdException;
import com.oocourse.spec2.exceptions.PathNotFoundException;
import com.oocourse.spec2.exceptions.TagIdNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;
import java.util.HashSet;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private final DisjointSet disjointSet;
    private boolean dirty;
    private int coupleSum;
    private static HashMap<Integer, HashMap<Integer, HashSet<Integer>>> idTag = new HashMap<>();

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disjointSet = new DisjointSet();
        this.dirty = false;
        this.coupleSum = 0;
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
            if (person1.addValue(person2, value)) {
                this.dirty = true;
            }
            if (person2.addValue(person1, value)) {
                this.dirty = true;
            }
            this.disjointSet.addRelation(id1, id2);
            modifyTagValueSum(id1, id2, value);
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
            int oldValue = person1.queryValue(person2);
            if (person1.modifyValue(person2, value)) {
                this.dirty = true;
            }
            if (person2.modifyValue(person1, value)) {
                this.dirty = true;
            }
            if (oldValue + value <= 0) {
                this.disjointSet.deleteRelation(id1, id2);
                modifyTagValueSum(id1, id2, -oldValue);
            } else {
                modifyTagValueSum(id1, id2, value);
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

    @Override
    public void addTag(int personId, Tag tag)
            throws PersonIdNotFoundException, EqualTagIdException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (getPerson(personId).containsTag(tag.getId())) {
            throw new MyEqualTagIdException(tag.getId());
        } else {
            getPerson(personId).addTag(tag);
        }
    }

    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, RelationNotFoundException,
            TagIdNotFoundException, EqualPersonIdException {
        Person person1 = getPerson(personId1);
        Person person2 = getPerson(personId2);
        if (!this.containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        } else if (!this.containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        } else if (personId1 == personId2) {
            throw new MyEqualPersonIdException(personId1);
        } else if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(personId1, personId2);
        } else if (!person2.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else if (person2.getTag(tagId).hasPerson(person1)) {
            throw new MyEqualPersonIdException(personId1);
        } else if (person2.getTag(tagId).getSize() <= 1111) {
            person2.getTag(tagId).addPerson(person1);
            MyNetwork.addIdTag(personId1, personId2, tagId);
        }
    }

    @Override
    public int queryTagValueSum(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            return getPerson(personId).getTag(tagId).getValueSum();
        }
    }

    @Override
    public int queryTagAgeVar(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            return getPerson(personId).getTag(tagId).getAgeVar();
        }
    }

    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        Person person1 = getPerson(personId1);
        Person person2 = getPerson(personId2);
        if (!this.containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        } else if (!this.containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        } else if (!person2.containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else if (!person2.getTag(tagId).hasPerson(person1)) {
            throw new MyPersonIdNotFoundException(personId1);
        } else {
            person2.getTag(tagId).delPerson(person1);
            MyNetwork.delIdTag(personId1, personId2, tagId);
        }
    }

    @Override
    public void delTag(int personId, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!this.containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            getPerson(personId).delTag(tagId);
        }
    }

    @Override
    public int queryBestAcquaintance(int id)
            throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!this.containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else if (((MyPerson) getPerson(id)).acquaintanceSize() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            return ((MyPerson) getPerson(id)).bestAcquaintance();
        }
    }

    @Override
    public int queryCoupleSum() {
        if (this.dirty) {
            this.coupleSum = 0;
            HashSet<Integer> set = new HashSet<>();
            for (Integer id : this.persons.keySet()) {
                if (!set.contains(id)) {
                    Integer bestId = ((MyPerson) this.persons.get(id)).bestAcquaintance();
                    if (bestId != null &&
                            id.equals(((MyPerson) this.persons.get(bestId)).bestAcquaintance())) {
                        this.coupleSum++;
                        set.add(bestId);
                    }
                    set.add(id);
                }
            }
            this.dirty = false;
        }
        return this.coupleSum;
    }

    @Override
    public int queryShortestPath(int id1, int id2)
            throws PersonIdNotFoundException, PathNotFoundException {
        if (!this.containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!this.containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2);
        } else {
            return disjointSet.shortestPath(id1, id2) - 1;
        }
    }

    public Person[] getPersons() {
        return null;
    }

    public static void addIdTag(int id1, int id2, int tagId) {
        if (idTag.containsKey(id1)) {
            HashMap<Integer, HashSet<Integer>> tagSet = idTag.get(id1);
            if (tagSet.containsKey(id2)) {
                tagSet.get(id2).add(tagId);
            } else {
                HashSet<Integer> set = new HashSet<>();
                set.add(tagId);
                tagSet.put(id2, set);
            }
        } else {
            HashMap<Integer, HashSet<Integer>> tagSet = new HashMap<>();
            HashSet<Integer> set = new HashSet<>();
            set.add(tagId);
            tagSet.put(id2, set);
            idTag.put(id1, tagSet);
        }
    }

    public static void delIdTag(int id1, int id2, int tagId) {
        HashMap<Integer, HashSet<Integer>> tagSet = idTag.get(id1);
        tagSet.get(id2).remove(tagId);
    }

    public void modifyTagValueSum(int id1, int id2, int value) {
        if (idTag.containsKey(id1)) {
            HashMap<Integer, HashSet<Integer>> tagSet = idTag.get(id1);
            for (int id : tagSet.keySet()) {
                HashSet<Integer> set = tagSet.get(id);
                MyPerson person = (MyPerson) this.persons.get(id);
                for (int tagId : set) {
                    MyTag tag = (MyTag) person.getTag(tagId);
                    if (tag != null) {
                        tag.modifyValueSum(id1, id2, value);
                    }
                }
            }
        }
    }
}
