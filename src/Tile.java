// This is to represent a single space on the maze

public class Tile {

    private char symbol;


    // This Tile also tests that all inputs for the map are valid.
    public Tile(char sym, int numColors) {
        if (sym == '.' || ('?' <= sym && sym <= ('A' + numColors -1) || sym == '^' || ('a' <= sym && sym <= ('a' + numColors-1)) || sym == '#')) {
            symbol = sym;
        }
        else {
            System.err.print("Invalid input inside of map");
            System.exit(1);
        }
    }

    public char getSymbol() {
        return symbol;
    }

    public char render (Color_value v) {
        if (symbol == v.asButton() || symbol== v.asDoor()){
            return '.';
        }
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    public boolean isButton(Color_value curr) {
        if (('a' <= symbol && symbol <= 'z') || symbol == '^'){
            if (curr.asButton() == symbol)
                return false;
            return true;
        } else {
            return false;
        }
    }
    // making a thing to see if symbol at curr is a door, if so return ture or false figure itout
    public boolean isDoor(Color_value curr) {
        if (('A' <= symbol && symbol <= 'Z') || symbol == '^'){
            if (curr.asDoor() == symbol)
                return false;
            return true;
        } else {
            return false;
        }
    }
    public void changeTile(char sym) {
        symbol = sym;
    }



}
