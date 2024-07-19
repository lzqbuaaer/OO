import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EqualTagIdException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.TagIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Tag;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import myexceptions.MyEmojiIdNotFoundException;
import myexceptions.MyEqualMessageIdException;
import myexceptions.MyEqualRelationException;
import myexceptions.MyEqualTagIdException;
import myexceptions.MyEqualPersonIdException;
import myexceptions.MyRelationNotFoundException;
import myexceptions.MyPersonIdNotFoundException;
import myexceptions.MyMessageIdNotFoundException;
import myexceptions.MyAcquaintanceNotFoundException;
import myexceptions.MyEqualEmojiIdException;
import myexceptions.MyPathNotFoundException;
import myexceptions.MyTagIdNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> persons;
    private final DisjointSet disjointSet;
    private boolean dirty;
    private int coupleSum;
    private final HashMap<Integer, Message> messages;
    private final HashSet<Integer> emojiIdList;
    private final HashMap<Integer, Integer> emojiHeatList;
    private static final HashMap<Integer, HashMap<Integer, HashSet<Integer>>>
            idTag = new HashMap<>();

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.disjointSet = new DisjointSet();
        this.dirty = false;
        this.coupleSum = 0;
        this.messages = new HashMap<>();
        this.emojiIdList = new HashSet<>();
        this.emojiHeatList = new HashMap<>();
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
    public boolean containsMessage(int id) {
        return this.messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 &&
                message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            this.messages.put(message.getId(), message);
        }
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return this.messages.get(id);
        }
        return null;
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, TagIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(
                    getMessage(id).getPerson1().getId(), getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !getMessage(id).getPerson1().containsTag(getMessage(id).getTag().getId())) {
            throw new MyTagIdNotFoundException(getMessage(id).getTag().getId());
        } else {
            Message message = this.messages.get(id);
            this.messages.remove(id);
            if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                this.emojiHeatList.put(emojiId, this.emojiHeatList.get(emojiId) + 1);
                ((MyEmojiMessage) message).send();
            } else if (message instanceof RedEnvelopeMessage) {
                ((MyRedEnvelopeMessage) message).send();
            } else if (message instanceof NoticeMessage) {
                ((MyNoticeMessage) message).send();
            } else {
                ((MyMessage) message).send();
            }
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }

    @Override
    public boolean containsEmojiId(int id) {
        return this.emojiIdList.contains(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            this.emojiIdList.add(id);
            this.emojiHeatList.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return this.persons.get(id).getMoney();
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return this.emojiHeatList.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> iterator = this.emojiHeatList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getValue() < limit) {
                this.emojiIdList.remove(entry.getKey());
                iterator.remove();
            }
        }
        Iterator<Map.Entry<Integer, Message>> msgIter = this.messages.entrySet().iterator();
        while (msgIter.hasNext()) {
            Map.Entry<Integer, Message> entry = msgIter.next();
            Message message = entry.getValue();
            if (message instanceof EmojiMessage) {
                int emojiId = ((EmojiMessage) message).getEmojiId();
                if (!this.emojiIdList.contains(emojiId)) {
                    msgIter.remove();
                }
            }
        }
        return this.emojiIdList.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            ((MyPerson) getPerson(personId)).clearNotices();
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

    public Message[] getMessages() {
        return null;
    }

    public int[] getEmojiIdList() {
        return null;
    }

    public int[] getEmojiHeatList() {
        return null;
    }
}