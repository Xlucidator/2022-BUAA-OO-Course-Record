import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 等待人员队列
 */
public abstract class PersonQueue {
    private final List<Person> queue = new ArrayList<>(); //list先后顺序 也可以用queue
    private final Map<Integer, Integer> up = new HashMap<>(); //上
    private final Map<Integer, Integer> down = new HashMap<>(); //下

    protected abstract boolean toUp(Person person);

    protected abstract int getKey(Person person);

    /**
     * 存取都是单线程操作不必上锁，但是更新数据可能会出现存取冲突
     */
    private synchronized void waitInc(Person person) {
        (toUp(person) ? up : down).merge(getKey(person), 1, Integer::sum);
    }

    private synchronized void waitDec(Person person) {
        (toUp(person) ? up : down).merge(getKey(person), -1, Integer::sum);
    }

    public synchronized void add(Person person) {
        queue.add(person);
        waitInc(person);
    }

    public synchronized Person getFirst() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.get(0);
    }

    public synchronized Person getAndRemoveFirst() {
        if (queue.isEmpty()) {
            return null;
        }
        Person person = queue.remove(0);
        waitDec(person);
        return person;
    }

    public List<Person> getUp(int key, int number) {
        return get(up, true, key, number);
    }

    public List<Person> getDown(int key, int number) {
        return get(down, false, key, number);
    }

    private synchronized List<Person> get(Map<Integer, Integer> map,
                                          boolean up, int key, int number) {
        if (queue.isEmpty()) {
            return Collections.emptyList();
        }
        Integer waitNum = map.get(key);
        if (waitNum == null || waitNum == 0) {
            return Collections.emptyList();
        }
        List<Person> persons = new ArrayList<>();
        queue.removeIf(person -> {
            if (persons.size() == number
                    || getKey(person) != key
                    || (toUp(person) != up)) {
                return false;
            }
            persons.add(person);
            waitDec(person);
            return true;
        });
        return persons;
    }

    public synchronized boolean hasWaitUp(int key) {
        Integer i = up.get(key);
        return i != null && i > 0;
    }

    public synchronized boolean hasWaitDown(int key) {
        Integer i = down.get(key);
        return i != null && i > 0;
    }
}
