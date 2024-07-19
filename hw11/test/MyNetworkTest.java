import com.oocourse.spec3.exceptions.*;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;

import static org.junit.Assert.*;

import java.util.Random;

public class MyNetworkTest {

    @org.junit.Test
    public void deleteColdEmoji() throws RelationNotFoundException, EqualEmojiIdException,
            TagIdNotFoundException, PersonIdNotFoundException, EmojiIdNotFoundException,
            EqualRelationException, EqualTagIdException, EqualPersonIdException,
            MessageIdNotFoundException, EqualMessageIdException {
        for (int m = 1; m <= 20; m++) {
            MyNetwork[] nets = createNet();
            MyNetwork oldNet = nets[0];
            MyNetwork net = nets[1];
            int limit = m % 5;
            if (limit == 3) {
                limit = 100;
            }
            int[] oldId = oldNet.getEmojiIdList();
            int[] oldHeat = oldNet.getEmojiHeatList();
            Message[] oldMessages = oldNet.getMessages();

            int result = net.deleteColdEmoji(limit);
            int[] id = net.getEmojiIdList();
            int[] heat = net.getEmojiHeatList();
            Message[] messages = net.getMessages();
            assertEquals(result, id.length);
            for (int i = 0; i < oldId.length; i++) {
                if (oldHeat[i] >= limit) {
                    boolean f = false;
                    for (int j = 0; j < id.length; j++) {
                        if (id[j] == oldId[i]) {
                            f = true;
                            break;
                        }
                    }
                    assertTrue(f);
                }
            }
            for (int i = 0; i < id.length; i++) {
                boolean f = false;
                for (int j = 0; j < oldId.length; j++) {
                    if (id[i] == oldId[j] && heat[i] == oldHeat[j]) {
                        f = true;
                        break;
                    }
                }
                assertTrue(f);
            }
            int num = 0;
            for (int i = 0; i < oldId.length; i++) {
                if (oldHeat[i] >= limit) {
                    num++;
                }
            }
            assertEquals(num, id.length);
            assertEquals(id.length, heat.length);
            for (int i = 0; i < oldMessages.length; i++) {
                if (oldMessages[i] instanceof EmojiMessage &&
                        net.containsEmojiId(((MyEmojiMessage) oldMessages[i]).getEmojiId())) {
                    boolean f = false;
                    for (int j = 0; j < messages.length; j++) {
                        if (oldMessages[i].equals(messages[j])) {
                            messageEquals(oldMessages[i], messages[j]);
                            f = true;
                            break;
                        }
                    }
                    assertTrue(f);
                }
            }
            for (int i = 0; i < oldMessages.length; i++) {
                if (!(oldMessages[i] instanceof EmojiMessage)) {
                    boolean f = false;
                    for (int j = 0; j < messages.length; j++) {
                        if (oldMessages[i].equals(messages[j])) {
                            messageEquals(oldMessages[i], messages[j]);
                            f = true;
                            break;
                        }
                    }
                    assertTrue(f);
                }
            }
            int sum = 0;
            for (int i = 0; i < oldMessages.length; i++) {
                if (oldMessages[i] instanceof EmojiMessage) {
                    if (net.containsEmojiId(((MyEmojiMessage) oldMessages[i]).getEmojiId())) {
                        sum++;
                    }
                } else {
                    sum++;
                }
            }
            assertEquals(sum, messages.length);
        }
    }

