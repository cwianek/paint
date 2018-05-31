package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Path extends Line {

    List<MousePosition> path;
    MousePosition start;

    public Path(List<MousePosition> path, MousePosition start, double lineWidth, Color color) {
        this.path = path;
        this.start = start;
        this.lineWidth = lineWidth;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.beginPath();
        context.setLineWidth(lineWidth);
        context.setStroke(color);
        context.moveTo(start.getX(), start.getY());
        for(MousePosition m: path){
            context.lineTo(m.getX(), m.getY());
        }
        context.stroke();

    }
}
