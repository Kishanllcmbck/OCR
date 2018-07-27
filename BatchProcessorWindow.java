package windows;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
 
public class BatchProcessorWindow extends Application {
 
	List<File> completedFiles = new ArrayList<>();
	
    ImageView imageView;
    StackPane contentPane;
    BorderPane layout;
    ScrollPane s1 = new ScrollPane();
    public File getLastOpenedFileFolder() {
		return lastOpenedFileFolder;
	}

	public void setLastOpenedFileFolder(File lastOpenedFileFolder) {
		this.lastOpenedFileFolder = lastOpenedFileFolder;
	}

	public File getLastSavedFileFolder() {
		return lastSavedFileFolder;
	}

	public void setLastSavedFileFolder(File lastSavedFileFolder) {
		this.lastSavedFileFolder = lastSavedFileFolder;
	}

	public File getSelectedDirectoryBatchFileProcess() {
		return selectedDirectoryBatchFileProcess;
	}

	public void setSelectedDirectoryBatchFileProcess(File selectedDirectoryBatchFileProcess) {
		this.selectedDirectoryBatchFileProcess = selectedDirectoryBatchFileProcess;
	}
	boolean start = true;
	File sourceFilePath;
    File lastOpenedFileFolder;
    File lastSavedFileFolder;
    File selectedDirectoryBatchFileProcess;
    boolean imageSet = false;
    
    public void startNewWindow() {
        launch();
    }
 
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        
        layout = new BorderPane();
        Text t = new Text("Pictures for Line Freeze");
        t.fontProperty().set(Font.font("Impact", FontWeight.BOLD, 20));
        t.setFill(Color.DARKCYAN);
        contentPane = new StackPane(t);
        Scene scene = new Scene(layout, 800, 500, Color.BLACK);
        
        if(this.lastSavedFileFolder == null) {
        	
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Destination Directory");
        	alert.setHeaderText(null);
        	alert.setContentText("Please choose the destination directory");
        	// Get the Stage.
        	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
        	alert.showAndWait();
        	 DirectoryChooser chooser = new DirectoryChooser();
         	chooser.setTitle("Destination Directory");
         
         	this.lastSavedFileFolder = chooser.showDialog(primaryStage);
        }
     
       layout.setCenter(contentPane);
     
       Button batchProcess = new Button("Source Directory");
       batchProcess.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
            	
            	DirectoryChooser chooser = new DirectoryChooser();
            	chooser.setTitle("SOURCE DIRECTORY");
        
            	lastOpenedFileFolder = chooser.showDialog(primaryStage);
            	try {
					getNextImage();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            		
           }
       });
       
       Button changeDestDir = new Button("Destination Directory");
       changeDestDir.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
                    	
            	Alert alert = new Alert(AlertType.WARNING);
            	
            	alert.setTitle("Destination Directory");
            	alert.setHeaderText(null);
            	alert.setContentText("Change destination directory?");
            	// Get the Stage.
            	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
            	alert.showAndWait();
            	
            	DirectoryChooser chooser = new DirectoryChooser();
            	chooser.setTitle("Destination Directory");
      
            	lastSavedFileFolder = chooser.showDialog(primaryStage);
            	
            	alert.setAlertType(AlertType.INFORMATION);
            	alert.setTitle("Destination Directory");
            	alert.setHeaderText(null);
            	alert.setContentText("Destination directory changed to "+ lastSavedFileFolder.getName());
            	
            	alert.showAndWait();
                        		
           }
       });
       
       Button loadSingleImage = new Button("Load Single Image");
       loadSingleImage.setOnAction(new EventHandler<ActionEvent>(){
            @Override
           public void handle(ActionEvent arg0) {
            	
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Single Image Processing");
            	alert.setHeaderText(null);
            	alert.setContentText("Switch to Single Image Processing?");
            	
            	// Get the Stage.
            	Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

            	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
            	
            	Optional<ButtonType> result = alert.showAndWait();
            	if (result.get() == ButtonType.OK){
            		new windows.LoadSingleImage(lastOpenedFileFolder, lastSavedFileFolder).start(primaryStage);
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
       
       try {
    	   
		start = start ? getNextImage() : start;
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       HBox hBox = new HBox();
            
       hBox.getChildren().add(batchProcess);
       hBox.getChildren().add(changeDestDir);
       hBox.getChildren().add(loadSingleImage);
       hBox.getChildren().add(openWebCam);
       hBox.setPadding(new Insets(10));
       
       layout.setBottom(hBox);
        primaryStage.setScene(scene);
        
        primaryStage.show();
        
        if(imageSet) {
     	   openArttextBox();
     	  new BatchProcessorWindow(this.lastOpenedFileFolder, this.lastSavedFileFolder, this.completedFiles).start(primaryStage);
        }
        
    }
 
    void addImage(Image i, StackPane pane){
 
        imageView = new ImageView();
        imageView.setImage(i);
        pane.getChildren().add(imageView);

    }
 
    boolean getNextImage() throws FileNotFoundException {
    	
    	
    	File[] listOfFilesLOFF = this.lastOpenedFileFolder.listFiles(new FileFilter() {
    	    public boolean accept(File directory) {
    	        return directory.getName().endsWith(".jpg") | directory.getName().endsWith(".jpeg") | 
    	        		directory.getName().endsWith(".png") | directory.getName().endsWith(".JPG") | directory.getName().endsWith(".JPEG")
    	        		| directory.getName().endsWith(".PNG");
    	    }
    	});
    	
    	
    	List<File> listLOFF = new ArrayList<>();
    	for(File f: listOfFilesLOFF) {
    		if(!(f.isDirectory() | f == null)) {
    			listLOFF.add(f);
    		}
    	}
    	
    	boolean flagForImagSet = false;
    	for(File f: listLOFF) {
    		if(! this.completedFiles.contains(f)) {
    			addImage(new Image(new FileInputStream(f.getAbsolutePath()), 400, 400, false, false), contentPane);
    			this.completedFiles.add(f);
    			sourceFilePath = f;
    			flagForImagSet = true;
    			break;
    		}
    	}
    	imageSet = flagForImagSet;
    	
    	return false;
    }
    
    /**
	 * @param lastOpenedFileFolder
	 * @param lastSavedFileFolder
	 */
	public BatchProcessorWindow(File lastOpenedFileFolder, File lastSavedFileFolder, List<File> completedFiles) {
		super();
		this.lastOpenedFileFolder = lastOpenedFileFolder;
		this.lastSavedFileFolder = lastSavedFileFolder;
		this.completedFiles = completedFiles;
	}

	void openArttextBox() {
    	TextInputDialog dialog = new TextInputDialog();
    	dialog.setTitle("Article Number");
    	dialog.setHeaderText("Please enter the ARTICLE NUMBER");
    	dialog.setContentText(null);
    	// Get the Stage.
    	Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

    	// Add a custom icon.
    	stage.getIcons().add(new Image("/images/PumaSplashSingleCut.png"));
    	Optional<String> result = dialog.showAndWait();
    
    	result.ifPresent(name -> saveFile(name));
    }

    void saveFile(String name) {
    	File outPath = new File(lastSavedFileFolder.toPath()+"/"+name+".jpeg");
    	try {
			Files.copy(sourceFilePath.toPath(), outPath.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
	/**
	 * @param lastOpenedFileFolder
	 */
	public BatchProcessorWindow(File lastOpenedFileFolder) {
		super();
		this.lastOpenedFileFolder = lastOpenedFileFolder;
	}
 
}
