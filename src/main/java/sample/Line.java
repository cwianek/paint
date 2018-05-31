package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.event.MouseEvent;

public class Line {
    MousePosition start;
    MousePosition end;
    double lineWidth;
    Color color;
    boolean fill;

    public Line(){

    }

    public Line(MousePosition start, MousePosition end, double lineWidth, Color color, boolean fill) {
        this.start = start;
        this.end = end;
        this.lineWidth = lineWidth;
        this.color = color;
        this.fill = fill;
    }

    public void draw(GraphicsContext context){
        context.setLineWidth(lineWidth);
        context.setStroke(color);
        context.strokeLine(start.getX(),start.getY(),end.getX(),end.getY());
    }
}
