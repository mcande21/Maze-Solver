import javax.xml.stream.events.Characters;
import java.util.*;

public class Maze {
    // store dimensions
    private int width, height, numColors;
    private boolean solution = false;

    // 2D ArrayList to store all tiles
    // outer AL stores rows
    // Inner AL stores columns
    private ArrayList<ArrayList<Tile>> map;

    // 3D Arraylist to store wether we reach a tile
    // in a given color
    private ArrayList<ArrayList<ArrayList<Character>>> reached;

    // recheable collection
    private ArrayDeque<State> reachableCollection;
    // States to represent start and finish
    private State start, finish;

    private static final char FROM_START = '@';
    private static final char NOT_REACHED = '#';
    private static final char GO_NORTH = 'N';
    private static final char GO_SOUTH = 'S';
    private static final char GO_EAST = 'E';
    private static final char GO_WEST = 'W';

    public Maze(Scanner in) {

        numColors = in.nextInt();
        height = in.nextInt();
        width = in.nextInt();

        // Error checking with map dimensions
        if (height < 1 || width < 1) {
            System.err.print("Map dimensions are not up to standard");
            System.exit(1);
        }
        // Error checking with number of colors
        if (numColors < 0 || numColors > 26) {
            System.err.print("Color index out of range.");
            System.exit(1);
        }

        // read the rest of the line
        in.nextLine();

        // allocate enough space for the height rows
        map = new ArrayList<>(height);

        // helper variable for tracking how many rows we've read
        int row = 0;
        int initial_sym = 0;
        int target_sym = 0;
        while (in.hasNextLine()) {
            if (row >= height) {
                break;
            }
            // read a line
            String line = in.nextLine();

            // check if comment and skip
            if (line.length() >= 2 && line.charAt(0) == '/' && line.charAt(1) == '/') {
                continue;
            }

            // we know we have a line of tiles to process
            // Set boolean value
            ArrayList<Tile> rowofTiles = new ArrayList<>(width);
            for (int i = 0; i < line.length(); i++) {
                char sym = line.charAt(i);
                // assigning start value
                if (sym == '@') {
                    start = new State(Color_value.fromIndex(0), new Point(i, row));
                }
                // assigning finish value
                if (sym == '?') {
                    finish = new State(Color_value.fromIndex(0), new Point(i, row));
                }
                // error checking for multiple starts or finishes
                rowofTiles.add(new Tile(sym, numColors));
                if (line.charAt(i) == '@')
                    initial_sym++;
                if (line.charAt(i) == '?')
                    target_sym++;
            }

            map.add(rowofTiles);
            row++;
        }
        // check for error checking for multiple starts and finishes
        if (initial_sym != 1 || target_sym != 1) {
            System.err.print("Invalid input for @ or ? was provided.\n");
            System.exit(1);
        }
    }

