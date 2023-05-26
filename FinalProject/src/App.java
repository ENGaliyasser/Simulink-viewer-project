import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulink Viewer");

        // Create a FileChooser to allow the user to select a file
        FileChooser fileChooser = new FileChooser();

        // Create a button to trigger the file selection
        Button button = new Button("Select File");
        button.setOnAction(e -> {
            try {
                // Open the file chooser and wait for the user to select a file
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                String fileName = selectedFile.getName();

                // Check the file extension
                if (!fileName.endsWith(".mdl")) {
                    throw new NotVaildMDLFileException("Invalid file extension");
                }

                // Check if the selected file is empty
                if (selectedFile.length() == 0) {
                    throw new EmptyMDLFileException("Empty file selected");
                }

                // Extract the Simulink blocks from the MDL file
                MDLFileExtractor system = new MDLFileExtractor();
                String sys = MDLFileExtractor.MDLblocks(selectedFile);

                // Create a Pane to display the Simulink diagram
                Pane pane = new Pane();

                // Extract the blocks from the MDL file and draw them on the pane
                system.extractBlocks(sys);
                system.draw(pane);

                // Draw the lines connecting the blocks on the pane
                Lines.linesDrawer(sys, pane, system.getBlocks());

                // Create a Scene with the pane and set it as the primary stage's scene
                Scene scene = new Scene(pane, 1300, 600);
                primaryStage.setScene(scene);

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (EmptyMDLFileException e2) {
                e2 = new EmptyMDLFileException(e2.getMessage());
            } catch (NotVaildMDLFileException s) {
                s = new NotVaildMDLFileException(s.getMessage());
            }
        });

        // Set the position of the button
        button.setLayoutX(410);
        button.setLayoutY(450);

        // Create the root pane and add the button and text to it
        Pane root = new Pane();
        Text text = new Text(260, 250, "SimuLink Viewer");
        text.setFont(new Font(100));
        text.setStyle(
                "-fx-font: bold 45px Arial; -fx-fill:Black;"
        );
        text.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().addAll(button, text);

        // Create the scene with the root pane and set it as the primary stage's scene
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
