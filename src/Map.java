//package assignments.Ex3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;
	
	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {init(w,h, v);}
	/**
	 * Constructs a square map (size*size).
	 * @param size
	 */
	public Map(int size) {this(size,size, 0);}
	
	/**
	 * Constructs a map from a given 2D array.
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}
	@Override
	public void init(int w, int h, int v) {
		/////// add your code below ///////
        if (w<0 || h<0){throw new RuntimeException();}// check matrix is valid
        _map = new int[h][w];
        for (int y = 0;y<h;y++){
            for (int x = 0; x<w ; x++){
                _map[y][x] = v;
            }
        }
		///////////////////////////////////
	}
	@Override
	public void init(int[][] arr) {
		/////// add your code below ///////
        if (arr == null || arr.length == 0 || arr[0].length == 0) { // check matrix length valid
            throw new RuntimeException();}
        int h = arr.length;
        int w = arr[0].length;
        _map = new int[h][w];

        for (int y= 0; y< h; y++){
            if (arr[y].length != w){throw new RuntimeException();} // check if all the lines are in the same length
            for (int x=0; x<w; x++){
                _map[y][x] = arr[y][x];
            }
        }
		///////////////////////////////////
	}
	@Override
	public int[][] getMap() {
		/////// add your code below ///////
        int h = _map.length;
        int w = _map[0].length;
        int[][] ans = new int[h][w];
        for (int i = 0; i<h; i++){ // for every column in matrix
            for (int k = 0 ; k< w; k++){ // for every row in matrix
                ans[i][k] = _map[i][k]; // copy every value in old matrix to new matrix
            }
        }
		///////////////////////////////////
		return ans;
	}
	@Override
	/////// add your code below ///////
	public int getWidth() {
        int ans;
        ans = _map[0].length;
        return ans;}
	@Override
	/////// add your code below ///////
	public int getHeight() {
        int ans;
        ans = _map.length;
        return ans;}
	@Override
	/////// add your code below ///////
	public int getPixel(int x, int y) {
        int ans;
        ans = _map[y][x];
        return ans;}
	@Override
	/////// add your code below ///////
	public int getPixel(Pixel2D p) {
        int ans;
        int x = p.getX();
        int y = p.getY();
        ans = _map[y][x];
        return ans;
	}
	@Override
	/////// add your code below ///////
	public void setPixel(int x, int y, int v) {
        _map[y][x] = v;}
	@Override
	/////// add your code below ///////
	public void setPixel(Pixel2D p, int v) {
        int x = p.getX();
        int y = p.getY();
        _map[y][x] = v;
	}
	@Override
	/** 
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans=0;
		/////// add your code below ///////
        int old_color = getPixel(xy);
        if (old_color == new_v) {return 0;} // already colored
        ans = fillPixel(xy.getX(), xy.getY(), old_color, new_v);
		///////////////////////////////////
		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		Pixel2D[] ans = null;  // the result.
		/////// add your code below ///////
        if (p1 == null || p2 == null){return null;} // if one of the point is null
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null; // if one of the points is red
        if(p1.equals(p2)){ return new Pixel2D[]{ new Index2D(p1) };}// if the points are equal then there is only one path, which is the point itself

        // Start from p1 and explore the map layer by layer (neighbors first).
        // Each valid neighbor (not obsColor and not visited) is added to a queue.
        // For each visited pixel we store where we came from.
        // When p2 is reached for the first time, the shortest path is found.
        int w = getWidth();
        int h = getHeight();

        boolean[][] visited = new boolean[h][w];
        Pixel2D[][] parent = new Pixel2D[h][w];

        java.util.Queue<Pixel2D> q = new java.util.LinkedList<>();

        q.add(new Index2D(p1));
        visited[p1.getY()][p1.getX()] = true;
        parent[p1.getY()][p1.getX()] = null;

        int[] dx = {1, -1, 0, 0}; // the neighbors
        int[] dy = {0, 0, 1, -1};

        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();
            int x = curr.getX();
            int y = curr.getY();

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isCyclic()) {
                    nx = (nx + w) % w;
                    ny = (ny + h) % h;
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                }

                if (visited[ny][nx]) continue;
                if (getPixel(nx, ny) == obsColor) continue;

                visited[ny][nx] = true;
                parent[ny][nx] = curr;

                Pixel2D next = new Index2D(nx, ny);

                if (next.equals(p2)) {
                    return buildPath(parent, p2);
                }

                q.add(next);
            }
        }
        ///////////////////////////////////
		return ans; // no path
	}
	@Override
	/////// add your code below ///////
	public boolean isInside(Pixel2D p) {
        boolean ans = true;
        if(_map.length -1 < p.getY()){ans = false;}
        if(_map[0].length -1< p.getX()){ans = false;}
        return ans;
	}

	@Override
	/////// add your code below ///////
	public boolean isCyclic() {return _cyclicFlag;}
	@Override
	/////// add your code below ///////
	public void setCyclic(boolean cy) {_cyclicFlag = cy;}
	@Override
	/////// add your code below ///////
	public Map2D allDistance(Pixel2D start, int obsColor) {
		/////// add your code below ///////
        if (start == null) return null;
        //if (getPixel(start) == obsColor) return null;
        int w = getWidth(); int h = getHeight();
        // new distance map
        int[][] dist = new int[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                dist[y][x] = -1 ;// initialize all distances to -1
            }
        }
        Queue<Pixel2D> q = new LinkedList<>();
        dist[start.getY()][start.getX()] = 0;
        q.add(new Index2D(start));

        int[] dx = {1, -1, 0, 0}; // the neighbors
        int[] dy = {0, 0, 1, -1};

        while (!q.isEmpty()) {
            Pixel2D curr = q.poll();
            int x = curr.getX();
            int y = curr.getY();

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isCyclic()) {
                    nx = (nx + w) % w;
                    ny = (ny + h) % h;}
                else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;}

                if (getPixel(nx, ny) == obsColor) continue;
                if (dist[ny][nx] != -1) continue; // already visited

                dist[ny][nx] = dist[y][x] + 1;
                q.add(new Index2D(nx, ny));
            }
        }
		///////////////////////////////////
		return new Map(dist);
	}


    ////////////////////// Private Methods ///////////////////////

    private int fillPixel(int x, int y, int old_color, int new_v) {
        if (x < 0 || x >= _map[0].length || y < 0 || y >= _map.length) {return 0;} // exception checking
        if (getPixel(x, y) != old_color) {return 0;} // if pixel doesn't have same color

        setPixel(x, y, new_v); // else, color by the new color
        int ans = 1;

        //if neighbor is old color, check his neighbor
        ans+=fillPixel(x + 1, y, old_color, new_v);
        ans+=fillPixel(x - 1, y, old_color, new_v);
        ans+=fillPixel(x, y + 1, old_color, new_v);
        ans+=fillPixel(x, y - 1, old_color, new_v);
        return ans;
    }

    private Pixel2D[] buildPath(Pixel2D[][] parent, Pixel2D end) {
        java.util.List<Pixel2D> path = new java.util.ArrayList<>();

        Pixel2D curr = end;
        while (curr != null) {
            path.add(curr);
            curr = parent[curr.getY()][curr.getX()];
        }

        java.util.Collections.reverse(path);
        return path.toArray(new Pixel2D[0]);
    }

}
