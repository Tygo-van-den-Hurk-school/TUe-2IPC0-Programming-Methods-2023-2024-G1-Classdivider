/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import java.util.List;

/**
 * Enumeration of class division strategies.
 * 
 * @author Huub de Beer
 */
public enum ClassDivisionStrategy {
    
    /** Divide a class randomly.*/
    Random("Random"), 
    /** Divide a class based on lastName. */
    LastName("Lastname"), 
    /** Divide a class based on gender. */
    Genders("Genders"), 
    /** Divide a class based on their nationality. */
    DutchAndInternationals("DutchAndInternationals");

    /** Stores the key used for the enum. */
    private final String key;
    
    /** Creates a new {@code ClassDivisionStrategy} from a {@code key}. */
    private ClassDivisionStrategy(final String key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return this.key;
    }

    /**
     * Gets a descendant of {@code classdivder} based on the {@code strategy}.
     * 
     * @param strategy a ClassDivisionStrategy where the division is based on.
     * @return a classDivider instance to divide a Set of {@code Student}'s into a set of {@code
     * group}'s.
     * @post {@code 
     *   (
     *     (
     *        (strategy == ClassDivisionStrategy.Random) 
     *        <==> (\result instanceof RandomClassDivider)
     *     ) && (
     *        (strategy == ClassDivisionStrategy.LastName) 
     *        <==> (\result instanceof LastNameClassDivider)
     *     ) && (
     *        (strategy == ClassDivisionStrategy.DutchAndInternationals)
     *        <==> (\result instanceof BucketClassDivider)
     *     ) && (
     *        (strategy == ClassDivisionStrategy.Genders) 
     *        <==> (\result instanceof BucketClassDivider)
     *     ) && (
     *        (\result instanceof RandomClassDivider) <==> (
     *          (strategy != ClassDivisionStrategy.Random)
     *          || (strategy != ClassDivisionStrategy.LastName)
     *          || (strategy != ClassDivisionStrategy.DutchAndInternationals)
     *          || (strategy != ClassDivisionStrategy.Genders)
     *        )
     *     )
     *   )
     * }
     */
    public static ClassDivider create(final ClassDivisionStrategy strategy) {
        return switch (strategy) {
            case Genders -> new BucketClassDivider<Gender>(
                List.of(Gender.values()), 
                Student::gender);
            case DutchAndInternationals -> new BucketClassDivider<Boolean>(
                List.of(true, false), 
                Student::isDutch);
            case LastName -> new LastNameClassDivider();
            case Random -> new RandomClassDivider();
            default -> new RandomClassDivider();
        };
    }
}
