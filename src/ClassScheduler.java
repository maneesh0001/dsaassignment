//Question 1a

import java.util.*;

public class ClassScheduler {

    public static int scheduleClasses(int n, int[][] classes) {
        // Step 1: Sort classes by start time, and by end time if start times are the same
        Arrays.sort(classes, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        // Step 2: Priority queue to track end times and room indices
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Array to count the number of classes held by each room
        int[] classCount = new int[n];

        // Step 3: Iterate through each class
        for (int[] cls : classes) {
            int start = cls[0];
            int end = cls[1];

            // Step 4: Free up rooms that have finished their classes before the current class start time
            while (!pq.isEmpty() && pq.peek()[0] <= start) {
                pq.poll();
            }

            // Step 5: Assign room to current class or delay it if necessary
            if (pq.size() < n) {
                // Assign to a new room
                pq.offer(new int[]{end, pq.size()});
                classCount[pq.size() - 1]++;
            } else {
                // Delay the class until the earliest room becomes available
                int[] earliest = pq.poll();
                pq.offer(new int[]{earliest[0] + (end - start), earliest[1]});
                classCount[earliest[1]]++;
            }
        }

        // Step 6: Determine the room with the maximum number of classes held
        int maxClasses = 0;
        int roomWithMaxClasses = 0;
        for (int i = 0; i < n; i++) {
            if (classCount[i] > maxClasses) {
                maxClasses = classCount[i];
                roomWithMaxClasses = i;
            }
        }

        return roomWithMaxClasses;
    }

    public static void main(String[] args) {
        // Example 1
        int n1 = 2;
        int[][] classes1 = {{0, 10}, {1, 5}, {2, 7}, {3, 4}};
        System.out.println(scheduleClasses(n1, classes1));  // Output: 0

        // Example 2
        int n2 = 3;
        int[][] classes2 = {{1, 20}, {2, 10}, {3, 5}, {4, 9}, {6, 8}};
        System.out.println(scheduleClasses(n2, classes2));  // Output: 1
    }
}
