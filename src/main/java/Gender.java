/**
 * Self-reported gender.
 * 
 * @author Huub de Beer
 */
public enum Gender {

    /** Female gender. */
    FEMALE("F"), 

    /** Male gender. */
    MALE("M"), 

    /** Gender not reported. */
    NOT_REPORTED("-"), 

    /** X gender. */
    X("X");
    
    /** Stores the key of this enum. */
    private final String key;
    
    /** Creates a new {@code Gender} Object from the key. */
    private Gender(final String key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return this.key;
    }
    
    /**
     * Create a {@code Gender} from string. 
     * 
     * @param s string to convert to gender
     * @return {@code Gender} corresponding to {@code s}. If {@code s} isn't recognized
     * as a gender, {@code NOT_REPORTED} is returned.
     */
    public static Gender fromString(String s) {
        return switch (s.toUpperCase()) {
            case "F" -> FEMALE;
            case "M" -> MALE;
            case "X" -> X;
            default -> NOT_REPORTED;
        };
    }
}
