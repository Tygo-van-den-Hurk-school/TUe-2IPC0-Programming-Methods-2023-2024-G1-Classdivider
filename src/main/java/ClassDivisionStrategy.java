/**
 * Enumeration of class division strategies.
 * 
 * @author Huub de Beer
 */
public enum ClassDivisionStrategy {
    
    /** Divide a class randomly.*/
    Random("Random"), 
    /** Divide a class based on lastName. */
    LastName("Lastname");

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
            
            case Random -> new RandomClassDivider();
            default -> new RandomClassDivider();
        };
    }
}
