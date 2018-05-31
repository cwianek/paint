package sample;

import javafx.scene.image.Image;

import java.util.List;

public class Drawable {
    private Image image;
    private Line line;
    private DrawableType drawableType;
    private List<MousePosition> path;

    public Drawable(Line line, DrawableType drawableType) {
        this.line = line;
        this.drawableType = drawableType;
    }

    public Drawable(Image image, DrawableType drawableType) {
        this.image = image;
        this.drawableType = drawableType;
    }

    public Drawable(List<MousePosition> path, DrawableType drawableType){
        this.path = path;
        this.drawableType = drawableType;
    }

    public Image getImage() {
        return image;
    }

    public Line getLine() {
        return line;
    }

    public DrawableType getDrawableType() {
        return drawableType;
    }
}

