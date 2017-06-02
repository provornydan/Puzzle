/**
 * Created by Provorny on 6/2/2017.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by cmiron on 5/31/17.
 */
public class Solver {


    private int size;
    private Board board;

    private ArrayList<Node>  open = new ArrayList<>();
    private ArrayList<Node> close = new ArrayList<>();
    private int nrOpen;
    private int maxOpen;
    private int gx;
    private int ids;

    public Solver(Board board)
    {
        this.board = board;
        this.size = board.getSize();
    }

    public class CustomComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            Integer a = o1.fx;
            Integer b = o2.fx;
            return a.compareTo(b);
        }
    }

    public boolean matrixEqual(int [][]a, int [][]b)
    {
        for(int i = 0; i<size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(a[i][j] != b[i][j])
                    return false;
            }
        }
        return true;
    }

    public void showPath(Node path)
    {
        int name = path.getID_name();

        while(name != 0)
        {
            for(Node o : close)
            {
                if(o.getID_name() == name)
                    name = o.getID_parent();
            }
            System.out.println("Name = "+name);
        }
    }

    private Point find_zero(int [][]a)
    {
        Point pos = new Point();

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(a[i][j] == 0)
                {
                    pos.x = i;
                    pos.y = j;
                }
            }
        return pos;
    }

    private void add_left(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.y > 0)
        {
            int [][] a;
            a = father.getState().clone();
            a[pos.x][pos.y] = a[pos.x][pos.y-1];
            a[pos.x][pos.y-1] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal);
            new_node.setID_name(++ids);
            new_node.setID_parent(father.getID_name());
            temp.add(new_node);
        }
    }

    private void add_right(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.y < size - 1)
        {
            int [][] a;
            a = father.getState().clone();
            a[pos.x][pos.y] = a[pos.x][pos.y+1];
            a[pos.x][pos.y+1] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal);
            new_node.setID_name(++ids);
            new_node.setID_parent(father.getID_name());
            temp.add(new_node);
        }
    }

    private void add_up(Point pos, Node father, ArrayList<Node> temp)
    {

    }

    private void add_down(Point pos, Node father, ArrayList<Node> temp)
    {

    }


    public ArrayList<Node> generateList(Node father)
    {
        ArrayList<Node> temp = new ArrayList<>();

        Point zeroPos = find_zero(father.getState());
        add_left(zeroPos, father, temp);
        add_right(zeroPos, father, temp);
        add_up(zeroPos, father, temp);
        add_down(zeroPos, father, temp);

        return temp;
    }

    public void solve()
    {
        ArrayList<Node> tempList;

        Node first = new Node(gx, board.Size, board.getMatrix(), board.goal);
        first.setID_name(++ids);
        first.setID_parent(0);
        open.add(first);
        while(!open.isEmpty())
        {
            nrOpen++;
            if(open.size() > maxOpen)
                maxOpen = open.size();
            open.sort(new CustomComparator());
            if(matrixEqual(open.get(0).getState(), board.goal))
                showPath(open.get(0));
            else
            {
                gx++;
                tempList = generateList(open.get(0));
            }
        }
    }
}
