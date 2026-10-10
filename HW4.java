// ASSUMED Turtle API (Turtle.java not yet seen): penUp(), penDown(), setPosition(x,y),
// setColor(String), forward(d), left(deg). Adjust once the real Turtle.java is available.
public class HW4 {

    /** Draws one regular polygon at the turtle's current location and heading; restores both. */
    public static void drawPolygon(Turtle t, int sides, double sideLength, String color) {
        t.setColor(color);
        t.penDown();
        double turn = 360.0 / sides;
        for (int i = 0; i < sides; i++) {
            t.forward(sideLength);
            t.left(turn);
        }
    }

    /** Draws a rosette of petalCount polygons centered at (centerX, centerY), alternating colors. */
    public static void drawFlower(Turtle t, double centerX, double centerY,
                                  double petalSize, int petalSides, int petalCount,
                                  String color1, String color2) {
        t.penUp();
        t.setPosition(centerX, centerY);
        double turn = 360.0 / petalCount;
        for (int i = 0; i < petalCount; i++) {
            drawPolygon(t, petalSides, petalSize, i % 2 == 0 ? color1 : color2);
            t.left(turn);
        }
    }

    public static void main(String[] args) {
        // TODO: canvas/turtle setup once Turtle.java is available
    }
}