    public void search(Config c) {
        // Step 1:
        // fill in our reached array list with false
        reached = new ArrayList<>(numColors + 1);
        for (int x = 0; x <= numColors; x++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Character>> rowlist = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                // array list for colomun values
                ArrayList<Character> collist = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    collist.add(NOT_REACHED);

                }
                rowlist.add(collist);
            }
            reached.add(rowlist);
        }

        // initialize our reachable collection
        reachableCollection = new ArrayDeque<>();

        // Initialize a String builder
        StringBuilder Massive_String = new StringBuilder();

        // step 2
        markReached(start, '@');
        reachableCollection.addFirst(start);

        if (c.isCheckpoint2())
            Massive_String.append(" adding (").append(start.getColor_value()).append(", (").append(start.getPoint().getRow()).append(", ").append(start.getPoint().getCol()).append("))\n");

        // step 3
        int counter = 0;
        while (!reachableCollection.isEmpty()) {
            counter++;
            State curr = null;
            if (c.isQueuemode()) {
                curr = reachableCollection.pollFirst();
            }
            if (c.isStackmode()) {
                curr = reachableCollection.pop();
            }

            if (c.isCheckpoint2())
                Massive_String.append(counter).append(": processing (").append(curr.getColor_value()).append(", (").append(curr.getPoint().getRow()).append(", ").append(curr.getPoint().getCol()).append("))\n");


            if (map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol()).isButton(curr.getColor_value())) {
                // it's a button!!
                State button = new State(new Color_value((map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol()).getSymbol())), curr.getPoint());
                if (canBeReached(button)) {
                    markReached(button, curr.getColor_value().asButton());
                    if (c.isQueuemode()) {
                        reachableCollection.addLast(button);
                    }
                    if (c.isStackmode()) {
                        reachableCollection.addFirst(button);
                    }
                    //System.out.printf("  adding (%s, (%d, %d))\n", button.getColor_value(), button.getPoint().getRow(), button.getPoint().getCol());
                    if (c.isCheckpoint2())
                        Massive_String.append(" adding (").append(button.getColor_value()).append(", (").append(button.getPoint().getRow()).append(", ").append(button.getPoint().getCol()).append("))\n");
                }
            } else {
                // check north
                State north = new State(curr.getColor_value(), curr.getPoint().north());
                if (canBeReached(north)) {
                    markReached(north, GO_NORTH);
                    if (c.isQueuemode()) {
                        reachableCollection.addLast(north);
                    }
                    if (c.isStackmode()) {
                        reachableCollection.addFirst(north);
                    }
                    //System.out.printf("  adding (%s, (%d, %d))\n", north.getColor_value(), north.getPoint().getRow(), north.getPoint().getCol());
                    if (c.isCheckpoint2())
                        Massive_String.append(" adding (").append(north.getColor_value()).append(", (").append(north.getPoint().getRow()).append(", ").append(north.getPoint().getCol()).append("))\n");
                }
                // check east
                State east = new State(curr.getColor_value(), curr.getPoint().east());
                if (canBeReached(east)) {
                    markReached(east, GO_EAST);
                    if (c.isQueuemode()) {
                        reachableCollection.addLast(east);
                    }
                    if (c.isStackmode()) {
                        reachableCollection.addFirst(east);
                    }
                    //System.out.printf("  adding (%s, (%d, %d))\n", east.getColor_value(), east.getPoint().getRow(), east.getPoint().getCol());
                    if (c.isCheckpoint2())
                        Massive_String.append(" adding (").append(east.getColor_value()).append(", (").append(east.getPoint().getRow()).append(", ").append(east.getPoint().getCol()).append("))\n");
                }
                // check south
                State south = new State(curr.getColor_value(), curr.getPoint().south());
                if (canBeReached(south)) {
                    markReached(south, GO_SOUTH);
                    if (c.isQueuemode()) {
                        reachableCollection.addLast(south);
                    }
                    if (c.isStackmode()) {
                        reachableCollection.addFirst(south);
                    }
                    //System.out.printf("  adding (%s, (%d, %d))\n", south.getColor_value(), south.getPoint().getRow(), south.getPoint().getCol());
                    if (c.isCheckpoint2())
                        Massive_String.append(" adding (").append(south.getColor_value()).append(", (").append(south.getPoint().getRow()).append(", ").append(south.getPoint().getCol()).append("))\n");
                }
                // check west
                State west = new State(curr.getColor_value(), curr.getPoint().west());
                if (canBeReached(west)) {
                    markReached(west, GO_WEST);
                    if (c.isQueuemode()) {
                        reachableCollection.addLast(west);
                    }
                    if (c.isStackmode()) {
                        reachableCollection.addFirst(west);
                    }
                    //System.out.printf("  adding (%s, (%d, %d))\n", west.getColor_value(), west.getPoint().getRow(), west.getPoint().getCol());
                    if (c.isCheckpoint2())
                        Massive_String.append(" adding (").append(west.getColor_value()).append(", (").append(west.getPoint().getRow()).append(", ").append(west.getPoint().getCol()).append("))\n");
                }
                // If new addition equals finish, set solution to true and break.
                if (north.equals(finish) || south.equals(finish) || east.equals(finish) || west.equals(finish)) {
                    solution = true;
                    break;
                }
            }
        }
        // solution has no solution
        if (!(solution)) {
            no_solution();
            System.exit(0);
        }
        // Checkpoint 2 print
        if (c.isCheckpoint2())
            System.out.print(Massive_String);
    }

    private void no_solution() {
        // print header stating no solution.
        System.out.println("No solution.\nReachable:");
        //change all unreachable tiles to '#' and print map
        for (int row = 0; row < height; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < width; col++) {
                // check to see if point at row and col was reached
                if (wasReached(row, col)) {
                    sb.append(map.get(row).get(col));
                } else {
                    sb.append('#');
                }
            }
            // row is output
            System.out.print(sb + "\n");
        }
    }

    public boolean wasReached(int row, int col) {
        for (int color = 0; color < numColors + 1; color++) {
            if (reached.get(color).get(row).get(col).equals('#')) {
                continue;
            }
            // return true if '#' was found in any color
            return true;
        }
        // return false if reached
        return false;
    }


    public void printSolution(Config c) {
        List<State> backtrack = backtrack();

        // check output mode for Map or List
        if (c.isMapoutputMode()) {
            printMap(backtrack);
        } else {
            // print the list
            StringBuilder solution = new StringBuilder();
            for (int i = backtrack.size() - 1; i >= 0; i--) {
                State s = backtrack.get(i);
                solution.append("(").append(s.getColor_value()).append(", (").append(s.getPoint().getRow()).append(", ").append(s.getPoint().getCol()).append("))\n");
            }
            System.out.print(solution + "\n");
        }
    }

    private void printMap(List<State> backtrack) {
        // first, we need to create a dooplicate map for outputs
        // look very much like our reached map
        ArrayList<ArrayList<ArrayList<Character>>> output = new ArrayList<>(numColors + 1);
        for (int x = 0; x < numColors + 1; x++) {
            // make an array list to store rows for this color
            ArrayList<ArrayList<Character>> rowlist = new ArrayList<>(height);
            for (int row = 0; row < height; row++) {
                // array list for colomun values
                ArrayList<Character> collist = new ArrayList<>(width);
                for (int col = 0; col < width; col++) {
                    collist.add(map.get(row).get(col).render(Color_value.fromIndex(x)));
                }
                rowlist.add(collist);
            }
            output.add(rowlist);
        }
        for (int i = 1; i < numColors + 1; i++) {
            output.get(i).get(start.getPoint().getRow()).set(start.getPoint().getCol(), '.');
        }
        // starter map
        // walk through the solution and update out characters stored along the path
        for (State curr : backtrack) {
            if (curr == backtrack.get(0) || curr == backtrack.get(backtrack.size() - 1)) {
                continue;
            }
            Tile currTile = map.get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
            // If tile is '.' switch to '+' to show path
            if (currTile.getSymbol() == '.') {
                output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
            // Tile is a button
            } else if (currTile.getSymbol() >= 'a' && currTile.getSymbol() <= 'z' || currTile.getSymbol() == '^') {
                char tmp = getBacktrack(curr);
                if (tmp >= 'a' && tmp <= 'z' || tmp == '^') {
                    // Tile is button we are starting at
                    output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '@');
                } else if (currTile.getSymbol() == curr.getColor_value().asButton()) {
                    // Tile is same color value we are walking through
                    output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
                } else {
                    // Tile is button we are pressing
                    output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '%');
                }
            } else if (currTile.getSymbol() >= 'A' && currTile.getSymbol() <= 'Z') {
                char tmp = getBacktrack(curr);
                if (tmp >= 'A' && tmp <= 'Z' || tmp == '^') {
                    // Tile is door we are walking through
                    output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
                }
                // If we have to walk back through the start we change it to a '+'
            } else if (currTile.getSymbol() == '@' && curr.getColor_value().asIndex() != start.getColor_value().asIndex()) {
                output.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).set(curr.getPoint().getCol(), '+');
            }

        }

        // print out the map
        for (int co = 0; co <= numColors; co++) {
            Color_value curr = Color_value.fromIndex(co);
            System.out.print("// color " + curr + "\n");
            for (int row = 0; row < height; row++) {
                StringBuilder sb = new StringBuilder();
                for (int col = 0; col < width; col++) {
                    sb.append(output.get(co).get(row).get(col));
                }
                System.out.print(sb + "\n");
            }
        }
    }

    /**
     * Backtrack through the reached items to find the start
     *
     * @return a list of the states
     */
    private List<State> backtrack() {
        // start from the end point
        State curr = null;
        ArrayList<State> backtrack = new ArrayList<>();
        for (int color = 0; color <= numColors; color++) {
            // check backtrack to see if reached
            char tmp = reached.get(color).get(finish.getPoint().getRow()).get(finish.getPoint().getCol());
            if (tmp != NOT_REACHED) {
                // This was the color we reached the finish in
                curr = new State(Color_value.fromIndex(color), finish.getPoint());
                // Stop looping
                break;
            }
        }

        // at this point, we should have a value in curr
        // if no value (null) --> no solution
        if (curr == null) {
            return backtrack;
        }
        // commence backtracking
        backtrack.add(curr);
        // keep looping while our current state is not the starting point
        // while backtrack for current is not '@'
        while ((getBacktrack(curr) != FROM_START)) {
            //get one more state and add to our list
            char dir = getBacktrack(curr);

            if (dir == GO_NORTH) {
                curr = new State(curr.getColor_value(), curr.getPoint().south());
            } else if (dir == GO_SOUTH) {
                curr = new State(curr.getColor_value(), curr.getPoint().north());
            } else if (dir == GO_EAST) {
                curr = new State(curr.getColor_value(), curr.getPoint().west());
            } else if (dir == GO_WEST) {
                curr = new State(curr.getColor_value(), curr.getPoint().east());
            } else if (dir >= 'a' && dir <= 'z' || dir == '^') {
                // pressed a button
                curr = new State(new Color_value(dir), curr.getPoint());
            } else {
                System.err.println("reached a backtrack location with no place to go: " + curr);
                System.exit(1);
            }
            backtrack.add(curr);
        }
        // Reverse this here
        return backtrack;


    }

    private char getBacktrack(State curr) {
        return reached.get(curr.getColor_value().asIndex()).get(curr.getPoint().getRow()).get(curr.getPoint().getCol());
    }

    public void printState() {
        for (State s : reachableCollection) {
            System.out.printf("%s (%s):", s.getColor_value(), s.getPoint().toString());
            System.out.println();
        }
    }

    /**
     * mark that a state was reached and from where we came
     */
    private void markReached(State st, char from) {
        int colorIdx = st.getColor_value().asIndex();
        Point p = st.getPoint();
        reached.get(colorIdx).get(p.getRow()).set(p.getCol(), from);
    }

    private boolean canBeReached(State st) {
        int colorIdx = st.getColor_value().asIndex();
        Point p = st.getPoint();

        if (p.getCol() < 0 || p.getCol() >= width || p.getRow() < 0 || p.getRow() >= height) {
            // outside of bounds
            return false;
        }
        // Already Reached?
        if (reached.get(colorIdx).get(p.getRow()).get(p.getCol()) != NOT_REACHED) {
            return false;
        }

        // check if this particular location is reachable (not an obstacle)
        Tile curr = map.get(p.getRow()).get(p.getCol());
        if (curr.getSymbol() == '#')
            return false;
        if ('A' <= curr.getSymbol() && curr.getSymbol() <= 'Z') {
            return !curr.isDoor(st.getColor_value());
        }
        return true;
    }


    public void printMap() {
        // numColors +1: all closed with each of the colors
        for (int i = 0; i < numColors + 1; i++) {
            Color_value curr = Color_value.fromIndex(i);
            System.out.print("//color " + curr + "\n");
            for (ArrayList<Tile> row : map) {
                StringBuilder sb = new StringBuilder();
                for (Tile col : row) {
                    sb.append(col.render(curr));
                }
                // row is output
                System.out.print(sb + "\n");
            }

        }
    }

}
