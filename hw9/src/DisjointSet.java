import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

public class DisjointSet {
    private HashMap<Integer, Integer> parent;
    private HashMap<Integer, Integer> rank;
    private HashMap<Integer, HashSet<Integer>> edge;
    private int blockCnt;
    private int tripleCnt;

    public DisjointSet() {
        this.parent = new HashMap<>();
        this.rank = new HashMap<>();
        this.edge = new HashMap<>();
        this.blockCnt = 0;
        this.tripleCnt = 0;
    }

    public int getBlockCnt() {
        return this.blockCnt;
    }

    public int getTripleCnt() {
        return this.tripleCnt;
    }

    public boolean isCircle(int id1, int id2) {
        return find(id1) == find(id2);
    }

    public void addPerson(int id) {
        this.parent.put(id, id);
        this.rank.put(id, 0);
        this.blockCnt++;
    }

    public void addRelation(int id1, int id2) {
        HashSet<Integer> edge1 = this.edge.get(id1);
        HashSet<Integer> edge2 = this.edge.get(id2);
        if (edge1 != null && edge2 != null) {
            for (int id3 : edge1) {
                if (edge2.contains(id3)) {
                    this.tripleCnt++;
                }
            }
        } else {
            if (edge1 == null) {
                edge1 = new HashSet<>();
                this.edge.put(id1, edge1);
            }
            if (edge2 == null) {
                edge2 = new HashSet<>();
                this.edge.put(id2, edge2);
            }
        }
        edge2.add(id1);
        edge1.add(id2);
        merge(id1, id2);
    }

    public void deleteRelation(int id1, int id2) {
        HashSet<Integer> edge1 = this.edge.get(id1);
        HashSet<Integer> edge2 = this.edge.get(id2);
        edge2.remove(id1);
        edge1.remove(id2);
        for (int id3 : edge1) {
            if (edge2.contains(id3)) {
                this.tripleCnt--;
            }
        }
        deleteEdge(id1, id2);
    }

    public int find(int id) {
        int ans = id;
        while (this.parent.get(ans) != ans) {
            ans = this.parent.get(ans);
        }
        int now = id;
        while (now != ans) {
            int father = this.parent.get(now);
            this.parent.put(now, father);
            now = father;
        }
        return ans;
    }

    public void merge(int id1, int id2) {
        int fa1 = find(id1);
        int fa2 = find(id2);
        if (fa1 != fa2) {
            int rk1 = this.rank.get(fa1);
            int rk2 = this.rank.get(fa2);
            if (rk1 < rk2) {
                this.parent.put(fa1, fa2);
            } else {
                this.parent.put(fa2, fa1);
                if (rk1 == rk2) {
                    this.rank.put(fa1, rk1 + 1);
                }
            }
            this.blockCnt--;
        }
    }

    public void deleteEdge(int id1, int id2) {
        if (find(id1) == find(id2)) {
            this.parent.put(id1, id1);
            this.rank.put(id1, 1);
            this.parent.put(id2, id2);
            HashSet<Integer> visited = new HashSet<>();
            bfs(id1, id1, visited);
            if (this.parent.get(id2) == id2) {
                this.blockCnt++;
                HashSet<Integer> visit = new HashSet<>();
                bfs(id2, id2, visit);
            }
        }
    }

    public void bfs(int id, int parent, HashSet<Integer> visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(id);
        while (!queue.isEmpty()) {
            int now = queue.poll();
            visited.add(now);
            for (int link : this.edge.get(now)) {
                if (!visited.contains(link)) {
                    visited.add(link);
                    queue.offer(link);
                    this.parent.put(link, parent);
                }
            }
        }
    }
}
