package finley.gmair.datastructrue;


import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class LimitQueue<E> implements Serializable {

    private int limit; // 队列长度

    private LinkedList<E> queue = new LinkedList<E>();

    public LimitQueue(int limit) {
        this.limit = limit;
    }


    //入队：当队列大小已满时，把队头(最先加入的)的元素poll掉
    public void offer(E e) {
        if (queue.size() >= limit) {
            queue.poll();
        }
        queue.offer(e);
    }

    public void replaceLast(E e) {
        if (queue.size() > 0) {
            queue.removeLast();
            queue.offer(e);
        }
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public E getFirst() {
        return queue.getFirst();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return queue.size();
    }

}