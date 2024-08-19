//qsn 3a
import java.util.ArrayList;
import java.util.List;

public class FriendRequests {

    // Define the Union-Find data structure
    static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return false;
            }
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }

        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }

    // Function to process road requests and determine if they can be accepted
    public static List<String> processRoadRequests(int n, int[][] roadRestrictions, int[][] roadRequests) {
        UnionFind uf = new UnionFind(n);
        List<String> results = new ArrayList<>();

        for (int[] request : roadRequests) {
            int city1 = request[0];
            int city2 = request[1];
            boolean canBeConnected = true;

            for (int[] restriction : roadRestrictions) {
                int restrictedCity1 = restriction[0];
                int restrictedCity2 = restriction[1];
                if ((uf.isConnected(city1, restrictedCity1) && uf.isConnected(city2, restrictedCity2)) ||
                        (uf.isConnected(city1, restrictedCity2) && uf.isConnected(city2, restrictedCity1))) {
                    canBeConnected = false;
                    break;
                }
            }

            if (canBeConnected) {
                uf.union(city1, city2);
                results.add("approved");
            } else {
                results.add("denied");
            }
        }

        return results;
    }

    // Main method to test the solution
    public static void main(String[] args) {
        int n = 4; // Number of cities
        int[][] roadRestrictions = {{0, 1}, {2, 3}}; // Road restrictions list
        int[][] roadRequests = {{0, 2}, {1, 3}, {0, 1}, {2, 3}}; // Road requests list
        List<String> result = processRoadRequests(n, roadRestrictions, roadRequests);

        // Print the results of each road request
        for (String res : result) {
            System.out.println(res);
        }
    }
}
