package modelAttributes;

import java.util.Objects;

public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password1 = (Password) o;
        return Objects.equals(getPassword(), password1.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword());
    }
}
