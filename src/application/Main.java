package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


public class Main extends Application{
	private Controller controller;
	
	@Override
	public void start(Stage primaryStage)  throws Exception {
		FXMLLoader loader=new FXMLLoader(getClass().getResource("Game.fxml"));
		GridPane rootgridpane=loader.load();
		controller=loader.getController();
		controller.createplayround();
		
		
		MenuBar menubar=createMenu();
		menubar.prefWidthProperty().bind(primaryStage.widthProperty());
		Pane menuPane=(Pane)rootgridpane.getChildren().get(0);
		menuPane.getChildren().add(menubar);
		Scene scene=new Scene(rootgridpane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("connect 4");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}
	
	private MenuBar createMenu(){
		Menu filemenu=new Menu("File");
		
		MenuItem newgame=new MenuItem("New game");
		newgame.setOnAction(event->resetgame());

		
		
		MenuItem resetgame=new MenuItem("Reset game");
		resetgame.setOnAction(event->controller.reset());
		
		SeparatorMenuItem sep=new SeparatorMenuItem();
		MenuItem exitgame=new MenuItem("Exit game");
		exitgame.setOnAction(event->controller.reset());
		
		
		filemenu.getItems().addAll(newgame,resetgame,sep,exitgame);
		
		Menu helpmenu =new Menu("Help");
		MenuItem about =new MenuItem("About connect 4");
		
		about.setOnAction(event->aboutgame());
		
		
		SeparatorMenuItem sepe=new SeparatorMenuItem();
		MenuItem aboutme=new MenuItem("About Me");
		aboutme.setOnAction(event->aboutme());
		
		helpmenu.getItems().addAll(about,sepe,aboutme);
		

		MenuBar menubar=new MenuBar();
		menubar.getMenus().addAll(filemenu,helpmenu);
		return menubar;
	}
		private void aboutme() {
			
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("About connect 4");
			alert.setHeaderText("Meenal");
			alert.setContentText("me");
			alert.show();
		// TODO Auto-generated method stub
		
	}

		private void aboutgame() {
			
			Alert alert=new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("About connect 4");
			alert.setHeaderText("How to play?");
			alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
			alert.show();
		// TODO Auto-generated method stub
		
	}

		private void exitgame() {
			Platform.exit();
			System.exit(0);
		// TODO Auto-generated method stub
		
	}

		private Object resetgame() {
		// TODO Auto-generated method stub
		return null;
	}

		/*
		private void aboutapp() {
			
			
			ButtonType yes=new ButtonType("yes");
			ButtonType np=new ButtonType("no");
			alertdialog.getButtonTypes().setAll(yes,np);
			alertdialog.show();
		
	}*/
	public static void main(String[] args) {
		launch(args);
	}
}
