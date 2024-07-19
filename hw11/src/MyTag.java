import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;

public class MyTag implements Tag {
    private final int id;
    private final HashMap<Integer, Person> persons;
    private int ageSum;
    private int ageVarSum;
    private int valueSum;

    public MyTag(int id) {
        this.id = id;
        this.persons = new HashMap<>();
        this.ageSum = 0;
        this.ageVarSum = 0;
        this.valueSum = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return (((Tag) obj).getId() == this.id);
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        this.ageSum += person.getAge();
        this.ageVarSum += person.getAge() * person.getAge();
        for (int id : this.persons.keySet()) {
            this.valueSum += (2 * this.persons.get(id).queryValue(person));
        }
        int personId = person.getId();
        this.persons.put(personId, person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return this.persons.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return this.valueSum;
    }

    @Override
    public int getAgeMean() {
        if (getSize() == 0) {
            return 0;
        }
        return this.ageSum / getSize();
    }

    @Override
    public int getAgeVar() {
        if (getSize() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        int n = getSize();
        return (this.ageVarSum - 2 * this.ageSum * mean + n * mean * mean) / n;
    }

    @Override
    public void delPerson(Person person) {
        this.persons.remove(person.getId());
        this.ageSum -= person.getAge();
        this.ageVarSum -= person.getAge() * person.getAge();
        for (int id : this.persons.keySet()) {
            this.valueSum -= (2 * this.persons.get(id).queryValue(person));
        }
    }

    @Override
    public int getSize() {
        return this.persons.size();
    }

    public void delAll(int personId2) {
        for (Integer personId1 : this.persons.keySet()) {
            MyNetwork.delIdTag(personId1, personId2, this.id);
        }
    }

    public void modifyValueSum(int id1, int id2, int value) {
        if (this.persons.containsKey(id1) && this.persons.containsKey(id2)) {
            this.valueSum += (2 * value);
        }
    }

    public void addSocialValue(int socialValue) {
        for (Integer id : this.persons.keySet()) {
            this.persons.get(id).addSocialValue(socialValue);
        }
    }

    public void addMoney(int money) {
        for (Integer id : this.persons.keySet()) {
            this.persons.get(id).addMoney(money);
        }
    }
}
