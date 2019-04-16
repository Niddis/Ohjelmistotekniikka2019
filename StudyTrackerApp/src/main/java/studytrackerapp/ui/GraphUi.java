package studytrackerapp.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class GraphUi extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label usernameLabel = new Label("käyttäjätunnus");
        TextField usernameInput = new TextField();
        Label passwordLabel = new Label("salasana");
        PasswordField passwordInput = new PasswordField();
        
        inputPane.getChildren().addAll(usernameLabel, usernameInput);
        Label loginMessage = new Label();
        
        Button loginButton = new Button("login");
        Button createButton = new Button("create new user");
        
        loginButton.setOnAction(e->{
            String username = usernameInput.getText();
            String password = passwordInput.getText();
        });
    }
    
}
