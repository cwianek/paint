package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Oval extends Line {
    public Oval(MousePosition mousePosition, MousePosition mousePosition1, double lineWidth, Color color, boolean fill) {
        super(mousePosition,mousePosition1, lineWidth, color, fill);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setLineWidth(lineWidth);
        context.setStroke(color);
        if(fill) {
            context.setFill(color);
            context.fillOval(start.getX(),start.getY(),end.getX(),end.getY());
        }else{
            context.strokeOval(start.getX(),start.getY(),end.getX(),end.getY());
        }
    }
}
