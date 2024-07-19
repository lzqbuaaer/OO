import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Tag tag;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.tag = null;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Tag messageTag) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 1;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.tag = messageTag;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public Person getPerson1() {
        return this.person1;
    }

    @Override
    public Person getPerson2() {
        return this.person2;
    }

    @Override
    public Tag getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return (((Message) obj).getId() == id);
        }
        return false;
    }

    public void send() {
        if (this.type == 0) {
            send0();
        } else {
            send1();
        }
    }

    public void send0() {
        this.person1.addSocialValue(this.socialValue);
        this.person2.addSocialValue(this.socialValue);
        ((MyPerson) this.person2).addMessage(this);
    }

    public void send1() {
        this.person1.addSocialValue(this.socialValue);
        ((MyTag) this.tag).addSocialValue(socialValue);
    }
}