    public MyNetwork[] createNet() throws EqualPersonIdException, PersonIdNotFoundException,
            EqualRelationException, EqualEmojiIdException, EqualTagIdException,
            RelationNotFoundException, TagIdNotFoundException, EmojiIdNotFoundException,
            EqualMessageIdException, MessageIdNotFoundException {
        MyNetwork net1 = new MyNetwork();
        MyNetwork net2 = new MyNetwork();
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            net1.addPerson(new MyPerson(i, "person" + i, 20 + i));
            net2.addPerson(new MyPerson(i, "person" + i, 20 + i));
            net1.addTag(i, new MyTag(i));
            net2.addTag(i, new MyTag(i));
        }
        for (int i = 1; i <= 10; i++) {
            for (int j = i + 1; j <= 10; j++) {
                net1.addRelation(i, j, i * j);
                net2.addRelation(i, j, i * j);
                net1.addPersonToTag(j, i, i);
                net2.addPersonToTag(j, i, i);
            }
        }
        for (int i = 1; i <= 20; i++) {
            net1.storeEmojiId(i);
            net2.storeEmojiId(i);
        }
        for (int i = 1; i <= 100; i++) {
            int id1 = random.nextInt(10) + 1;
            int id2 = random.nextInt(10) + 1;
            int id = random.nextInt(20) + 1;
            if (random.nextInt(2) == 0) {
                if (id1 == id2) {
                    id2++;
                    if (id2 > 10) {
                        id2 -= 10;
                    }
                }
                MyEmojiMessage msg1 = new MyEmojiMessage(100 + i, id, net1.getPerson(id1), net1.getPerson(id2));
                MyEmojiMessage msg2 = new MyEmojiMessage(100 + i, id, net2.getPerson(id1), net2.getPerson(id2));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            } else {
                MyEmojiMessage msg1 = new MyEmojiMessage(100 + i, id, net1.getPerson(id1), net1.getPerson(id1).getTag(id1));
                MyEmojiMessage msg2 = new MyEmojiMessage(100 + i, id, net2.getPerson(id1), net2.getPerson(id1).getTag(id1));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            }
            net1.sendMessage(100 + i);
            net2.sendMessage(100 + i);
        }
        for (int i = 1; i <= 2; i++) {
            int id1 = random.nextInt(10) + 1;
            int id2 = random.nextInt(10) + 1;
            if (random.nextInt(2) == 0) {
                if (id1 == id2) {
                    id2++;
                    if (id2 > 10) {
                        id2 -= 10;
                    }
                }
                MyRedEnvelopeMessage msg1 = new MyRedEnvelopeMessage(300 + i, 10, net1.getPerson(id1), net1.getPerson(id2));
                MyRedEnvelopeMessage msg2 = new MyRedEnvelopeMessage(300 + i, 10, net2.getPerson(id1), net2.getPerson(id2));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            } else {
                MyRedEnvelopeMessage msg1 = new MyRedEnvelopeMessage(300 + i, 10, net1.getPerson(id1), net1.getPerson(id1).getTag(id1));
                MyRedEnvelopeMessage msg2 = new MyRedEnvelopeMessage(300 + i, 10, net2.getPerson(id1), net1.getPerson(id1).getTag(id1));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            }
        }
        for (int i = 1; i <= 2; i++) {
            int id1 = random.nextInt(10) + 1;
            int id2 = random.nextInt(10) + 1;
            if (random.nextInt(2) == 0) {
                if (id1 == id2) {
                    id2++;
                    if (id2 > 10) {
                        id2 -= 10;
                    }
                }
                MyNoticeMessage msg1 = new MyNoticeMessage(400 + i, "10", net1.getPerson(id1), net1.getPerson(id2));
                MyNoticeMessage msg2 = new MyNoticeMessage(400 + i, "10", net2.getPerson(id1), net2.getPerson(id2));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            } else {
                MyNoticeMessage msg1 = new MyNoticeMessage(400 + i, "10", net1.getPerson(id1), net1.getPerson(id1).getTag(id1));
                MyNoticeMessage msg2 = new MyNoticeMessage(400 + i, "10", net2.getPerson(id1), net1.getPerson(id1).getTag(id1));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            }
        }
        for (int i = 1; i <= 50; i++) {
            int id1 = random.nextInt(10) + 1;
            int id2 = random.nextInt(10) + 1;
            int id = random.nextInt(20) + 1;
            if (random.nextInt(2) == 0) {
                if (id1 == id2) {
                    id2++;
                    if (id2 > 10) {
                        id2 -= 10;
                    }
                }
                MyEmojiMessage msg1 = new MyEmojiMessage(500 + i, id, net1.getPerson(id1), net1.getPerson(id2));
                MyEmojiMessage msg2 = new MyEmojiMessage(500 + i, id, net2.getPerson(id1), net2.getPerson(id2));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            } else {
                MyEmojiMessage msg1 = new MyEmojiMessage(500 + i, id, net1.getPerson(id1), net1.getPerson(id1).getTag(id1));
                MyEmojiMessage msg2 = new MyEmojiMessage(500 + i, id, net2.getPerson(id1), net2.getPerson(id1).getTag(id1));
                net1.addMessage(msg1);
                net2.addMessage(msg2);
            }
        }
        return new MyNetwork[]{net1, net2};
    }

    public void messageEquals(Message msg1, Message msg2) {
        assertEquals(msg1.getTag(), msg2.getTag());
        assertEquals(msg1.getPerson1(), msg2.getPerson1());
        assertEquals(msg1.getPerson2(), msg2.getPerson2());
        assertEquals(msg1.getType(), msg2.getType());
        assertEquals(msg1.getSocialValue(), msg2.getSocialValue());
        if (msg1 instanceof MyEmojiMessage) {
            assertTrue(msg2 instanceof MyEmojiMessage);
            assertEquals(((MyEmojiMessage) msg1).getEmojiId(), ((MyEmojiMessage) msg2).getEmojiId());
        }
        if (msg1 instanceof MyNoticeMessage) {
            assertTrue(msg2 instanceof MyNoticeMessage);
            assertEquals(((MyNoticeMessage) msg1).getString(), ((MyNoticeMessage) msg2).getString());
        }
        if (msg1 instanceof MyRedEnvelopeMessage) {
            assertTrue(msg2 instanceof MyRedEnvelopeMessage);
            assertEquals(((MyRedEnvelopeMessage) msg1).getMoney(), ((MyRedEnvelopeMessage) msg2).getMoney());
        }
    }
}