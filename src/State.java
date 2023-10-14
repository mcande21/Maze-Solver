import java.awt.*;
import java.util.Objects;

public class State {
    private Color_value c;
    private Point p;

    public State (Color_value c, Point p){
        this.c = c;
        this.p = p;
    }
    public Color_value getColor_value() {return c;}
    public Point getPoint() {return p;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return p.equals(state.p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, p);
    }
}


