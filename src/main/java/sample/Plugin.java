package sample;

import javafx.scene.control.Button;

import java.lang.reflect.Method;

public class Plugin {
    private String classname;
    private Method method;
    private Object object;
    private Button b;

    public Plugin(String classname, Method method, Object object) {
        this.classname = classname;
        this.method = method;
        this.object = object;
    }

    public String getClassname() {
        return classname;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }

    public void setB(Button b) {
        this.b = b;
    }

    public Button getB() {
        return b;
    }
}
