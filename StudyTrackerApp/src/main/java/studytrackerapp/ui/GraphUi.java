package studytrackerapp.ui;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import studytrackerapp.database.Database;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.Service;

public class GraphUi extends Application {
    private Scene newUserScene;
    private Scene coursesScene;
    private Scene newCourseScene;
    private VBox courseNodes;
    private Service service;
    private Database database;
    private Label message = new Label();
    private Course currentCourse;

    @Override
    public void init() throws Exception {
        database = new Database("jdbc:sqlite:sta.db");
        database.init();
        service = new Service(database);
        
    }

    @Override
    public void start(Stage primary) throws Exception {
        
    //----- login -----
        //message.setText("");
        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10));
        
        Label usernameLabel = new Label("käyttäjätunnus");
        TextField usernameInput = new TextField();
        Label passwordLabel = new Label("salasana");
        PasswordField passwordInput = new PasswordField();
        Label loginMessage = new Label();
        
        Button loginButton = new Button("kirjaudu");
        Button createButton = new Button("luo uusi tunnus");
        loginButton.setOnAction(e->{
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            System.out.println(username + " " + password);
            boolean loginOk = service.login(username, password);
            if (loginOk) {
                message.setText("");
                updateCourseList();
                primary.setScene(coursesScene);
            } else {
                message.setText("Virheellinen käyttäjätunnus tai salasana.");
                message.setTextFill(Color.RED);
            }
        });
        createButton.setOnAction(e -> {
            message.setText("");
            primary.setScene(newUserScene);
        });

        loginPane.add(message, 1, 0, 3, 1);
        loginPane.addRow(1, usernameLabel, usernameInput);
        loginPane.addRow(2, passwordLabel, passwordInput);
        loginPane.add(loginButton, 1, 3);
        loginPane.add(createButton, 1, 4);

        loginPane.setVgap(15);
        loginPane.setHgap(10);

        Scene loginScene = new Scene(loginPane, 400, 250);
        
        //----- creating new user -----
        
        GridPane newUserPane = new GridPane();
        newUserPane.setPadding(new Insets(10));
        
        Label newNameLabel = new Label("Nimi");
        Label newUsernameLabel = new Label("Käyttäjätunnus");
        Label newPasswordLabel = new Label("Salasana");
        Label newPasswordLabel2 = new Label("Salasana uudestaan");
        TextField newNameInput = new TextField();
        TextField newUsernameInput = new TextField();
        PasswordField newPasswordInput = new PasswordField();
        PasswordField newPasswordInput2 = new PasswordField();
        Button createNewUserButton = new Button("Luo uusi käyttäjä");
        createNewUserButton.setOnAction(e -> {
            if (newNameInput.getText().trim().length() < 2 || newUsernameInput.getText().trim().length() < 2 || newPasswordInput.getText().trim().length() < 5) {
                message.setText("Nimi, käyttäjätunnus tai salasana liian lyhyt");
                message.setTextFill(Color.RED);
            } else if (!newPasswordInput.getText().equals(newPasswordInput2.getText())) {
                message.setText("Salasanakentät eivät vastanneet toisiaan. Anna salasana uudestaan");
                newPasswordInput.setText("");
                newPasswordInput2.setText("");
            } else {
                boolean newUserOk = service.createNewUser(0, newNameInput.getText(), newUsernameInput.getText(), newPasswordInput.getText());
                if (newUserOk) {
                    message.setText("Uusi käyttäjä luotu");
                    message.setTextFill(Color.GREEN);
                    primary.setScene(loginScene);
                } else {
                    message.setText("Käyttäjätunnus on jo käytössä.");
                    message.setTextFill(Color.RED);
                }
            }
            
        });
        
        newUserPane.add(message, 0, 0, 3, 1);
        newUserPane.addRow(1, newNameLabel, newNameInput);
        newUserPane.addRow(2, newUsernameLabel, newUsernameInput);
        newUserPane.addRow(3, newPasswordLabel, newPasswordInput);
        newUserPane.addRow(4, newPasswordLabel2, newPasswordInput2);
        newUserPane.add(createNewUserButton, 1, 5);
        newUserPane.setVgap(15);
        newUserPane.setHgap(10);
        
        newUserScene = new Scene(newUserPane, 400, 250);
        
        //----- main -----
        
        ScrollPane coursesScrollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(coursesScrollbar);
        coursesScene = new Scene(mainPane, 400, 250);
        
        Button createCourse = new Button("lisää kurssi");
        createCourse.setOnAction(e -> {
            System.out.println("lisää kurssi");
            primary.setScene(newCourseScene);
        });
        
        courseNodes = new VBox(10);
        updateCourseList();
        coursesScrollbar.setContent(courseNodes);
        mainPane.setBottom(createCourse);
        
        //----- creating new course -----
        
        GridPane newCoursePane = new GridPane();
        newCoursePane.setPadding(new Insets(10));
        
        Label newCourseNameLabel = new Label("Nimi");
        Label newCoursePointsLabel = new Label("Opintopisteet");
        Label newCourseCompulsoryLabel = new Label("Onko kurssi pakollinen?");
        TextField newCourseNameField = new TextField();
        TextField newCoursePointsField = new TextField();
        Label newCourseMessage = new Label();
        
        ToggleGroup group = new ToggleGroup();
        RadioButton yesButton = new RadioButton("kyllä");
        yesButton.setToggleGroup(group);
        yesButton.setSelected(true);
        RadioButton noButton = new RadioButton("ei");
        noButton.setToggleGroup(group);
        VBox selectButtonsPane = new VBox(5);
        selectButtonsPane.getChildren().addAll(yesButton, noButton);
        
        Button createNewCourseButton = new Button("Luo kurssi");
        createNewCourseButton.setOnAction(e -> {
            System.out.println("nimi:" + newCourseNameField.getText().trim() + ".");
            if (isInputAnInteger(newCoursePointsField)) {
                int points = Integer.parseInt(newCoursePointsField.getText());
                System.out.println(points);
            } else {
                newCourseMessage.setText("Et antanut kokonaislukua");
                newCoursePointsField.setText("");
            }
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            System.out.println(selected.getText());
        });
        Button backToMainSceneButton = new Button("Takaisin");
        backToMainSceneButton.setOnAction(e -> {
            primary.setScene(coursesScene);
        });
        
        newCoursePane.add(newCourseMessage, 0, 0);
        newCoursePane.addRow(1, newCourseNameLabel, newCourseNameField);
        newCoursePane.addRow(2, newCoursePointsLabel, newCoursePointsField);
        newCoursePane.addRow(3, newCourseCompulsoryLabel, selectButtonsPane);
        newCoursePane.add(createNewCourseButton, 1, 4);
        newCoursePane.add(backToMainSceneButton, 1, 5);
        newCoursePane.setVgap(15);
        newCoursePane.setHgap(10);
        
        newCourseScene = new Scene(newCoursePane, 400, 250);
        
        //----- updating course -----
        
        GridPane updateCoursePane = new GridPane();
        
        //----- setup -----
        
        primary.setTitle("Opintojen seurantasovellus");
        primary.setScene(loginScene);
        primary.show();
    }
    

    public Node createNode(Course course) {
        String compulsoryText;
        if (course.getCompulsory() == 1) {
            compulsoryText = "pakollinen";
        } else {
            compulsoryText = "valinnainen";
        }
        String doneText;
        if (course.getDone() == 0) {
            doneText = "suorittamatta";
        } else {
            doneText = "suoritettu";
        }
        HBox box = new HBox(10);
        Label content = new Label(course.getName() + " " + doneText + " " + compulsoryText + " " + course.getPoints() + " op");
        Button update = new Button("muokkaa");
        update.setOnAction(e -> {
            currentCourse = course;
            System.out.println("muokattava kurssi: " + currentCourse.getName());
        });
        box.getChildren().addAll(content, update);
        return box;
    }
    
    public void updateCourseList() {
        courseNodes.getChildren().clear();
        List<Course> courses = service.listCoursesByUser();
        if (!courses.isEmpty() && courses != null) {
            courses.forEach(course -> {
                courseNodes.getChildren().add(createNode(course));
            });
        }
        System.out.println("Kurssilista: " + courses);
    }
    
    private List<String> coursesList() {
        List<String> courses = new ArrayList<>();
        courses.add("Ohpe pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        courses.add("Ohja pakollinen suorittamatta 5 op");
        return courses;
    }
    
    private boolean isInputAnInteger(TextField field) {
        Boolean isValid = false;
        if (!(field.getText() == null || field.getText().length() == 0)) {
            try {
                int points = Integer.parseInt(field.getText());
                isValid = true;
            } catch (NumberFormatException e) {
                //System.out.println("et anatanut numeroa");
            }
        }
        return isValid;
    }
    
    public static void main(String[] args) {
        launch(GraphUi.class);
    }
    
}
