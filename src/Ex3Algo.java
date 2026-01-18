//package assignments.Ex3;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
        }
        int blue = Game.getIntColor(Color.BLUE, 0);

        int[][] board = game.getGame(0);
        String pos = game.getPos(0).toString(); // pacman position
        String[] p = pos.split(",");
        int x = Integer.parseInt(p[0]); int y = Integer.parseInt(p[1]); //extracting my position from string

        Map2D map = new Map(board);
        Index2D pixel = new Index2D(x,y);

        //Map2D dist = map.allDistance(pixel,blue); // start = pixel, obsColor = blue
        Pixel2D closestPink = closest_pink(map, pixel); // search for the closest pink pixel
        Pixel2D[] path = map.shortestPath(pixel,closestPink,blue);
        if (path == null|| path.length < 2) return randomDir();
        Pixel2D next = path[1];

        int dx = next.getX() - pixel.getX();
        int dy = next.getY() - pixel.getY();

        int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
        if (dx == 1) return right;
        if (dx == -1) return left;
        if (dy == 1) return down;
        if (dy == -1) return up;
        _count++;
		return 0;
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
    // 3 main cases in the game:
    public static Pixel2D closest_pink(Map2D map, Pixel2D pixel) { // pacman wants to eat pink pixels
        Pixel2D ans = null;
        int minDist = Integer.MAX_VALUE;
        int pink = Game.getIntColor(Color.PINK, 0);
        for (int x=0;x< map.getWidth();x++){
            for (int y=0; y< map.getHeight();y++){
                if (map.getPixel(x,y) == pink){
                    int d = Math.abs(pixel.getX() - x) + Math.abs(pixel.getY() - y); // distance
                    if (d < minDist) {
                        minDist = d;
                        ans = new Index2D(x, y);
                    }
                }
            }
        }
        return ans;
    }
    private static int chase() { // pacman wants to chase small monsters
        return 0;}
    private static int runaway() { // pacman wants to runaway from the monsters
        return 0; }
}