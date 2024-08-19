//qsn 2b

import java.util.HashMap;
import java.util.Map;

public class MovieTheaterSeating {

    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        // Initialize a HashMap to store seat numbers and their last seen index
        Map<Integer, Integer> seatMap = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int currentSeat = nums[i];

            // Check for potential seats within the allowed indexDiff
            for (Map.Entry<Integer, Integer> entry : seatMap.entrySet()) {
                int prevSeat = entry.getKey();
                int prevIndex = entry.getValue();

                // Ensure the index difference is within the allowed range
                if (i - prevIndex <= indexDiff && Math.abs(currentSeat - prevSeat) <= valueDiff) {
                    return true;
                }
            }

            // Update the HashMap with the current seat
            seatMap.put(currentSeat, i);

            // Maintain the size of the HashMap within indexDiff to avoid unnecessary checks
            if (i >= indexDiff) {
                seatMap.remove(nums[i - indexDiff]);
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums1 = {2, 3, 5, 4, 9};
        int indexDiff1 = 2;
        int valueDiff1 = 1;
        System.out.println(canSitTogether(nums1, indexDiff1, valueDiff1)); // Output: true

        int[] nums2 = {1, 5, 9, 13};
        int indexDiff2 = 2;
        int valueDiff2 = 3;
        System.out.println(canSitTogether(nums2, indexDiff2, valueDiff2)); // Output: false

        int[] nums3 = {4, 6, 8, 10};
        int indexDiff3 = 1;
        int valueDiff3 = 2;
        System.out.println(canSitTogether(nums3, indexDiff3, valueDiff3)); // Output: true
    }
}
