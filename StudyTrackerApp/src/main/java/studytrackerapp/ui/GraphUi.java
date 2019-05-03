package studytrackerapp.ui;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
    private Scene loginScene;
    //private Scene newUserScene;
    private Scene coursesScene;
    private Scene newCourseScene;
    private Scene updateCourseScene;
    private VBox courseNodes;
    private Service service;
    private Database database;
    private Label message = new Label();
    private Course currentCourse;

    @Override
    public void init() throws Exception {
        this.courseNodes = new VBox(10);
        
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        String databaseAddress = properties.getProperty("databaseAddress");
        String coursesFile = properties.getProperty("coursesFile");
        
        this.database = new Database(databaseAddress);
        database.init();
        this.service = new Service(database, coursesFile);
        
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
            //System.out.println(username + " " + password);
            boolean loginOk = service.login(username, password);
            if (loginOk) {
                message.setText("");
                service.listCoursesByUser();
                updateCourseList(primary);
                primary.setScene(listCourses(primary));
                usernameInput.setText("");
                passwordInput.setText("");
            } else {
                message.setText("Virheellinen käyttäjätunnus tai salasana.");
                message.setTextFill(Color.RED);
            }
        });
        createButton.setOnAction(e -> {
            message.setText("");
            primary.setScene(createUser(primary));
        });

        loginPane.add(message, 1, 0, 3, 1);
        loginPane.addRow(1, usernameLabel, usernameInput);
        loginPane.addRow(2, passwordLabel, passwordInput);
        loginPane.add(loginButton, 1, 3);
        loginPane.add(createButton, 1, 4);

        loginPane.setVgap(15);
        loginPane.setHgap(10);

        loginScene = new Scene(loginPane, 400, 250);

        //----- setup -----
        
        primary.setTitle("Opintojen seurantasovellus");
        primary.setScene(loginScene);
        primary.show();
    }
    
    private Scene createUser(Stage stage) {
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
                    stage.setScene(loginScene);
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
        
        return new Scene(newUserPane, 400, 250);
    }
    
    private Scene listCourses(Stage stage) {
        ScrollPane coursesScrollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(coursesScrollbar);
        coursesScene = new Scene(mainPane, 400, 250);
        
        ToggleGroup selectSorterGroup = new ToggleGroup();
        RadioButton nameButton = new RadioButton("nimi");
        RadioButton doneButton = new RadioButton("suoritettu");
        RadioButton compulsoryButton = new RadioButton("pakollinen");
        RadioButton pointsButton = new RadioButton("pisteet");
        nameButton.setToggleGroup(selectSorterGroup);
        doneButton.setToggleGroup(selectSorterGroup);
        compulsoryButton.setToggleGroup(selectSorterGroup);
        pointsButton.setToggleGroup(selectSorterGroup);
        nameButton.setSelected(true);
        
        
        Button createCourseButton = new Button("lisää kurssi");
        createCourseButton.setOnAction(e -> {
            stage.setScene(createCourse(stage));
        });
        Button logoutButton = new Button("kirjaudu ulos");
        logoutButton.setOnAction(e -> {
            service.logout();
            stage.setScene(loginScene);
        });
        Button sortButton = new Button("järjestä");
        sortButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) selectSorterGroup.getSelectedToggle();
            service.sortCoursesList(selected.getText());
            updateCourseList(stage);
        });
        
        HBox selectSorterPane = new HBox(5);
        selectSorterPane.getChildren().addAll(nameButton, doneButton, compulsoryButton, pointsButton, sortButton);
        //courseNodes = new VBox(10);
        //updateCourseList(primary);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(createCourseButton, logoutButton);
        coursesScrollbar.setContent(courseNodes);
        mainPane.setBottom(buttons);
        mainPane.setTop(selectSorterPane);
        return coursesScene;
    }
    
    private Scene createCourse(Stage stage) {
        GridPane newCoursePane = new GridPane();
        newCoursePane.setPadding(new Insets(10));
        
        ChoiceBox chooseCourse = new ChoiceBox();
        for (Course course: service.getCoursesFromFile()) {
            chooseCourse.getItems().add(course);
        }
        
        
        
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
            //System.out.println("nimi:" + newCourseNameField.getText().trim() + ".");
            String name = newCourseNameField.getText().trim();
            int points;
            int compulsory = 0;
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected.getText().equals("kyllä")) {
                compulsory = 1;
            }
            if (isInputAnInteger(newCoursePointsField)) {
                points = Integer.parseInt(newCoursePointsField.getText());
                //System.out.println(points);
                if (name.length() != 0) {
                    service.createNewCourse(name, compulsory, points);
                    updateCourseList(stage);
                    stage.setScene(coursesScene);
                } else {
                    newCourseMessage.setText("Kurssin nimi on pakollinen.");
                }
            } else {
                newCourseMessage.setText("Et antanut kokonaislukua");
                newCoursePointsField.setText("");
            }
            
            //System.out.println(selected.getText());
        });
        Button chooseCourseButton = new Button("Valitse kurssi");
        chooseCourseButton.setOnAction(e -> {
            Course selectedCourse = (Course) chooseCourse.getValue();
            System.out.println("nimi: " + selectedCourse.getName());
            boolean success = service.createNewCourse(selectedCourse.getName(), selectedCourse.getCompulsory(), selectedCourse.getPoints());
            updateCourseList(stage);
            stage.setScene(coursesScene);
        });
        Button backToMainSceneButton = new Button("Takaisin");
        backToMainSceneButton.setOnAction(e -> {
            stage.setScene(coursesScene);
        });
        
        newCoursePane.add(newCourseMessage, 0, 0, 3, 1);
        newCoursePane.addRow(1, newCourseNameLabel, newCourseNameField);
        newCoursePane.addRow(2, newCoursePointsLabel, newCoursePointsField);
        newCoursePane.addRow(3, newCourseCompulsoryLabel, selectButtonsPane);
        newCoursePane.add(createNewCourseButton, 1, 4);
        newCoursePane.add(backToMainSceneButton, 1, 5);
        newCoursePane.addRow(6, chooseCourseButton, chooseCourse);
        newCoursePane.setVgap(15);
        newCoursePane.setHgap(10);
        
        newCourseScene = new Scene(newCoursePane, 400, 350);
        return newCourseScene;
    }
    
    private Scene updateCourse(Stage stage) {
        GridPane updateCoursePane = new GridPane();
        updateCoursePane.setPadding(new Insets(10));
        
        Label updateCourseNameLabel = new Label("Nimi");
        Label updateCoursePointsLabel = new Label("Opintopisteet");
        Label updateCourseDoneLabel = new Label("Kurssi on suoritettu");
        Label updateCourseCompulsoryLabel = new Label("Kurssi on  pakollinen");
        TextField updateCourseNameInput = new TextField(currentCourse.getName());
        TextField updateCoursePointsInput = new TextField(String.valueOf(currentCourse.getPoints()));
        Label updateMessage = new Label();
        
        ToggleGroup updateDoneGroup = new ToggleGroup();
        RadioButton doneButton = new RadioButton("kyllä");
        doneButton.setToggleGroup(updateDoneGroup);
        RadioButton notDoneButton = new RadioButton("ei");
        notDoneButton.setToggleGroup(updateDoneGroup);
        if (currentCourse.getDone() == 1) {
            doneButton.setSelected(true);
        } else {
            notDoneButton.setSelected(true);
        }
        VBox selectDoneButtonsPane = new VBox(5);
        selectDoneButtonsPane.getChildren().addAll(doneButton, notDoneButton);
        
        ToggleGroup updateCompulsoryGroup = new ToggleGroup();
        RadioButton compulsoryButton = new RadioButton("kyllä");
        compulsoryButton.setToggleGroup(updateCompulsoryGroup);
        RadioButton notCompulsoryButton = new RadioButton("ei");
        notCompulsoryButton.setToggleGroup(updateCompulsoryGroup);
        if (currentCourse.getCompulsory() == 1) {
            compulsoryButton.setSelected(true);
        } else {
            notCompulsoryButton.setSelected(true);
        }
        
        Button updateCourseButton = new Button("Tallenna muutokset");
        updateCourseButton.setOnAction(e -> {
            int compulsory = 0;
            int done = 0;
            int points;
            String name = updateCourseNameInput.getText().trim();
            
            RadioButton selectedCompulsory = (RadioButton) updateCompulsoryGroup.getSelectedToggle();
            //System.out.println(selected.getText());
            if (selectedCompulsory.getText().equals("kyllä")) {
                compulsory = 1;
            }
            RadioButton selectedDone = (RadioButton) updateDoneGroup.getSelectedToggle();
            if (selectedDone.getText().equals("kyllä")) {
                done = 1;
            }
            if (isInputAnInteger(updateCoursePointsInput)) {
                points = Integer.parseInt(updateCoursePointsInput.getText());
                //System.out.println(points);
                if (name.length() != 0) {
                    service.updateCourse(currentCourse.getId(), name, done, compulsory, points);
                    updateCourseList(stage);
                    stage.setScene(coursesScene);
                } else {
                    updateMessage.setText("Kurssin nimi on pakollinen.");
                }
            } else {
                updateMessage.setText("Anna opintopisteet-kohtaan kokonaisluku.");
                updateCoursePointsInput.setText("");
            }         
        });
        Button backToMainSceneButton = new Button("Takaisin");
        backToMainSceneButton.setOnAction(e -> {
            stage.setScene(coursesScene);
        });
        
        VBox selectCompulsoryButtonsPane = new VBox(5);
        selectCompulsoryButtonsPane.getChildren().addAll(compulsoryButton, notCompulsoryButton);
        
        updateCoursePane.add(updateMessage, 0, 0, 3, 1);
        updateCoursePane.addRow(1, updateCourseNameLabel, updateCourseNameInput);
        updateCoursePane.addRow(2, updateCoursePointsLabel, updateCoursePointsInput);
        updateCoursePane.addRow(3, updateCourseCompulsoryLabel, selectCompulsoryButtonsPane);
        updateCoursePane.addRow(4, updateCourseDoneLabel, selectDoneButtonsPane);
        updateCoursePane.add(updateCourseButton, 1, 5);
        updateCoursePane.add(backToMainSceneButton, 1, 6);
        updateCoursePane.setVgap(15);
        updateCoursePane.setHgap(10);
        
        updateCourseScene = new Scene(updateCoursePane, 400, 350);
        return updateCourseScene;
    }
    
    public Node createNode(Course course, Stage stage) {
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
            stage.setScene(updateCourse(stage));
            //System.out.println("muokattava kurssi: " + currentCourse.getName());
        });
        Button deleteButton = new Button("poista");
        deleteButton.setOnAction(e -> {
            currentCourse = course;
            service.deleteCourse(course.getId());
            updateCourseList(stage);
        });
        box.getChildren().addAll(content, update, deleteButton);
        return box;
    }
    
    public void updateCourseList(Stage stage) {
        courseNodes.getChildren().clear();
        List<Course> courses = service.getCourses();
        if (!courses.isEmpty() && courses != null) {
            courses.forEach(course -> {
                courseNodes.getChildren().add(createNode(course, stage));
            });
        }
        //System.out.println("Kurssilista: " + courses);
    }
        
    private boolean isInputAnInteger(TextField field) {
        Boolean isValid = false;
        if (!(field.getText() == null || field.getText().trim().length() == 0)) {
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
