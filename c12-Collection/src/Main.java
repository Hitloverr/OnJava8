import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * List[ArrayList LinkedList]
 * Set[HashSet、TreeSet、LinkedHashSet]
 * Queue[ArrayQueue、LinkedQueue、PriorityQueue]
 * Map【HashMap、TreeMap、LinkedHashMap】
 */

public class Main {

    // List
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Collection c = new ArrayList(Arrays.asList(new Integer[]{1, 2, 3}));
        Integer[] i2s = new Integer[]{5, 52};
        c.addAll(Arrays.asList(i2s));

        Collections.addAll(c, 1, 2, 3);

        List<Object> l1 = new ArrayList<>();
        l1.contains(1);
        l1.indexOf(1);
        l1.remove(1);
        l1.add(1);
        l1.add(1, 2);
        l1.set(1, 1);
        l1.subList(1, 2);// 得到的子列表的修改会反映在原列表上。

        l1.retainAll(c);
        l1.removeAll(c);
        l1.addAll(1, c);
        l1.isEmpty();
        l1.clear();
        Object[] objects = l1.toArray();
        Object[] objects1 = l1.toArray(new Object[0]);


    }

    // Iterator：将遍历序列的操作和与该序列的底层结构分离。
    public void IteratorDemo() {
        List<Object> list = new ArrayList<>();
        Iterator<Object> iterator = list.iterator();
        while(iterator.hasNext()) {
            iterator.next();
        }
        for (Object o: list) {
            System.out.println(o);
        }

        Iterator<Object> iterator1 = list.iterator();
        while(iterator1.hasNext()) {
            iterator1.next();
            iterator1.remove(); // 删除由next生成的最后一个元素，也就是说必须先next才能remove
        }

        ListIterator<Object> listIterator = list.listIterator();
        while(listIterator.hasNext()) {
            System.out.println(listIterator.next());
            System.out.println(listIterator.nextIndex());
            System.out.println(listIterator.previous());
            System.out.println(listIterator.previousIndex());
        }
        while (listIterator.hasPrevious()) {
            listIterator.previous();
            listIterator.set(1);
            listIterator.remove();
        }
    }

    // LinkedList
    public void LinkedListDemo() {
        LinkedList<Object> l = new LinkedList<>();
        l.addFirst(1);
        l.peek();   // 与下面两个类似，区别是为空时这里会null，下面会报异常
        l.getFirst();
        l.element();

        l.poll(); // 删除头部元素，不存在时返回mull，其他异常
        l.remove();
        l.removeFirst();

        l.add(1);
        l.offer(1);
        l.addLast(1);

        l.removeLast();

    }


    // Stack
    public void StackDemo() {
       // java中的Stack不好用，用Deque吧！
    }

    // Set
    public void setDemo() throws IOException {
        // HashSet采用散列方式排序，你不能确定他的顺序
        // TreeSet比较器
        // LinkedHashSet按照插入顺序。
        List<String> lines = Files.readAllLines(Paths.get("Set.java"));
        // 默认字典序，这里参数表示字母序。
        Set<String> words = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String line : lines) {
            // 一个或者多个非单词拆分。
            for (String word : line.split("\\W+")) {
                if (word.trim().length() > 0) {
                    words.add(word);
                }
            }
        }

    }


    // Map
    public void mapDemo() {
        Map<String,String> m = new HashMap<>();
        m.containsKey("a");
        m.containsValue("a");
        Map<String,List<? extends Pet>> map = new HashMap<>();
        m.keySet();
        m.entrySet();
    }

    // queue,在并发编程中尤其重要，可以安全将对象从一个任务传输到另外一个任务重。
    public void queueDemo() {
        Queue<Object> queue = new LinkedList<>();
        queue.offer(1);
        queue.peek(); // no exception
        queue.element();
        queue.poll(); // no exception
        queue.remove();

        PriorityQueue<Pet> pq = new PriorityQueue<>(Comparator.comparingInt(Object::hashCode));
//        pq.addAll(queue);
    }

    // 集合与迭代器
    public void iterDemo() {
        //Collection AbstractCollection.Collection与迭代器绑定在了一起，实现Collection就要提供iterator方法
        // 生成iterator是将序列与消费该序列方法连接在一起耦合度最小的方式。施加的约束也最小。

    }

    // for-in 与迭代器
    public void forInDemo() {
        // for in == Iterable接口，必须要实现iterator方法 == Iterator
        // 但是数组不是Iterable接口，也能用for in。
        class ReversedList<T> {
            public Iterable<T> reversed() {
                return new Iterable<T>(){
                    @Override
                    public Iterator<T> iterator() {
                        return new Iterator<T>() {
                            @Override
                            public boolean hasNext() {
                                return false;
                            }

                            @Override
                            public T next() {
                                return null;
                            }
                        };
                    }










                }

               ;
            }
        }

        ReversedList<Object> list = new ReversedList<>();
        // 提供了多种迭代集合的方式。
        for (Object o : list.reversed()) {

        }
    }
        /**
         * 要
         * 注意 Arrays.asList() 生成一个 List 对象，该对象使用底层数组作为其物理实现。如
         * 果执行的操作会修改这个 List ，并且不希望修改原始数组，那么就应该在另一个集合
         * 中创建一个副本。你可以再new ArrayList 包装一层*/

}

class Pet{

}

// 如果只需要栈的行为，不要用继承，因为你会有Deque的所有方法。
// 用组合，只暴露必须的接口。
class Stack<T> {
    private Deque<T> storage = new ArrayDeque<>();

    public void push(T t) {
        storage.push(t);
    }

    public T peek() {
        return storage.peek();
    }

    public T pop() {
        return storage.pop();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }
}