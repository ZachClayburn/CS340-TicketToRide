package modelAttributes;

public class Username {
    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Username(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }
}
