public class Color_value {

    // we will store this as a button value
    private char value;

    public Color_value(char sym) {
        if (!(sym == '^' || (sym >= 'a' && sym <= 'z'))){
            System.err.print("Invalid Color\n");
            System.exit(1);
        }
        value = sym;
    }

    public static Color_value fromIndex(int index) {
        if (index > 26 || index < 0) {
            System.err.print("Color out of range 0-26");
            System.exit(1);
        }

        if (index == 0) {
            return new Color_value('^');
        }

        // letter
        return new Color_value((char) ('a' + index -1));

    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public char asButton() {
        return value;
    }

    public char asDoor() {
        return (char) (value - 'a' + 'A');
    }

    public int asIndex() {
        if (value == '^') {
            return 0;
        }
        else {
            return value - 'a' + 1;
        }
    }




}
