package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends Line {

    public Rectangle(MousePosition start, MousePosition end, double lineWidth, Color color, boolean fill) {
        super(start, end, lineWidth, color, fill);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setLineWidth(lineWidth);
        context.setStroke(color);
        if (fill) {
            context.setFill(color);
            context.fillRect(start.getX(), start.getY(), end.getX(), end.getY());
        } else {
            context.strokeRect(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }
}
