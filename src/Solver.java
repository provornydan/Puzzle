/**
 * Created by Provorny on 6/2/2017.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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
        System.out.println("Name = "+path.getID_name());
        ArrayList<Node> drum = new ArrayList<>();
        drum.add(path);
        int name = path.getID_parent();

        while(name != 0)
        {
            for(Node o : close)
            {
                if(o.getID_name() == name) {
                    drum.add(o);
                    name = o.getID_parent();
                }
            }
//            System.out.println("Name = "+name);
        }
        Collections.reverse(drum);
        for(Node m:drum)
            m.toStr();
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
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
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
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
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
        if(pos.x > 0)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x-1][pos.y];
            a[pos.x-1][pos.y] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal);
            new_node.setID_name(++ids);
            new_node.setID_parent(father.getID_name());
            temp.add(new_node);
        }
    }

    private void add_down(Point pos, Node father, ArrayList<Node> temp)
    {
        if(pos.x < size - 1)
        {
            int [][] a = new int[size][];
            for(int i = 0; i < size; i++)
                a[i] = father.getState()[i].clone();
            a[pos.x][pos.y] = a[pos.x+1][pos.y];
            a[pos.x+1][pos.y] = 0;

            Node new_node = new Node(gx, board.Size, a, board.goal);
            new_node.setID_name(++ids);
            new_node.setID_parent(father.getID_name());
            temp.add(new_node);
        }
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

    private boolean replece_in_open(Node el)
    {
        boolean flag = false;

        for(int i=0; i<open.size(); i++)
        {
            if(Node.stateEqual(open.get(i), el)) {
                if (el.fx < open.get(i).fx)
                    open.set(i, el);
                flag = true;
            }
        }
        return flag;
    }

    private boolean move_from_closed(Node el)
    {
        boolean flag = false;

        for(int i=0; i<close.size(); i++)
        {
            if(Node.stateEqual(close.get(i), el)) {
                if (el.fx < close.get(i).fx) {
                    close.remove(i);
                    open.add(el);
                }
                flag = true;
            }
        }
        return flag;
    }


    public void solve()
    {
        ArrayList<Node> tempList;

        Node first = new Node(gx, board.Size, board.getMatrix(), board.goal);
        first.setID_name(++ids);
        first.setID_parent(0);
        open.add(first);
        nrOpen++;
        while(!open.isEmpty())
        {
            int poz = 0;
            if(open.size() > maxOpen)
                maxOpen = open.size();
            open.sort(new CustomComparator());
//            showAndWait();
            if(matrixEqual(open.get(0).getState(), board.goal)) {
                showPath(open.get(0));
                return ;
            }
            else
            {
                gx++;
                tempList = generateList(open.get(0));
                for(Node el : tempList)
                {
                    if(!replece_in_open(el))
                        if(!move_from_closed(el)) {
                            nrOpen++;
                            open.add(el);
                        }
                }
            }
            close.add(open.get(0));
            open.remove(0);
        }
    }

    private void showAndWait()
    {
        System.out.println("Open :");
        for(Node n : open)
            n.toStr();
        System.out.println("Close :");
        for(Node n : close)
            n.toStr();
        Parser.promptEnterKey();
    }
}
