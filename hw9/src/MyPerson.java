import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
    }

    @Override
    public boolean isLinked(Person person) {
        if (this.id == person.getId()) {
            return true;
        }
        return this.acquaintance.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        if (this.value.containsKey(person.getId())) {
            return this.value.get(person.getId());
        }
        return 0;
    }

    public void addValue(Person person, int value) {
        this.acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
    }

    public boolean modifyValue(Person person, int value) {
        int oldValue = this.queryValue(person);
        int id = person.getId();
        if (oldValue + value > 0) {
            this.value.put(id, oldValue + value);
            return false;
        } else {
            this.acquaintance.remove(id);
            this.value.remove(id);
            return true;
        }
    }

    public boolean strictEquals(Person person) {
        return true;
    }
}
