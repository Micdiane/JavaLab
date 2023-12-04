package MsgGUI;

public class Contact {
    private String email;
    private String name;  // Add other fields as needed

    // Constructors, getters, setters, etc.

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
