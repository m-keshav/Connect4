package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Controller  implements Initializable{
	private static final int columns=7;
	private static final int rows=6;
	private static final int circle_diameter=80;
	private static final String disccolor1="#24303E";
	private static final String disccolor2="#4caabb";
	
	
	private static String player1="player 1";
	private static String player2="player 2";
	
	private boolean isplayeroneturn=true;
	private boolean fla=true;
	
	private Disc[][] insertedDiscArray=new Disc[rows][columns];
	
	@FXML
	
	public GridPane rootgridpane;
	public Pane inserteddiscpane;
	public Label playernamelabel;
	public TextField playeronetextfield,playertwotextfield;
	public Button setnamesbutton;
	
	public void createplayround() {
		Shape rectanglewithhole=createstructure();
		rootgridpane.add(rectanglewithhole, 0, 1);
		
		setnamesbutton.setOnAction(event->{
			player1=playeronetextfield.getText();
			player2=playertwotextfield.getText();
			});
		
		List<Rectangle> rectlist=clickcolumn();
		for(Rectangle rect:rectlist) {
		rootgridpane.add(rect, 0, 1);}
		
	}
	private Shape createstructure() {
		Shape rectanglewithhole =new Rectangle((columns+1)*circle_diameter,(rows+1)*circle_diameter);
		
		for(int row=0;row<rows;row++) {
			for(int column=0;column<columns;column++) {
		Circle circle=new Circle();
		circle.setRadius(circle_diameter/2);
		circle.setCenterX(circle_diameter/2);
		circle.setCenterY(circle_diameter/2);
		circle.setSmooth(true);
		
		circle.setTranslateX(column*(circle_diameter+5)+circle_diameter/4);
		circle.setTranslateY(row*(circle_diameter+5)+circle_diameter/4);
		
		rectanglewithhole=Shape.subtract(rectanglewithhole, circle);
			}
		}
		rectanglewithhole.setFill(Color.WHITE);
		
		return rectanglewithhole;
	}
	private List<Rectangle> clickcolumn() {
		List<Rectangle>rectlist =new ArrayList<>();
		for(int col=0;col<columns;col++) {
		Rectangle rect=new Rectangle(circle_diameter,(rows+1)*circle_diameter);
		rect.setFill(Color.TRANSPARENT);
		rect.setTranslateX(col*(circle_diameter+5)+circle_diameter/4);
		rect.setOnMouseEntered(event->rect.setFill(Color.valueOf("#eeeeee26")));
		rect.setOnMouseExited(event->rect.setFill(Color.TRANSPARENT));
		final int column=col;
		rect.setOnMouseClicked(event->{
			if(fla) {
				fla=false;
			insertdisc(new Disc(isplayeroneturn),column);}
		});
		rectlist.add(rect);
		
		}
		return rectlist;
	}
	
	private  void insertdisc(Disc disc,int column) {
		int row=rows-1;
		while(row >=0) {
			if(discpresent(row,column)==null)
				break;
			row--;
		}
		if(row<0)
			return;
		
		insertedDiscArray[row][column]=disc;
		inserteddiscpane.getChildren().add(disc);
		
		disc.setTranslateX(column*(circle_diameter+5)+circle_diameter/4);
		TranslateTransition translatetransition=new TranslateTransition(Duration.seconds(0.5),disc);
		int currow=row;
		translatetransition.setToY(row*(circle_diameter+5)+circle_diameter/4);
		translatetransition.setOnFinished(eveny->{
			fla=true;
			
			if(ended(currow,column)) {
				over();
				return;
				
			}
			isplayeroneturn=!isplayeroneturn;
			playernamelabel.setText(isplayeroneturn?player1:player2);
		});
		translatetransition.play();
	}
	private void over() {
		String winner=isplayeroneturn?player1:player2;
		System.out.println("winner is "+winner);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect 4");
		alert.setHeaderText("Winner is "+winner);
		alert.setContentText("Want to play ?");
		
		ButtonType yesbtn=new ButtonType("Yes");
		ButtonType nobtn=new ButtonType("Exit");
		alert.getButtonTypes().setAll(yesbtn,nobtn);

		Platform.runLater(()->{
			Optional<ButtonType>btnclicked=alert.showAndWait();
			if(btnclicked.isPresent()&&btnclicked.get()==yesbtn) {
				reset();
			}else {
				Platform.exit();
				System.exit(0);
				
			}
			
			
		});
	
	}
	public void reset() {
		
		inserteddiscpane.getChildren().clear();
		for(int row=0;row<insertedDiscArray.length;row++) {
			for(int col=0;col<insertedDiscArray[row].length;col++) {
				insertedDiscArray[row][col]=null;
			}
		}
		
		isplayeroneturn=true;
		playernamelabel.setText(player1);
		createplayround();
		// TODO Auto-generated method stub
		
	}
	private boolean ended(int row, int column) {
		List<Point2D> verticalpoints=IntStream.rangeClosed(row-3,row+3).mapToObj(r->new Point2D(r,column)).collect(Collectors.toList());
		List<Point2D> orizontalpoints=IntStream.rangeClosed(column-3,column+3).mapToObj(col->new Point2D(row,col)).collect(Collectors.toList());
		
		Point2D startPoint1=new Point2D(row-3,column+3);
		List<Point2D> diaonal1points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());

		Point2D startPoint2=new Point2D(row-3,column-3);
		List<Point2D> diaonal2points=IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());

		
		boolean isended=checkCombination(verticalpoints)||checkCombination(orizontalpoints)||checkCombination(diaonal1points)||checkCombination(diaonal2points);
		
		
		return isended;
	}
	private boolean checkCombination(List<Point2D> points) {
		int cain=0;
		
		for(Point2D point:points) {
			
			int rowIndexForArray=(int)point.getX();
			int colIndexForArray=(int)point.getY();
			Disc disc=discpresent(rowIndexForArray,colIndexForArray);
			if(disc!=null && disc.isplayeronemove==isplayeroneturn) {
				cain++;
				if(cain==4) {
					return true;
				}
			}else {
				cain=0;
				
			}
		}
		return false;
		
	}
	private Disc discpresent(int row,int col) {
		if(row>=rows||row<0||col>=columns||col<0)
			return null;
		return insertedDiscArray[row][col];
	}
	private static class Disc extends Circle{
		private final boolean isplayeronemove;
		public Disc(boolean isplayeronemove) {
			this.isplayeronemove=isplayeronemove;
			setRadius(circle_diameter/2);
			setFill(isplayeronemove?Color.valueOf(disccolor1):Color.valueOf(disccolor2));
			setCenterX(circle_diameter/2);
			setCenterY(circle_diameter/2);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method 
	}

}
