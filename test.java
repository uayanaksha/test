#include <iostream>
#include <vector>
#include <queue>
#include <set>
using namespace std;

// Structure to represent a node in the search
struct Node {
    int vertex;
    int heuristic;  // Estimated cost to goal
    
    Node(int v, int h) : vertex(v), heuristic(h) {}
    
    // Operator for priority queue comparison
    bool operator>(const Node& other) const {
        return heuristic > other.heuristic;
    }
};

class Graph {
private:
    int V;  // Number of vertices
    vector<vector<int>> adjacencyMatrix;
    vector<int> heuristics;  // Heuristic values for each vertex

public:
    Graph(int vertices) : V(vertices) {
        // Initialize adjacency matrix with zeros
        adjacencyMatrix = vector<vector<int>>(V, vector<int>(V, 0));
        heuristics = vector<int>(V, 0);
    }
    
    // Add an edge to the graph
    void addEdge(int from, int to) {
        adjacencyMatrix[from][to] = 1;
        adjacencyMatrix[to][from] = 1;  // For undirected graph
    }
    
    // Set heuristic value for a vertex
    void setHeuristic(int vertex, int value) {
        heuristics[vertex] = value;
    }
    
    // Greedy Best-First Search implementation
    vector<int> bestFirstSearch(int start, int goal) {
        // Priority queue to store nodes
        priority_queue<Node, vector<Node>, greater<Node>> pq;
        
        // Set to keep track of visited vertices
        set<int> visited;
        
        // Vector to store parent of each vertex for path reconstruction
        vector<int> parent(V, -1);
        
        // Add start node to priority queue
        pq.push(Node(start, heuristics[start]));
        
        while (!pq.empty()) {
            // Get the node with lowest heuristic value
            int current = pq.top().vertex;
            pq.pop();
            
            // If we've reached the goal, reconstruct and return the path
            if (current == goal) {
                vector<int> path;
                while (current != -1) {
                    path.push_back(current);
                    current = parent[current];
                }
                reverse(path.begin(), path.end());
                return path;
            }
            
            // Skip if already visited
            if (visited.find(current) != visited.end()) {
                continue;
            }
            
            // Mark current node as visited
            visited.insert(current);
            
            // Check all adjacent vertices
            for (int i = 0; i < V; i++) {
                if (adjacencyMatrix[current][i] == 1 && visited.find(i) == visited.end()) {
                    // Add unvisited neighbors to priority queue
                    pq.push(Node(i, heuristics[i]));
                    // Only update parent if it hasn't been set yet
                    if (parent[i] == -1) {
                        parent[i] = current;
                    }
                }
            }
        }
        
        // Return empty path if goal is not reachable
        return vector<int>();
    }
};

// Example usage
int main() {
    // Create a graph with 6 vertices
    Graph g(6);
    
    // Add edges
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 4);
    g.addEdge(3, 5);
    g.addEdge(4, 5);
    
    // Set heuristic values (estimated distance to goal)
    g.setHeuristic(0, 8);
    g.setHeuristic(1, 6);
    g.setHeuristic(2, 7);
    g.setHeuristic(3, 3);
    g.setHeuristic(4, 2);
    g.setHeuristic(5, 0);  // Goal state
    
    // Find path from vertex 0 to 5
    vector<int> path = g.bestFirstSearch(0, 5);
    
    // Print the path
    cout << "Path found: ";
    for (int vertex : path) {
        cout << vertex << " ";
    }
    cout << endl;
    
    return 0;
}