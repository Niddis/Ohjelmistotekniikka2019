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
            boolean loginOk = service.login(username, password);
            if (loginOk) {
                message.setText("");
                service.listCoursesByUser();
                updateCourseList(primary);
                primary.setScene(listCourses(primary));
                usernameInput.setText("");
                passwordInput.setText("");
            } else {
                loginMessage.setText("Virheellinen käyttäjätunnus tai salasana.");
                loginMessage.setTextFill(Color.RED);
            }
        });
        createButton.setOnAction(e -> {
            loginMessage.setText("");
            primary.setScene(createUser(primary));
        });

        loginPane.add(loginMessage, 1, 0, 3, 1);
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
        Label newNameInputInfo = new Label("Nimen on oltava 2-50 merkkiä pitkä.");
        Label newUsernameInputInfo = new Label("Käyttäjätunnuksen on oltava 2-10 merkkiä pitkä.");
        Label newPasswordInputInfo = new Label("Salasanan on oltava 5-15 merkkiä pitkä.");
        TextField newNameInput = new TextField();
        TextField newUsernameInput = new TextField();
        PasswordField newPasswordInput = new PasswordField();
        PasswordField newPasswordInput2 = new PasswordField();
        Button createNewUserButton = new Button("Luo uusi käyttäjä");
        createNewUserButton.setOnAction(e -> {
            String name = newNameInput.getText().trim();
            String username = newUsernameInput.getText().trim();
            String password = newPasswordInput.getText().trim();
            String password2 = newPasswordInput2.getText().trim();
            if (name.length() < 2 || name.length() > 50 || username.length() < 2 || username.length() > 10 || password.length() < 5 || password.length() > 15) {
                message.setText("Nimen, käyttäjätunnuksen tai salasanan pituus ei kelpaa.");
                message.setTextFill(Color.RED);
            } else if (!password.equals(password2)) {
                message.setText("Salasanakentät eivät vastanneet toisiaan. Anna salasana uudestaan.");
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
        Button backToLoginSceneButton = new Button("Takaisin");
        backToLoginSceneButton.setOnAction(e -> {
            stage.setScene(loginScene);
        });
        
        newUserPane.add(message, 0, 0, 3, 1);
        newUserPane.addRow(1, newNameLabel, newNameInput);
        newUserPane.add(newNameInputInfo, 1, 2);
        newUserPane.addRow(3, newUsernameLabel, newUsernameInput);
        newUserPane.add(newUsernameInputInfo, 1, 4);
        newUserPane.addRow(5, newPasswordLabel, newPasswordInput);
        newUserPane.add(newPasswordInputInfo, 1, 6);
        newUserPane.addRow(7, newPasswordLabel2, newPasswordInput2);
        newUserPane.add(createNewUserButton, 1, 8);
        newUserPane.add(backToLoginSceneButton, 1, 9);
        newUserPane.setVgap(15);
        newUserPane.setHgap(10);
        
        return new Scene(newUserPane, 500, 400);
    }
    
    private Scene listCourses(Stage stage) {
        ScrollPane coursesScrollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(coursesScrollbar);
        coursesScene = new Scene(mainPane, 500, 400);
        
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
        
        Label pointsSumLabel = new Label("Suoritettuja opintoja yhteensä ");
        HBox selectSorterPane = new HBox(5);
        selectSorterPane.setPadding(new Insets(10));
        selectSorterPane.getChildren().addAll(nameButton, doneButton, compulsoryButton, pointsButton, sortButton);
        HBox buttons = new HBox(10);
        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(10));
        buttons.getChildren().addAll(createCourseButton, logoutButton);
        bottom.getChildren().addAll(pointsSumLabel ,buttons);
        coursesScrollbar.setContent(courseNodes);
        mainPane.setBottom(bottom);
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
        Label newCourseNameInfo = new Label("Nimen pituuden on oltava 1-30 merkkiä.");
        Label newCoursePointsInfo = new Label("Pisteiden on oltava kokonaisluku väliltä 0-100.");
        
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
            String name = newCourseNameField.getText().trim();
            int points;
            int compulsory = 0;
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected.getText().equals("kyllä")) {
                compulsory = 1;
            }
            if (isInputAnInteger(newCoursePointsField)) {
                points = Integer.parseInt(newCoursePointsField.getText());
                if (name.length() > 0 && name.length() <= 30 && points >= 0 && points <= 100) {
                    service.createNewCourse(name, compulsory, points);
                    updateCourseList(stage);
                    stage.setScene(coursesScene);
                } else {
                    newCourseMessage.setText("Kurssin nimi tai pisteet ei kelpaa.");
                    newCourseMessage.setTextFill(Color.RED);
                }
            } else {
                newCourseMessage.setText("Et antanut kokonaislukua");
                newCourseMessage.setTextFill(Color.RED);
                newCoursePointsField.setText("");
            }
            
        });
        Button chooseCourseButton = new Button("Valitse kurssi");
        chooseCourseButton.setOnAction(e -> {
            Course selectedCourse = (Course) chooseCourse.getValue();
            boolean success = service.createNewCourse(selectedCourse.getName(), selectedCourse.getCompulsory(), selectedCourse.getPoints());
            updateCourseList(stage);
            stage.setScene(coursesScene);
        });
        Button backToMainSceneButton = new Button("Takaisin");
        backToMainSceneButton.setOnAction(e -> {
            stage.setScene(coursesScene);
        });
        
        newCoursePane.add(newCourseMessage, 0, 0, 3, 1);
        newCoursePane.add(chooseCourse, 1, 1);
        newCoursePane.add(chooseCourseButton, 1, 2);
        //newCoursePane.addRow(1, chooseCourse, chooseCourseButton);
        newCoursePane.addRow(3, newCourseNameLabel, newCourseNameField);
        newCoursePane.add(newCourseNameInfo, 1, 4);
        newCoursePane.addRow(5, newCoursePointsLabel, newCoursePointsField);
        newCoursePane.add(newCoursePointsInfo, 1, 6);
        newCoursePane.addRow(7, newCourseCompulsoryLabel, selectButtonsPane);
        //newCoursePane.addRow(7, createNewCourseButton, backToMainSceneButton);
        newCoursePane.add(createNewCourseButton, 1, 8);
        newCoursePane.add(backToMainSceneButton, 2, 8);
        
        newCoursePane.setVgap(15);
        newCoursePane.setHgap(10);
        
        newCourseScene = new Scene(newCoursePane, 600, 400);
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
        Label updateCourseNameInfo = new Label("Nimen pituuden on oltava 1-30 merkkiä.");
        Label updateCoursePointsInfo = new Label("Opintopisteiden on oltava kokonaisluku väliltä 0-100.");
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
            if (selectedCompulsory.getText().equals("kyllä")) {
                compulsory = 1;
            }
            RadioButton selectedDone = (RadioButton) updateDoneGroup.getSelectedToggle();
            if (selectedDone.getText().equals("kyllä")) {
                done = 1;
            }
           
            if (isInputAnInteger(updateCoursePointsInput)) {
                points = Integer.parseInt(updateCoursePointsInput.getText());
                if (name.length() > 0 && name.length() <= 30 && points >= 0 && points <= 100) {
                    boolean success = service.updateCourse(currentCourse.getId(), name, done, compulsory, points);
                    if (success) {
                        updateCourseList(stage);
                        stage.setScene(coursesScene);
                    } else {
                        updateMessage.setText("Tapahtui virhe.");
                        updateMessage.setTextFill(Color.RED);
                    }
                    
                } else {
                    updateMessage.setText("Kurssin nimi tai opintopisteet ei kelpaa.");
                    updateMessage.setTextFill(Color.RED);
                }
            } else {
                updateMessage.setText("Opintopisteiden on oltava kokonaisluku.");
                updateMessage.setTextFill(Color.RED);
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
        updateCoursePane.add(updateCourseNameInfo, 1, 2);
        updateCoursePane.addRow(3, updateCoursePointsLabel, updateCoursePointsInput);
        updateCoursePane.add(updateCoursePointsInfo, 1, 4);
        updateCoursePane.addRow(5, updateCourseCompulsoryLabel, selectCompulsoryButtonsPane);
        updateCoursePane.addRow(6, updateCourseDoneLabel, selectDoneButtonsPane);
        updateCoursePane.addRow(7, updateCourseButton, backToMainSceneButton);
        //updateCoursePane.add(updateCourseButton, 1, 7);
        //updateCoursePane.add(backToMainSceneButton, 1, 8);
        updateCoursePane.setVgap(15);
        updateCoursePane.setHgap(10);
        
        updateCourseScene = new Scene(updateCoursePane, 600, 400);
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
        });
        Button deleteButton = new Button("poista");
        deleteButton.setOnAction(e -> {
            currentCourse = course;
            service.deleteCourse(course.getId());
            updateCourseList(stage);
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(content, spacer, update, deleteButton);
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
    }
    
    private Label updatePointsSumLabel() {
        int sumPoints = service.getPointsSum();
        return new Label("Suoritettuja opintoja yhteensä " + sumPoints + " op");
    }
    
    private boolean isInputAnInteger(TextField field) {
        Boolean isValid = false;
        if (!(field.getText() == null || field.getText().trim().length() == 0)) {
            try {
                int points = Integer.parseInt(field.getText());
                isValid = true;
            } catch (NumberFormatException e) {
                
            }
        }
        return isValid;
    }
    
    public static void main(String[] args) {
        launch(GraphUi.class);
    }
    
}
