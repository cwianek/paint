package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {

    Stage stage;
    List<Plugin> plugins;


    @FXML
    Menu fileMenu;

    @FXML
    MenuItem language;

    @FXML
    MenuItem saveItem;


    @FXML
    MenuItem loadItem;

    @FXML
    MenuItem closeItem;

    @FXML
    Menu settings;

    @FXML
    FlowPane pluginsPane;

    @FXML
    Canvas canvas;

    @FXML
    Slider slider;

    @FXML
    ColorPicker color;

    @FXML
    ToggleButton fill;

    @FXML
    ToggleButton line;

    @FXML
    ToggleButton brush;

    @FXML
    ToggleButton rectangle;

    @FXML
    ToggleButton oval;

    DrawingShape drawingShape = DrawingShape.LINE;

    GraphicsContext gc;
    MousePosition mousePosition;
    List<Drawable> lines = new ArrayList<>();
    List<Drawable> linesRemoved = new ArrayList<>();
    List<MousePosition> path;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        brushAction();
        color.setValue(Color.BLACK);
        englishLanguage();
    }

    @FXML
    public void onBack(){
        if(lines.size()>0) {
            Drawable shape = lines.remove(lines.size() - 1);
            linesRemoved.add(shape);
            clear();
            drawShapes();
        }
    }

    private ResourceBundle resourceBundle;
    private Locale locale;

    @FXML
    private void polishLanguage(){
        locale = new Locale("pl","PL");
        resourceBundle = ResourceBundle.getBundle("lang", locale);
        switchLanguage();
    }

    @FXML
    private void englishLanguage(){
        locale = new Locale("en","US");
        resourceBundle = ResourceBundle.getBundle("lang", locale);
        switchLanguage();
    }

    private void switchLanguage(){
        line.setText(resourceBundle.getString("line"));
        brush.setText(resourceBundle.getString("brush"));
        rectangle.setText(resourceBundle.getString("rectangle"));
        oval.setText(resourceBundle.getString("oval"));
        fill.setText(resourceBundle.getString("fill"));
        closeItem.setText(resourceBundle.getString("close"));
        saveItem.setText(resourceBundle.getString("saveItem"));
        loadItem.setText(resourceBundle.getString("loadItem"));
        settings.setText(resourceBundle.getString("settings"));
        fileMenu.setText(resourceBundle.getString("file"));
        language.setText(resourceBundle.getString("language"));
        if(plugins != null){
        for(Plugin p: plugins){
            boolean translation = true;
            try{
                resourceBundle.getString(p.getClassname());
            }catch (Exception e){
                translation = false;
            }
            if(translation){
                 p.getB().setText(resourceBundle.getString(p.getClassname()));
            }else{
                p.getB().setText(p.getClassname());
            }
        }}
    }

    @FXML
    public void saveImage(ActionEvent e) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //Show save file dialog
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);

        if(file != null){
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image snapshot = canvas.snapshot(params, null);
            BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
            ImageIO.write(image, "png", file);
        }
    }

    @FXML
    public void loadImage(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image1 = new Image(file.toURI().toString(), 600, 400, true, false);
            Drawable drawable = new Drawable(image1,DrawableType.Image);
            lines.add(drawable);
            drawShapes();
        }
    }


    @FXML
    public void onForward(){
        if(linesRemoved.size()>0){
            Drawable shape = linesRemoved.remove(linesRemoved.size()-1);
            lines.add(shape);
            clear();
            drawShapes();
        }
    }

    private void clear(){
        gc.clearRect(0, 0, 800, 600);
    }

    private void deselect(){
        rectangle.setSelected(false);
        oval.setSelected(false);
        line.setSelected(false);
        brush.setSelected(false);
    }

    @FXML
    public void drawLine() {
        deselect();
        line.setSelected(true);
        drawingShape = DrawingShape.LINE;
    }

    @FXML
    public void drawRectangle() {
        deselect();
        rectangle.setSelected(true);
        drawingShape = DrawingShape.RECTANGLE;
    }

    @FXML
    public void drawOval() {
        deselect();
        oval.setSelected(true);
        drawingShape = DrawingShape.OVAL;
    }

    @FXML
    public void brushAction(){
        deselect();
        brush.setSelected(true);
        drawingShape = DrawingShape.BRUSH;
    }

    @FXML
    public void onMouseDragStart(MouseEvent e) {
        mousePosition = new MousePosition(e.getX(), e.getY());
        if(drawingShape == DrawingShape.BRUSH) {
            gc.beginPath();
            gc.moveTo(mousePosition.getX(), mousePosition.getY());
            gc.stroke();
            path = new ArrayList<>();
        }
    }

    @FXML
    public void trans(){
    }


    @FXML
    public void onMouseDragEnd(MouseEvent e) {
        Line shape = null;
        switch (drawingShape) {
            case LINE:
                shape = new Line(mousePosition, new MousePosition(e.getX(), e.getY()), slider.getValue(), color.getValue(), fill.isSelected());
                break;
            case RECTANGLE:
                shape = new Rectangle(mousePosition, new MousePosition(e.getX() - mousePosition.getX(), e.getY() - mousePosition.getY()), slider.getValue(), color.getValue(), fill.isSelected());
                break;
            case OVAL:
                shape = new Oval(mousePosition, new MousePosition(e.getX() - mousePosition.getX(), e.getY() - mousePosition.getY()), slider.getValue(), color.getValue(), fill.isSelected());
                break;
            case BRUSH:
                List<MousePosition> copy = new ArrayList<>();
                for(MousePosition m: path){
                    copy.add(m);
                }
                shape = new Path(copy, mousePosition, slider.getValue(), color.getValue());
                path = new ArrayList<>();
                gc.closePath();
                gc.beginPath();
                break;
        }
        if(shape != null) {
            lines.add(new Drawable(shape, DrawableType.Shape));
        }
        drawShapes();
    }

    @FXML
    public void onMouseMoved(MouseEvent e) {
        gc.setLineWidth(slider.getValue());
        gc.setStroke(color.getValue());
        if (fill.isSelected()) {
            gc.setFill(color.getValue());
        }
        clear();
        drawShapes();
        switch (drawingShape) {
            case LINE:
                gc.strokeLine(mousePosition.getX(), mousePosition.getY(), e.getX(), e.getY());
                break;
            case RECTANGLE:
                gc.strokeRect(mousePosition.getX(), mousePosition.getY(), e.getX() - mousePosition.getX(), e.getY() - mousePosition.getY());
                break;
            case OVAL:
                gc.strokeOval(mousePosition.getX(), mousePosition.getY(), e.getX() - mousePosition.getX(), e.getY() - mousePosition.getY());
                break;
            case BRUSH:
                MousePosition mp = new MousePosition(e.getX(),e.getY());
                path.add(mp);

                gc.beginPath();
                gc.setLineWidth(slider.getValue());
                gc.setStroke(color.getValue());
                gc.moveTo(mousePosition.getX(), mousePosition.getY());
                gc.stroke();
                for(MousePosition m: path){
                    gc.lineTo(m.getX(), m.getY());
                    gc.stroke();
                }
                break;
        }
    }

    private void drawShapes() {
        for (Drawable l : lines) {
            if(l.getDrawableType() == DrawableType.Shape) {
                l.getLine().draw(gc);
            }else{
                gc.drawImage(l.getImage(),0,0);
            }
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;

    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;

        for(Plugin p: plugins){
            boolean translation = true;
            try{
                resourceBundle.getString(p.getClassname());
            }catch (Exception e){
                translation = false;
            }
            Button b = null;
            if(translation){
                b =new Button(resourceBundle.getString(p.getClassname()));
            }else{
                b = new Button(p.getClassname());
            }
            p.setB(b);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Image image = (WritableImage) p.getMethod().invoke(p.getObject(),canvas);
                        gc.drawImage(image,0,0);
                        Drawable drawable = new Drawable(image, DrawableType.Image);
                        lines.add(drawable);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            pluginsPane.getChildren().add(b);
        }
    }
}
