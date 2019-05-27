import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("cg_lab1");
        Group mygroup = new Group();
        Scene scene = new Scene (mygroup, 431, 259);

        Polygon body = new Polygon(111, 117, 221, 30, 389, 105, 282, 151, 318, 224, 167, 235);
        mygroup.getChildren().add(body);
        body.setFill(Color.rgb(0, 255, 0));

        Polygon tail = new Polygon(305, 151, 372, 135, 330, 205);
        mygroup.getChildren().add(tail);
        tail.setFill(Color.YELLOW);

        Line centerLine = new Line(111, 117, 282, 151);
        mygroup.getChildren().add(centerLine);
        centerLine.setStroke(Color.BLACK);
        centerLine.setStrokeWidth(3);

        Line topLine = new Line(70, 35, 139, 97);
        mygroup.getChildren().add(topLine);
        topLine.setStroke(Color.BLACK);
        topLine.setStrokeWidth(6);
        topLine.setStrokeLineCap(StrokeLineCap.ROUND);

        Line bottomLine = new Line(59, 205,  140, 176);
        mygroup.getChildren().add(bottomLine);
        bottomLine.setStroke(Color.BLACK);
        bottomLine.setStrokeWidth(6);
        bottomLine.setStrokeLineCap(StrokeLineCap.ROUND);

        Rectangle eyeTop = new Rectangle(188, 97, 10, 10);
        mygroup.getChildren().add(eyeTop);
        eyeTop.setFill(Color.GREEN);

        Rectangle eyeBottom = new Rectangle(170, 160, 10, 10);
        mygroup.getChildren().add(eyeBottom);
        eyeBottom.setFill(Color.GREEN);


        scene.setFill(Color.rgb(0, 128, 128));

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}