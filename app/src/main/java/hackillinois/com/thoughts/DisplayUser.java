package hackillinois.com.thoughts;

/**
 * Created by jatin1 on 2/20/16.
 */
public class DisplayUser {
    private String name;
    private int followers;
    private String image;

    public DisplayUser(String name, int followers, String image) {
        this.name = name;
        this.followers = followers;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
