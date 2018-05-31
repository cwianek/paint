package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends Application {

    private static List<Plugin> plugins = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = (Parent) loader.load();
        Controller controller = (Controller) loader.getController();
        controller.setStage(primaryStage);
        controller.setPlugins(plugins);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 850, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {


        File pluginsDir[] = new File("resources/plugins").listFiles();
        for (File file : pluginsDir) {

            String pathToJar = "resources/plugins/"+file.getName();
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');

                Class c = Class.forName(className, true, cl);
                if (!className.equals("Plugin")) {
                    Object o = c.newInstance();
                    Method method = o.getClass().getMethod("transform", Canvas.class);
                    Plugin plugin = new Plugin(className, method, o);
                    plugins.add(plugin);
                }

            }
        }
        launch(args);
    }
}
