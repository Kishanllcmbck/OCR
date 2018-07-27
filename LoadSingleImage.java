package windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.Alert.AlertType;
 
public class LoadSingleImage extends Application {
 
 
    ImageView imageView;
    StackPane contentPane;
    BorderPane layout;
    ScrollPane s1 = new ScrollPane();
    File sourceFilePath;
    File lastOpenedFileFolder;
    File lastSavedFileFolder;
    File selectedDirectoryBatchFileProcess;
    
    public LoadSingleImage(File lastOpenedFileFolder, File lastSavedFileFolder) {
    	this.lastOpenedFileFolder = lastOpenedFileFolder;
    	this.lastSavedFileFolder = lastSavedFileFolder;
    }
    
    public LoadSingleImage() {
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public void start(Stage primaryStage) {
        
        layout = new BorderPane();
        Text t = new Text("Pictures for Line Freeze");
        t.fontProperty().set(Font.font("Impact", FontWeight.BOLD, 20));
        t.setFill(Color.DARKCYAN);
        contentPane = new StackPane(t);
        
        Scene scene = new Scene(layout, 800, 500, Color.BLACK);
 
        contentPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
            	mouseDragOver( event);
            }
        }); 
 
        contentPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                mouseDragDropped(event);
            }
        });
 
         contentPane.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(final DragEvent event) {
                contentPane.setStyle("-fx-border-color: #C6C6C6;");
            }
        });
 
       layout.setCenter(contentPane);
       
       Button buttonLoad = new Button("Load Image");
       buttonLoad.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
               FileChooser fileChooser = new FileChooser();
               FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.jpeg", "*.png");
               if(lastOpenedFileFolder != null)
            	   fileChooser.setInitialDirectory(lastOpenedFileFolder);
               fileChooser.getExtensionFilters().add(extFilter);
              
               File file = fileChooser.showOpenDialog(primaryStage);
               if (file != null) {
				Image img = null;
				try {
					img = new Image(new FileInputStream(file.getAbsolutePath()), 400, 400, false, false);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addImage(img, contentPane);
				sourceFilePath = file;
				lastOpenedFileFolder = file.getParentFile();
				System.out.println(sourceFilePath);
			}
           }
       });
       
       Button buttonSave = new Button("Save Image");
       buttonSave.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
            	FileChooser fileChooser1 = new FileChooser();
            	if(lastSavedFileFolder != null)
            	fileChooser1.setInitialDirectory(lastSavedFileFolder);
            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.jpeg", "*.png");
            	fileChooser1.getExtensionFilters().add(extFilter);
            	fileChooser1.setTitle("Save Image");
            	
            	File destinationFilePath = fileChooser1.showSaveDialog(primaryStage);

            	if (destinationFilePath != null) {
            	    try {
                    	lastSavedFileFolder = destinationFilePath.getParentFile();
            	        Files.copy(sourceFilePath.toPath(), destinationFilePath.toPath());
            	    } catch (IOException ex) {
            	        // handle exception...
            	    }
            	}
           }
       });
       
       
       
       Button batchProcess = new Button("Batch process");
       batchProcess.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Batch Processing");
            	alert.setHeaderText(null);
            	alert.setContentText("Switch to Batch Processing?");
            	// Get the Stage.
            	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
            	Optional<ButtonType> result = alert.showAndWait();
            	if (result.get() == ButtonType.OK){
            		alert.setAlertType(AlertType.INFORMATION);
            		alert.setTitle("Source Directory");
                	alert.setHeaderText(null);
                	alert.setContentText("Please choose the source directory");
                	alert.showAndWait();
                	DirectoryChooser chooser = new DirectoryChooser();
                	chooser.setTitle("SOURCE DIRECTORY");
                	
                	selectedDirectoryBatchFileProcess = chooser.showDialog(primaryStage);
                	try {
						new windows.BatchProcessorWindow(selectedDirectoryBatchFileProcess).start(primaryStage);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	} else {
            	    // ... user chose CANCEL or closed the dialog
            	}
            	
           }
       });
       
       Button openWebCam = new Button("Open Live Cam");
       openWebCam.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Live Camera");
            	alert.setHeaderText(null);
            	alert.setContentText("Switch to Live Camera?");
            	// Get the Stage.
            	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
            	Optional<ButtonType> result = alert.showAndWait();
            	if (result.get() == ButtonType.OK){
            		alert.setAlertType(AlertType.INFORMATION);
            		alert.setTitle("Target Directory");
                	alert.setHeaderText(null);
                	alert.setContentText("Please choose the target directory");
                	alert.showAndWait();
                	DirectoryChooser chooser = new DirectoryChooser();
                	chooser.setTitle("Target Directory");
                	
                	File TargetDir = chooser.showDialog(primaryStage);
                	
                	try {

                    	new Thread(new webCam.WebCamViewer(TargetDir.getPath())).start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	} else {
            	    // ... user chose CANCEL or closed the dialog
            	}
            	
           }
       });
       
       HBox hBox = new HBox();
       hBox.getChildren().add(buttonLoad);
       
       hBox.getChildren().add(buttonSave);
       hBox.getChildren().add(batchProcess);
       hBox.getChildren().add(openWebCam);
       hBox.setPadding(new Insets(10));
       //layout.setRight(vBox);
       layout.setBottom(hBox);
        primaryStage.setScene(scene);
        System.out.println(sourceFilePath);
        primaryStage.show();
 
    }
 
    void addImage(Image i, StackPane pane){
 
        imageView = new ImageView();
        imageView.setImage(i);
        
        /*s1.setPannable(true);
        s1.setPrefSize(400, 400);
        s1.setContent(imageView);*/
        
        pane.getChildren().add(imageView);
        //pane.getChildren().add(s1);
    }
 
  private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            
            final File file = db.getFiles().get(0);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println(file.getAbsolutePath());
                    try {
                        if(!contentPane.getChildren().isEmpty()){
                            contentPane.getChildren().remove(0);
                        }
                        Image img = new Image(new FileInputStream(file.getAbsolutePath()),400, 400, false, false);  
                        sourceFilePath = file;
                        addImage(img, contentPane);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(LoadSingleImage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }
 
    private  void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();
 
        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");
 
        if (db.hasFiles()) {
            if (isAccepted) {
                contentPane.setStyle("-fx-border-color: red;"
              + "-fx-border-width: 5;"
              + "-fx-background-color: #C6C6C6;"
              + "-fx-border-style: solid;");
                e.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            e.consume();
        }
    }
 
}