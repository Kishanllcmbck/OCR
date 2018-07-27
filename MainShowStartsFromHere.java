package main;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainShowStartsFromHere extends Application {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("PUMA - KiT");
		
		//primaryStage.getIcons().add(new Image(new FileInputStream(new File("src/images/KishanLogo.png").getAbsolutePath())));
		primaryStage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
		new windows.LoadSingleImage().start(primaryStage);
	}

}
