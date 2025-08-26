package JavaCollection.QueueInterface;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueInterfaceDemo {
    public static void main(String[] args) {

        System.out.println(" --- Queue --- ");
        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < 5; i++)
            q.add(i);

        System.out.println("Elements of queue: " + q);
        int removedele = q.remove();

        System.out.println("Removed element:"+ removedele);
        System.out.println(q);

        int head = q.peek();
        System.out.println("Head of queue:"+ head);

        int size = q.size();
        System.out.println("Size of queue:" + size);

        System.out.println(" --- Priority Queue --- ");
        Queue<Integer> pq = new PriorityQueue<Integer>();

        pq.add(10);
        pq.add(20);
        pq.add(15);

        System.out.println(pq.peek());

        // Printing the top element and removing it the PriorityQueue container
        System.out.println(pq.poll());

        System.out.println(pq.peek());
    }
}
