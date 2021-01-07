//8. Write a program to find the shortest path between vertices using bellman-ford algorithm.


import java.util.Scanner;

class BellmanFord {

    static int n, dest;
    static double[] prevDistanceVector, distanceVector;
    static double[][] adjacencyMatrix;

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of nodes");
        n = scanner.nextInt();
        adjacencyMatrix = new double[n][n];

        System.out.println("Enter Adjacency Matrix (Use '999' for No Link)");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                adjacencyMatrix[i][j] = scanner.nextDouble();

        System.out.println("Enter destination vertex");
        dest = scanner.nextInt();
       

        distanceVector = new double[n];
        for (int i = 0; i < n; i++)
            distanceVector[i] = 999;
        distanceVector[dest - 1] = 0;

        bellmanFordAlgorithm();

        System.out.println("Destination\tDistance\t");
        for(int vertex=0 ; vertex < n; vertex++)
        	{
        		if (vertex == dest - 1) {
        			continue;
        		}
        		System.out.println("  "+(vertex+1)+"\t\t"+distanceVector[vertex]);
        	}
        System.out.println();
        scanner.close();
    }

    static void bellmanFordAlgorithm() {
        for (int i = 0; i < n - 1; i++) {
            prevDistanceVector = distanceVector.clone();
            System.out.print(prevDistanceVector[i]);
            for (int j = 0; j < n; j++) {
                double min = 999;
                for (int k = 0; k < n; k++) {
                    if (min > adjacencyMatrix[j][k] + prevDistanceVector[k]) {
                        min = adjacencyMatrix[j][k] + prevDistanceVector[k];
                    }
                }
                distanceVector[j] = min;
            }
        }
    }
 
}
