//qsn 4a
import java.util.Arrays;

public class Q4aRoadNetworkImprovement {


    public int[][] modifyRoads(int n, int[][] roads, int source, int destination, int target) {
        int[][] modifiedRoads = new int[roads.length][3];
        for (int i = 0; i < roads.length; i++) {
            if (roads[i][2] == -1) {
                modifiedRoads[i][0] = roads[i][0];
                modifiedRoads[i][1] = roads[i][1];
                modifiedRoads[i][2] = (source == roads[i][0] && destination == roads[i][1]) ? target : 1;
            } else {
                modifiedRoads[i] = roads[i];
            }
        }
        return modifiedRoads;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {{4, 1, -1}, {2, 0, -1}, {0, 3, -1}, {4, 3, -1}};
        int source = 0;
        int destination = 1;
        int target = 5;

        Q4aRoadNetworkImprovement roadNetworkImprovement = new Q4aRoadNetworkImprovement();
        int[][] modifiedRoads = roadNetworkImprovement.modifyRoads(n, roads, source, destination, target);

        for (int[] road : modifiedRoads) {
            System.out.println(Arrays.toString(road));
        }
    }
}

