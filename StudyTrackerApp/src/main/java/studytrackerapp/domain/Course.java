
package studytrackerapp.domain;

public class Course {
    private int id;
    private String name;
    private int done;
    private int compulsory;
    private int points;
    private User user;

    public Course(String name, int compulsory, int points) {
        //this.id = id;
        this.name = name;
        this.done = 0;
        this.compulsory = compulsory;
        this.points = points;
        //this.user = user;
    }

    public Course(int id, String name, int done, int compulsory, int points, User user) {
        this.id = id;
        this.name = name;
        this.done = done;
        this.compulsory = compulsory;
        this.points = points;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDone() {
        return done;
    }

    public int getCompulsory() {
        return compulsory;
    }

    public int getPoints() {
        return points;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setCompulsory(int compulsory) {
        this.compulsory = compulsory;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        String compulsoryString;
        if (compulsory == 1) {
            compulsoryString = "pakollinen";
        } else {
            compulsoryString = "valinnainen"; 
        }
        String courseString = name + " " + compulsoryString + " " + points + " op";
        return courseString;
    }
}
