package JavaCollection.QueueInterface;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class DequeInterfaceDemo {
    public static void main(String[] args) {

        System.out.println(" --- LinkedList as Deque --- ");
        Deque<String> deque = new LinkedList<String>();


        deque.add("Element 1 (Tail)");

        deque.addFirst("Element 2 (Head)");

        deque.addLast("Element 3 (Tail)");

        deque.push("Element 4 (Head)");

        deque.offer("Element 5 (Tail)");

        // Add at the first
        deque.offerFirst("Element 6 (Head)");

        System.out.println(deque + "\n");


        deque.removeFirst();
        deque.removeLast();
        System.out.println("Deque after removing " + "first and last: " + deque);

        System.out.println("--- ArrayDeque ---");
        Deque<String> dq = new ArrayDeque<String>();

        // add() method to insert
        dq.add("Quang");
        dq.addFirst("Nguyen");
        dq.addLast("Luong");

        System.out.println(dq);

        for (Iterator itr = dq.iterator(); itr.hasNext();) {
            System.out.print(itr.next() + " ");
        }

        System.out.println("\nRemove deque");

        System.out.println(dq.pop());
        System.out.println(dq.poll());
        System.out.println(dq.pollFirst());
        System.out.println(dq.pollLast());//null

        System.out.println(dq); // []

        Deque<Integer> de_que = new ArrayDeque<Integer>(10);


        de_que.add(10);
        de_que.add(20);
        de_que.add(30);
        de_que.add(40);
        de_que.add(50);

        System.out.println(de_que);

        de_que.clear();

        // addFirst() method to insert the elements at the head
        de_que.addFirst(564);
        de_que.addFirst(291);

        // addLast() method to insert the elements at the tail
        de_que.addLast(24);
        de_que.addLast(14);

        System.out.println(de_que);
    }
}
