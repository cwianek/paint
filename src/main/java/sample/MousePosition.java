package sample;

public class MousePosition {



    private double x;
    private  double y;

    public MousePosition(double sceneX, double sceneY) {
        x = sceneX;
        y= sceneY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
