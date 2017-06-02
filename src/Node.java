/**
 * Created by Provorny on 6/2/2017.
 */
import java.awt.*;
import java.util.Hashtable;

/**
 * Created by cmiron on 5/31/17.
 */
public class Node {
    private int ID_name;
    private int ID_parent;
    private int size;
    private String arg;

    public int gx;
    public int fx;
    public int hx;


    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    private int[][] state;
    private int[][] goal;

    public Node(int gx, int size, int[][] state, int[][] goal, String arg)
    {
        this.gx = gx;
        this.size = size;

        this.state = state;
        this.goal = goal;
        this.arg = arg;
        calculate_h();
    }

    public Node(int gx, int size, int[][] state, int[][] goal)
    {
        this.gx = gx;
        this.size = size;

        this.state = state;
        this.goal = goal;
        this.arg = null;
        calculate_h();
    }

    public int getID_name() {
        return ID_name;
    }

    public void setID_name(int ID_name) {
        this.ID_name = ID_name;
    }

    public int getID_parent() {
        return ID_parent;
    }

    public void setID_parent(int ID_parent) {
        this.ID_parent = ID_parent;
    }

    private void calculate_h()
    {
        if(arg == null || arg.equals("manhattan"))
            manhattan();
        else if(arg.equals("euclid"))
            euclid();
        else if(arg.equals("hamming"))
            hamming();
        else
            Parser.printErr(9, 0);
        fx = gx + hx;
    }

    private void manhattan()
    {
        Point see;
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(state[i][j] == 0)
                    continue ;
                see = getCoordinates(state[i][j]);
                hx+= (Math.abs(i - see.x) + Math.abs(j - see.y));
            }
        }
    }

    private void hamming()
    {
        for(int i=0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(state[i][j] == 0)
                    continue ;
                if(state[i][j] != goal[i][j])
                    hx++;
            }
        }
    }

    private Point getCoordinates(int lookFor)
    {
        Point point = new Point();

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(goal[i][j] == lookFor)
                {
                    point.x = i;
                    point.y = j;
                }
            }
        return point;
    }

    private void euclid()
    {
        Point see;
        for(int i=0; i<size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] == 0)
                    continue;
                see = getCoordinates(state[i][j]);
                int one = Math.abs(i - see.x) * Math.abs(i - see.x);
                int two = Math.abs(j - see.y) * Math.abs(j - see.y);
                hx += (one + two);
            }
        }
    }

    public void toStr()
    {
        System.out.println("G(x) = " + gx);
        System.out.println("H(x) = " + hx);
        System.out.println("F(X) = " + fx);
        Board.print_MATRIX(state);
    }
}
