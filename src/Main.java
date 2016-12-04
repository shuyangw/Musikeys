import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

//TODO Possible to make global keylistener?
//TODO Make UI look good
//TODO CLEAN EVERYTHING
//TODO UPDATE KEBOARD TO DEAL WITH NUMERALS

public class Main extends Application implements NativeKeyListener{
	
	String clickedKey;
	BorderPane mPane;
	GridPane grid;
	Stage stage;
	Button currKey;
	Label select;
	static Keyboard keyboard;
	static MediaPlayer med;
	static boolean Override = true;
	static Stack<MediaPlayer> overlaps = new Stack<MediaPlayer>();
	static LinkedList<Button> selected = new LinkedList<Button>();
	static ArrayList<Button> arrB;
	
	public static void main(String[] args){
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) { //Do nothing
		}
		GlobalScreen.addNativeKeyListener(new Main());
		launch(args);
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
        clickedKey = NativeKeyEvent.getKeyText(e.getKeyCode());
        
		//System.out.println(event.getCode().getName());
		String Char = clickedKey;
		switch(Char){
		case "Open Bracket": Char = "["; break;
		case "Close Bracket": Char = "]"; break;
		case "Semicolon": Char = ";"; break;
		case "Quote": Char = "'"; break;
		case "Back Slash": Char = "\\"; break;
		case "Comma": Char = ","; break;
		case "Period": Char = "."; break;
		case "Slash": Char = "/"; break;
		}
		
		
		if(!keyboard.GetStop(Char)){
			String filename = keyboard.GetSound(Char);
			if(!filename.equals("") && filename != null){
				if(Override)
					OverridePlay(filename);
				else
					OverlapPlay(filename);
			}
		}
		else{
			if(Override) med.stop();
			else {
				while(!overlaps.isEmpty())
					overlaps.pop().stop();
			}
		}
        // TODO Auto-generated method stub 
        		
	}
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		clickedKey = NativeKeyEvent.getKeyText(e.getKeyCode());

		
	}
	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		// TODO Auto-generated method stub
		clickedKey = NativeKeyEvent.getKeyText(e.getKeyCode());
	}
	
	public void setup(){
		arrB = new ArrayList<Button>();
		String[] FileNameStrings = {"Q", "W","E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\",
                "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'",
                "Z", "X", "C", "V", "B", "N", "M", "," ,".", "/", 
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
        };
		
		for(int i = 0; i < 44; i++){
			String s = FileNameStrings[i];
			Button currBt = new Button(s);
			arrB.add(currBt);

			currBt.setPrefSize(50, 50);
			currBt.setOnAction(e -> {
				currKey = currBt;
				currKey.setText(s);
				
				if(selected.contains(currBt)){
					selected.remove(currBt);
					currBt.setStyle("");
					if(keyboard.GetStop(s)==true){
						currBt.setStyle("-fx-background-color: steelblue");}
					else if (!keyboard.GetSound(s).equals(""))
						currBt.setStyle("-fx-background-color: burlywood");
				}
				else{
					selected.addLast(currBt);
					currKey.setStyle("-fx-background-color: gray");
					
				}
				File f = new File(keyboard.GetSound(currKey.getText()));
				select.setText("Selected Key:  " + s + "    File: " + f.getName());
				
				if(keyboard.GetStop(currKey.getText()) == true){
					select.setText("Selected Key:  " + s + "    File: " + "Stop");
				}
			});
			if(i <= 12) grid.add(currBt, i, 1);
            if(i >= 13 && i <= 23) grid.add(currBt, i - 12, 2);
            if(i >= 24 && i <= 33) grid.add(currBt, i - 23, 3);
            if(i >= 34 && i <= 43) grid.add(currBt, i - 33, 0);
		}
		
		this.keyboard = new Keyboard();
	}
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		mPane = new BorderPane();
		grid = new GridPane();
		currKey = new Button();
		select = new Label("Please select a key");
		select.setFont(new Font("Segeo UI", 40));
		
		Scene scene = new Scene(mPane,1024,600);
		stage.setScene(scene);
		stage.setTitle("MusiKeys");
		VBox mTop = new VBox();
		HBox mBottom = new HBox(50);
		Button bind = new Button("Bind");
		mBottom.setAlignment(Pos.BOTTOM_CENTER);
		mBottom.setTranslateY(-50);
		Button bindstop = new Button("Bind to Stop");
		Button bindAll = new Button("Bind All");

	    setup();
		bind.setOnAction(e -> {
			if(!currKey.getText().equals("")){
				FileChooser f = new FileChooser();
				f.setTitle("Choose a file");
				File sound = f.showOpenDialog(stage);
				String extension = "";
				if(sound!=null){
				int i = sound.getName().lastIndexOf('.');
				if (i > 0) {
				    extension = sound.getName().substring(i+1);
				}
				
				try{
					if(!extension.equals("mp3")) throw new NullPointerException();//throw new Exception("ya");
					if(sound != null && !sound.getName().equals("")){
						Iterator itr = selected.iterator();
						while(itr.hasNext()){
							String Char = ((Button)itr.next()).getText();
							keyboard.ApplyStop(Char, false);
							keyboard.ApplySound(Char,sound.getAbsolutePath());
							File file = new File(keyboard.GetSound(currKey.getText()));
							select.setText("Selected Key:  " + currKey.getText() + "    File: " + file.getName());
						}
						//DeselectAll();
					}
				}
				catch(Exception d){
					Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setTitle("Error!");
		            alert.setHeaderText("You've imported the wrong file!");
		            alert.setContentText("Please make this an MP3 File!");
		            alert.show();
				}
			}
			}
			
			else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText(null);
				alert.setContentText("No file chosen!");
				alert.show();
			}
		});
		bindstop.setOnAction(e -> { //Binds stop function to a key
			if(!currKey.getText().equals("")){
				currKey.setStyle("-fx-background-color: gray");
				Iterator itr = selected.iterator();
				while(itr.hasNext()){
				keyboard.ApplyStop(((Button)itr.next()).getText(),true);
				select.setText("Selected Key:  " + currKey.getText() + "    File: " + "Stop");
				//DeselectAll();
				}
			}
				
			else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText(null);
				alert.setContentText("No file chosen!");
				alert.show();
			}
		});
		bindAll.setOnAction(e -> { //Clears key and selection
			if(!currKey.getText().equals("")){
				FileChooser f = new FileChooser();
				f.setTitle("Choose a file");
				File sound = f.showOpenDialog(stage);
				String extension = "";
				if(sound!=null){
				int i = sound.getName().lastIndexOf('.');
				if (i > 0) {
				    extension = sound.getName().substring(i+1);
				}
				try{
					if(!extension.equals("mp3")) throw new Exception();//throw new Exception("ya");
					if(sound != null && !sound.getName().equals("")){
						for(Button x: arrB){
							x.setStyle("-fx-background-color: aquamarine");
							keyboard.ApplySound(x.getText(),sound.getAbsolutePath());
							keyboard.ApplyStop(x.getText(), false);
							File file = new File(keyboard.GetSound(x.getText()));
							select.setText("Selected Key:  " + x.getText() + "    File: " + file.getName());
						}
					}
				}	
				catch(Exception d){
					Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setTitle(null);
		            alert.setHeaderText(null);
		            alert.setContentText("Please upload a wav or mp3 file.");
		            alert.show();
				}
			}
			else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText(null);
				alert.setContentText("No file chosen!");
				alert.show();
			}
		}
		});
		
		Button clear = new Button("Clear");
		clear.setOnAction(e->{
			if(!currKey.getText().equals("")){
				Iterator itr = selected.iterator();
				while(itr.hasNext()){
					String Char = ((Button)itr.next()).getText();
					keyboard.ClearSound(Char);
					keyboard.ApplyStop(Char, false);
					currKey.setStyle("");
					currKey = new Button();
					select.setText("Select: ");
				}
				//DeselectAll();
			}
			else{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText(null);
				alert.setContentText("Nothing to clear!");
				alert.show();
			}
		});
		mBottom.getChildren().addAll(bind,bindstop,bindAll,clear);
		mTop.getChildren().addAll(menuBar(), select);
		mTop.setAlignment(Pos.TOP_CENTER);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
	    mPane.setCenter(grid);
		mPane.setTop(mTop);
		mPane.setBottom(mBottom);
		
		//Quit Program
		stage.getIcons().add(new Image("KeyboardIcon.png"));
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.show();
	}
	
	public MenuBar menuBar(){
		Menu file = new Menu("File");
		Menu options = new Menu("Options");
		Menu help = new Menu("Help");
		MenuItem newProf = new MenuItem("New");
		MenuItem override = new MenuItem("Override/Overlap toggle");
		
		override.setOnAction(e->{ //Toggle between playback options
			if(Override){
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Override or Overlap?");
				alert.setHeaderText(null);
				alert.setContentText("Switch from overriding to overlapping sounds?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK){
					Override = false;
				}
			}
			else{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Override or Overlap?");
				alert.setHeaderText(null);
				alert.setContentText("Switch from overlapping to overriding sounds?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK){
					Override = true;
				}
			}
		});
		
		newProf.setOnAction(e-> { //Make a new profile
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("New profile?");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure? Any unsaved data will be lost");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK){
				keyboard = new Keyboard();
				select.setText("Select: ");
				refreshBindings();
				currKey = new Button();
			}
		});
		
		MenuItem loadProf = new MenuItem("Load");
		loadProf.setOnAction(e -> { //Load a profile
			FileChooser loadProfile = new FileChooser();
			loadProfile.setTitle("Choose a profile");
			File loadFile = loadProfile.showOpenDialog(stage);
			if(loadFile != null){
				keyboard.loadStatus(loadFile.getAbsolutePath());
				refreshBindings();
			}
		});
		
		MenuItem saveProf = new MenuItem("Save");
		saveProf.setOnAction(e -> { //Save a profile
			//TODO Come up with an extension to use CHECK :3
			FileChooser saveProfile = new FileChooser();
			FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("MK file(*.mk)", "*.mk");	
			saveProfile.getExtensionFilters().add(ext);
			saveProfile.setTitle("Save a profile");
			File saveFile = saveProfile.showSaveDialog(stage);
			
			//SaveFile will be null if the save dialogue is cancelled
			if(saveFile != null){
				keyboard.saveStatus(saveFile.getAbsolutePath());
			}
			
		}); 
		MenuItem helpProf = new MenuItem("Help");
        helpProf.setOnAction(e ->{ //Help box
            Stage dialog = new Stage();
            dialog.setTitle("Help");
            dialog.initModality(Modality.APPLICATION_MODAL); //IDK WHAT THIS MEANS
            dialog.initOwner(stage);
            Text helpText = new Text("To bind a sound to keys: \n  Select keys and then click on the bind button to browse for a sound to bind the sound to those keys"
                    + "\n\n To clear the binding for keys:  \n Select keys labeled by its tan or blue color and click on the clear button"
                    + "\n\n To save your current layout:  \n Click on file and then click on save to save your layout to a directory"
                    + "\n\n To load a previous layout:  \n Click on file and then click on load a .mk file from the directory you saved your layouts to"
                    + "\n\n To toggle whether or not you would like sounds to overlap:  \n  Click on the options to toggle whether or not you would like sounds to overlap with each other"
                    + "\n\n To bind keys to stop sounds:  \n Select keys and then click on the bind to stop button");
            helpText.setWrappingWidth(400);
            HBox textBox = new HBox(100);
            helpText.setTextAlignment(TextAlignment.CENTER);
             
            textBox.getChildren().addAll(helpText);
            textBox.setAlignment(Pos.TOP_CENTER);
            Scene dialogScene = new Scene(textBox, 500, 500);
            dialog.setScene(dialogScene);
            dialog.show();
        });
		options.getItems().addAll(override);
		file.getItems().addAll(newProf,loadProf,saveProf);
		help.getItems().addAll(helpProf);
		
		return new MenuBar(file, options,help);
	}
	public void refreshBindings(){ //Refresh Bindings, mainly upon loading a profile
		Iterator<Node> iter = grid.getChildren().iterator();
		while(iter.hasNext()){
			Button button = (Button)iter.next();
			//System.out.println(button.getText());
			if(button.getText()==null) button.setStyle("");
			else if(!keyboard.GetSound(button.getText()).equals("") || keyboard.GetStop(button.getText())){
				button.setStyle(GetColor(button.getText()));
			}
			else{
				//TODO Set the button color to default CHECK
				button.setStyle("");
			}
		}
	}
	
	//Playback functions (mp3)
	public void OverridePlay(String soundFile){ 
		if(soundFile != null && !soundFile.equals("")){
			if(med != null) med.stop();
			Media media = new Media(new File(soundFile).toURI().toString());
			med = new MediaPlayer(media);
			med.play();
		}
    }
	public void OverlapPlay(String soundFile){
		if(soundFile != null && !soundFile.equals("")){
			Media media = new Media(new File(soundFile).toURI().toString());
			med = new MediaPlayer(media);
			overlaps.push(med);
			med.play();
		}
    }
//	public void DeselectAll(){
//		LinkedList<Button> temp = selected;
//		Iterator itr = temp.iterator();
//		while(itr.hasNext()){
//		Object b = itr.next();
//		selected.remove((Button)b);
//		((Button)b).setStyle(((Button)b).getText());
//		}
//	}
	
	
	//Returns the correct color of a key
	public String GetColor(String Char){
		if(keyboard.GetStop(Char)==true){
			return "-fx-background-color: steelblue";
		}
		else if(!keyboard.GetSound(Char).equals("")){
			return "-fx-background-color: burlywood";
		}
		else return "";
	}

}
