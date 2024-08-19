import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class RouteOptimizationGUI extends JFrame {

    private Map<String, Point> locationPositions;
    private Map<String, Integer> locationIndexMap;
    private int[][] distances;

    private String startLocation;
    private String endLocation;

    private List<Integer> shortestPath;

    public RouteOptimizationGUI() {
        setTitle("Route Optimization for Delivery Service");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeLocationsAndDistances();

        setLayout(new BorderLayout(15, 15));

        GraphPanel graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);

        JPanel pathPanel = createPathPanel();
        add(pathPanel, BorderLayout.EAST);

        JPanel inputPanel = createInputPanel(graphPanel, pathPanel);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeLocationsAndDistances() {
        locationPositions = new HashMap<>();
        locationPositions.put("CityA", new Point(300, 150));
        locationPositions.put("CityB", new Point(100, 400));
        locationPositions.put("CityC", new Point(500, 200));
        locationPositions.put("CityD", new Point(350, 500));
        locationPositions.put("CityE", new Point(600, 400));
        locationPositions.put("CityF", new Point(700, 300));

        String[] locations = {"CityA", "CityB", "CityC", "CityD", "CityE", "CityF"};
        locationIndexMap = new HashMap<>();
        for (int i = 0; i < locations.length; i++) {
            locationIndexMap.put(locations[i], i);
        }

        distances = new int[locations.length][locations.length];
        for (int i = 0; i < locations.length; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
            distances[i][i] = 0;
        }

        addConnection("CityA", "CityB", 200);
        addConnection("CityB", "CityD", 50);
        addConnection("CityD", "CityF", 150);
        addConnection("CityF", "CityE", 50);
        addConnection("CityC", "CityF", 55);
        addConnection("CityC", "CityB", 200);
    }

    private void addConnection(String loc1, String loc2, int distance) {
        int index1 = locationIndexMap.get(loc1);
        int index2 = locationIndexMap.get(loc2);
        distances[index1][index2] = distance;
        distances[index2][index1] = distance;
    }

    private JPanel createPathPanel() {
        JPanel pathPanel = new JPanel(new BorderLayout());
        pathPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        pathPanel.setBackground(new Color(240, 240, 240));

        JLabel pathLabel = new JLabel("Shortest Path will be displayed here");
        pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pathLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        pathLabel.setForeground(Color.BLUE);
        pathPanel.add(pathLabel, BorderLayout.CENTER);
        return pathPanel;
    }

    private JPanel createInputPanel(GraphPanel graphPanel, JPanel pathPanel) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JLabel startLabel = new JLabel("Start Location:");
        startLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        inputPanel.add(startLabel);

        JComboBox<String> startComboBox = new JComboBox<>(locationIndexMap.keySet().toArray(new String[0]));
        startComboBox.addActionListener(e -> {
            startLocation = (String) startComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        startComboBox.setToolTipText("Select the starting location");
        inputPanel.add(startComboBox);

        JLabel endLabel = new JLabel("End Location:");
        endLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
        inputPanel.add(endLabel);

        JComboBox<String> endComboBox = new JComboBox<>(locationIndexMap.keySet().toArray(new String[0]));
        endComboBox.addActionListener(e -> {
            endLocation = (String) endComboBox.getSelectedItem();
            graphPanel.repaint();
        });
        endComboBox.setToolTipText("Select the ending location");
        inputPanel.add(endComboBox);

        JButton optimizeButton = new JButton("Optimize Route");
        optimizeButton.addActionListener(e -> {
            if (startLocation != null && endLocation != null && !startLocation.equals(endLocation)) {
                findShortestPath(startLocation, endLocation);
                ((JLabel) pathPanel.getComponent(0)).setText("Shortest Path: " + shortestPathToString());
                graphPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select different start and end locations.");
            }
        });
        optimizeButton.setFont(new Font("Verdana", Font.BOLD, 16));
        optimizeButton.setBackground(new Color(100, 150, 255));
        optimizeButton.setForeground(Color.WHITE);
        optimizeButton.setFocusPainted(false);
        inputPanel.add(optimizeButton);

        return inputPanel;
    }

    private void findShortestPath(String startLocation, String endLocation) {
        int startIndex = locationIndexMap.get(startLocation);
        int endIndex = locationIndexMap.get(endLocation);
        shortestPath = dijkstra(startIndex, endIndex);
    }

    private List<Integer> dijkstra(int start, int end) {
        int numLocations = distances.length;
        int[] minDistances = new int[numLocations];
        boolean[] visited = new boolean[numLocations];
        int[] previous = new int[numLocations];

        Arrays.fill(minDistances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        minDistances[start] = 0;

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(i -> minDistances[i]));
        queue.add(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            visited[u] = true;

            for (int v = 0; v < numLocations; v++) {
                if (!visited[v] && distances[u][v] != Integer.MAX_VALUE) {
                    int alt = minDistances[u] + distances[u][v];
                    if (alt < minDistances[v]) {
                        minDistances[v] = alt;
                        previous[v] = u;
                        queue.add(v);
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int current = end;
        while (current != -1) {
            path.add(current);
            current = previous[current];
        }
        Collections.reverse(path);
        return path;
    }

    private String shortestPathToString() {
        if (shortestPath == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            sb.append(getLocationName(shortestPath.get(i)));
            if (i < shortestPath.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    private String getLocationName(int index) {
        for (Map.Entry<String, Integer> entry : locationIndexMap.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }
        return null;
    }

    private class GraphPanel extends JPanel {

        private static final int NODE_RADIUS = 25;
        private static final Color NODE_COLOR = new Color(0, 102, 204);
        private static final Color EDGE_COLOR = new Color(0, 0, 0);
        private static final Color PATH_COLOR = new Color(255, 69, 0);
        private static final Font EDGE_FONT = new Font("Arial", Font.PLAIN, 14);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(EDGE_COLOR);
            g2d.setFont(EDGE_FONT);
            for (int i = 0; i < distances.length; i++) {
                for (int j = i + 1; j < distances.length; j++) {
                    if (distances[i][j] != Integer.MAX_VALUE) {
                        Point loc1Pos = locationPositions.get(getLocationName(i));
                        Point loc2Pos = locationPositions.get(getLocationName(j));
                        g2d.drawLine(loc1Pos.x, loc1Pos.y, loc2Pos.x, loc2Pos.y);

                        int centerX = (loc1Pos.x + loc2Pos.x) / 2;
                        int centerY = (loc1Pos.y + loc2Pos.y) / 2;

                        String distanceLabel = String.valueOf(distances[i][j]);
                        g2d.drawString(distanceLabel, centerX, centerY);
                    }
                }
            }

            g2d.setColor(NODE_COLOR);
            for (String location : locationPositions.keySet()) {
                Point locPos = locationPositions.get(location);
                g2d.fillOval(locPos.x - NODE_RADIUS, locPos.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                g2d.setColor(Color.WHITE);
                g2d.drawString(location, locPos.x - NODE_RADIUS + 5, locPos.y + 5);
                g2d.setColor(NODE_COLOR);
            }

            if (startLocation != null && endLocation != null) {
                g2d.setColor(Color.RED);
                Point startLocPos = locationPositions.get(startLocation);
                Point endLocPos = locationPositions.get(endLocation);
                g2d.drawOval(startLocPos.x - NODE_RADIUS - 5, startLocPos.y - NODE_RADIUS - 5,
                        2 * NODE_RADIUS + 10, 2 * NODE_RADIUS + 10);
                g2d.drawOval(endLocPos.x - NODE_RADIUS - 5, endLocPos.y - NODE_RADIUS - 5,
                        2 * NODE_RADIUS + 10, 2 * NODE_RADIUS + 10);
            }

            if (shortestPath != null) {
                g2d.setColor(PATH_COLOR);
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    Point loc1Pos = locationPositions.get(getLocationName(shortestPath.get(i)));
                    Point loc2Pos = locationPositions.get(getLocationName(shortestPath.get(i + 1)));
                    g2d.drawLine(loc1Pos.x, loc1Pos.y, loc2Pos.x, loc2Pos.y);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 700);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RouteOptimizationGUI::new);
    }
}
