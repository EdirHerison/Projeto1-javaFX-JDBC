package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primarySatge) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollpane = loader.load();
			
			scrollpane.setFitToHeight(true);
			scrollpane.setFitToWidth(true);
			
			Scene mainScene = new Scene(scrollpane);
			primarySatge.setScene(mainScene);
			primarySatge.setTitle("Primeiro Projeto com interface grafica e banco");
			primarySatge.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}