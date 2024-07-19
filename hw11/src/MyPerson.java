import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.TreeMap;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Iterator;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int socialValue;
    private int money;
    private final TreeMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;
    private final HashMap<Integer, Tag> tags;
    private final LinkedList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
        this.value = new HashMap<>();
        this.tags = new HashMap<>();
        this.messages = new LinkedList<>();
        this.acquaintance = new TreeMap<>((id1, id2) -> {
            int cmp = value.get(id2).compareTo(value.get(id1));
            if (cmp == 0) {
                return id1.compareTo(id2);
            }
            return cmp;
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        return false;
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
    public boolean containsTag(int id) {
        return this.tags.containsKey(id);
    }

    @Override
    public Tag getTag(int id) {
        return this.tags.get(id);
    }

    @Override
    public void addTag(Tag tag) {
        this.tags.put(tag.getId(), tag);
    }

    @Override
    public void delTag(int id) {
        ((MyTag) this.tags.get(id)).delAll(this.id);
        this.tags.remove(id);
    }

    @Override
    public boolean isLinked(Person person) {
        if (this.id == person.getId()) {
            return true;
        }
        return this.value.containsKey(person.getId());
    }

    @Override
    public int queryValue(Person person) {
        if (this.value.containsKey(person.getId())) {
            return this.value.get(person.getId());
        }
        return 0;
    }

    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        LinkedList<Message> receive = new LinkedList<>();
        for (int i = 0; i <= 4 && i < this.messages.size(); i++) {
            receive.add(this.messages.get(i));
        }
        return receive;
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    public void addMessage(Message message) {
        this.messages.addFirst(message);
    }

    public boolean addValue(Person person, int value) {
        Integer bestId = bestAcquaintance();
        this.value.put(person.getId(), value);
        this.acquaintance.put(person.getId(), person);
        Integer newBestId = bestAcquaintance();
        return !Objects.equals(newBestId, bestId);
    }

    public boolean modifyValue(Person person, int value) {
        final Integer bestId = bestAcquaintance();
        int oldValue = this.queryValue(person);
        int id = person.getId();
        if (oldValue + value > 0) {
            this.acquaintance.remove(id);
            this.value.put(id, oldValue + value);
            this.acquaintance.put(id, person);
        } else {
            for (int tagId : this.tags.keySet()) {
                Tag tag = this.tags.get(tagId);
                if (tag.hasPerson(person)) {
                    tag.delPerson(person);
                }
            }
            this.acquaintance.remove(id);
            this.value.remove(id);
        }
        Integer newBestId = bestAcquaintance();
        return !Objects.equals(newBestId, bestId);
    }

    public Integer bestAcquaintance() {
        if (this.acquaintance.isEmpty()) {
            return null;
        }
        return this.acquaintance.firstKey();
    }

    public int acquaintanceSize() {
        return this.acquaintance.size();
    }

    public void clearNotices() {
        Iterator<Message> iterator = this.messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (message instanceof NoticeMessage) {
                iterator.remove();
            }
        }
    }
}
