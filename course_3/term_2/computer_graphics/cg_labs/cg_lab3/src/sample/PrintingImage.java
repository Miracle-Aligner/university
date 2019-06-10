package sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PrintingImage extends Application{
	private HeaderBitmapImage image; // приватне поле, яке зберігає об'єкт з інформацією про заголовок зображення
	private int numberOfPixels; // приватне поле для збереження кількості пікселів з чорним кольором

	public PrintingImage(){}

	public PrintingImage(HeaderBitmapImage image) // перевизначений стандартний конструктор
	{
		this.image = image;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ReadingImageFromFile.loadBitmapImage("source_folder/tr5.bmp");
		this.image = ReadingImageFromFile.pr.image;
		int width = (int)this.image.getWidth();
		int height = (int)this.image.getHeight();
		int half = (int)image.getHalfOfWidth();
		Group root = new Group();
		Scene scene = new Scene (root, width, height);
		Circle cir;
		int let = 0;
		int let1 = 0;
		int let2 = 0;
		char[][] map = new char[width][height];

		// виконуємо зчитування даних про пікселі
		BufferedInputStream reader = new BufferedInputStream (new FileInputStream("pixels.txt"));

		for(int i=0;i<height;i++)  // поки не кінець зображення по висоті
		{
			for(int j=0;j<half;j++)  // поки не кінець зображення по довжині
			{
				let = reader.read();  // зчитуємо один символ з файлу
				let1 = let;
				let2 = let;
				let1 = let1&(0xf0);  // старший байт - перший піксель
				let1 = let1>>4;  // зсув на 4 розряди
				let2 = let2&(0x0f);  // молодший байт - другий піксель
				if(j*2<width) // так як 1 символ кодує 2 пікселі нам необхідно пройти до середини ширини зображення
				{
					cir = new Circle ((j)*2,(height-1-i),1,Color.valueOf((returnPixelColor(let1)))); // за допомогою стандартного
					// примітива Коло радіусом в 1 піксель та кольором визначеним за допомогою методу returnPixelColor малюємо піксель
					root.getChildren().add(cir); //додаємо об'єкт в сцену
					if (returnPixelColor(let1) == "BLACK") // якщо колір пікселя чорний, то ставимо в масиві 1
					{
						map[j*2][height-1-i] = '1';
						numberOfPixels++; // збільшуємо кількість чорних пікселів
					}
					else
					{
						map[j*2][height-1-i] = '0';
					}
				}

				if(j*2+1<width) // для другого пікселя
				{
					cir = new Circle ((j)*2+1,(height-1-i),1,Color.valueOf((returnPixelColor(let2))));
					root.getChildren().add(cir);
					if (returnPixelColor(let2) == "BLACK")
					{
						map[j*2+1][height-1-i] = '1';
						numberOfPixels++;
					}
					else
					{
						map[j*2+1][height-1-i] = '0';
					}
				}
			}
		}
		primaryStage.setScene(scene); // ініціалізуємо сцену
		primaryStage.show(); // візуалізуємо сцену
		reader.close();

		// writing
		BufferedOutputStream writer = new BufferedOutputStream (new FileOutputStream("map.txt")); // записуємо карту для руху по траекторії в файл
		for(int i=0;i<height;i++)  // поки не кінець зображення по висоті
		{
			for(int j=0;j<width;j++)  // поки не кінець зображення по довжині
			{
				writer.write(map[j][i]);
			}
			writer.write(10);
		}
		writer.close();
		System.out.println("number of black color pixels = " + numberOfPixels);
	}

	// далі необхідно зробити рух об'єкту по заданій траеторії
	private String returnPixelColor (int color) // метод для співставлення кольорів 16-бітного зображення
	{
		String col = "BLACK";
		switch(color)
		{
			case 0: return "BLACK";
			case 1: return "LIGHTCORAL";
			case 2: return "GREEN";
			case 3: return "BROWN";
			case 4: return "BLUE";
			case 5: return "MAGENTA";
			case 6: return "CYAN";
			case 7: return "LIGHTGRAY";
			case 8: return "DARKGRAY";
			case 9: return "RED";
			case 10:return "LIGHTGREEN";
			case 11:return "YELLOW";
			case 12:return "LIGHTBLUE";
			case 13:return "LIGHTPINK";
			case 14:return "LIGHTCYAN";
			case 15:return "WHITE";
		}
		return col;
	}

	public static void main (String args[])
	{
		launch(args);
	}

	public static class Transitions extends Application {

		public static void main (String args[]) {
			launch(args);
		}

		public void start(Stage primaryStage) throws Exception {
			Group root = new Group();
			Scene scene = new Scene (root, 500, 500);

			//Створення прямокутника червоного кольору
			final Rectangle rect1 = new Rectangle(10, 10, 100, 100);
			rect1.setArcHeight(20);
			rect1.setArcWidth(20);
			rect1.setFill(Color.RED);
			root.getChildren().add(rect1);

			// створення ефекту зникнення
			FadeTransition ft = new FadeTransition(Duration.millis(3000), rect1);
			ft.setFromValue(1.0); // встановлення початкового значення прозорості об'єкту
			ft.setToValue(0.1); // встановлення кінцевого значення прозорості об'єкту
			ft.setCycleCount(Timeline.INDEFINITE);
			ft.setAutoReverse(true);
			ft.play();

			// створення синього прямокутника з круглими кутами
			final Rectangle rectPath = new Rectangle (0, 0, 40, 40);
			rectPath.setArcHeight(10);
			rectPath.setArcWidth(10);
			rectPath.setFill(Color.BLUE);
			root.getChildren().add(rectPath);

			// створення траекторії з 2 ліній типу CubicCurveTo
			Path path = new Path();
			path.getElements().add(new MoveTo(20,20)); // вказання початкової позиції, з якої починається траекторія
			path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120)); // перша крива
			path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));   // друга крива

			// створення анімації руху по траекторії
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(4000)); // встановлення часу анмації
			pathTransition.setPath(path); // прив'язування траекторії
			pathTransition.setNode(rectPath); // вибір об'єкта, який буде анімуватися
			pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT); // вказання орієнтації об'єкта при русі
			pathTransition.setCycleCount(Timeline.INDEFINITE); // циклічна анімація
			pathTransition.setAutoReverse(true); // можливість руху назад
			pathTransition.play(); // відтворення анімації

			//Створення прямокутника
			Rectangle rectParallel = new Rectangle(10,200,50, 50);
			rectParallel.setArcHeight(15);
			rectParallel.setArcWidth(15);
			rectParallel.setFill(Color.DARKBLUE);
			rectParallel.setTranslateX(50);
			rectParallel.setTranslateY(75);
			root.getChildren().add(rectParallel);

			// створення ефекту зникнення
			FadeTransition fadeTransition =
					new FadeTransition(Duration.millis(3000), rectParallel);
			fadeTransition.setFromValue(1.0f);
			fadeTransition.setToValue(0.3f);
			fadeTransition.setCycleCount(2);
			fadeTransition.setAutoReverse(true);

			// Створення ефекту переміщення
			TranslateTransition translateTransition =
					new TranslateTransition(Duration.millis(2000), rectParallel);
			translateTransition.setFromX(50);
			translateTransition.setToX(350);
			translateTransition.setCycleCount(2);
			translateTransition.setAutoReverse(true);

			// Створення повороту об'єкту
			RotateTransition rotateTransition =
					new RotateTransition(Duration.millis(3000), rectParallel);
			rotateTransition.setByAngle(180f);
			rotateTransition.setCycleCount(4);
			rotateTransition.setAutoReverse(true);

			// Масштабування об'єкту
			ScaleTransition scaleTransition =
					new ScaleTransition(Duration.millis(2000), rectParallel);
			scaleTransition.setToX(2f);
			scaleTransition.setToY(2f);
			scaleTransition.setCycleCount(2);
			scaleTransition.setAutoReverse(true);

			// Створення можливості паралельно виконувати анімацію
			ParallelTransition parallelTransition =
					new ParallelTransition();
			parallelTransition.getChildren().addAll(
					fadeTransition,
					translateTransition,
					rotateTransition,
					scaleTransition
			);

			parallelTransition.setCycleCount(Timeline.INDEFINITE);
			parallelTransition.play();

			Rectangle rectSeq = new Rectangle(25,25,50,50);
			rectSeq.setArcHeight(15);
			rectSeq.setArcWidth(15);
			rectSeq.setFill(Color.CRIMSON);
			rectSeq.setTranslateX(50);
			rectSeq.setTranslateY(50);
			root.getChildren().add(rectSeq);

			fadeTransition =
					new FadeTransition(Duration.millis(1000), rectSeq);
			fadeTransition.setFromValue(1.0f);
			fadeTransition.setToValue(0.3f);
			fadeTransition.setCycleCount(1);
			fadeTransition.setAutoReverse(true);

			translateTransition =
					new TranslateTransition(Duration.millis(2000), rectSeq);
			translateTransition.setFromX(50);
			translateTransition.setFromY(40);
			translateTransition.setToX(375);
			translateTransition.setToY(375);
			translateTransition.setCycleCount(1);
			translateTransition.setAutoReverse(true);

			rotateTransition =
					new RotateTransition(Duration.millis(2000), rectSeq);
			rotateTransition.setByAngle(180f);
			rotateTransition.setCycleCount(4);
			rotateTransition.setAutoReverse(true);

			/*
			scaleTransition =
				 new ScaleTransition(Duration.millis(2000), rectSeq);
			scaleTransition.setFromX(1);
			scaleTransition.setFromY(1);
			scaleTransition.setToX(2);
			scaleTransition.setToY(2);
			scaleTransition.setCycleCount(1);
			scaleTransition.setAutoReverse(true);
			*/

			SequentialTransition sequentialTransition = new SequentialTransition();
			sequentialTransition.getChildren().addAll(
					fadeTransition,
					translateTransition
			);

			sequentialTransition.setCycleCount(Timeline.INDEFINITE);
			sequentialTransition.setAutoReverse(true);
			sequentialTransition.play();

			final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
			rectBasicTimeline.setFill(Color.RED);
			root.getChildren().add(rectBasicTimeline);

			final Timeline timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.setAutoReverse(true);
			final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300);
			final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
			timeline.getKeyFrames().add(kf);
			timeline.play();

			final Rectangle rectBasicTimeline1 = new Rectangle(200, 100, 200, 100);
			rectBasicTimeline1.setFill(Color.BROWN);
			root.getChildren().add(rectBasicTimeline1);
			final Timeline timeline1 = new Timeline();
			timeline1.setCycleCount(Timeline.INDEFINITE);
			timeline1.setAutoReverse(true);
			final KeyValue kv1 = new KeyValue(rectBasicTimeline1.xProperty(), 300,
					Interpolator.EASE_BOTH);
			final KeyFrame kf1 = new KeyFrame(Duration.millis(500), kv1);
			timeline1.getKeyFrames().add(kf1);
			timeline1.play();

			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}
}
