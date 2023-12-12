import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Divides a group of {@code Student}s into {@code Group}s based on the "bucket" they belong to.
 * 
 * @param <Type> the type of the bucket that the students belong to.
 * @author Tygo van den Hurk
 * @version 1.0
 * @see RandomClassDivider
 * @see Student
 * @see Group
 */
public class BucketClassDivider<Type> extends RandomClassDivider {
    
    private final List<Type> buckets;
    private final Function<Student, Type> determineBucket; 

    /**
     * Assigns the global variables {@code this.buckets} and {@code this.determineBucket} to
     * the parameters {@code buckets} and {@code determineBucket}.
     *  
     * @pre {@code buckets != null && determineBucket != null}
     * @param buckets is a list of possible buckets where students can belong to.
     * @param determineBucket is a function which links a student to a bucket they belong to. 
     * @post {@code this.buckets = buckets && this.determineBucket = determineBucket}
     */
    public BucketClassDivider(
        final List<Type> buckets, 
        final Function<Student, Type> determineBucket
    ) {
        super();
        
        /* Pre-Condition Guard-Statements */ {
            if (buckets == null) {
                throw new IllegalArgumentException(
                    "new " + this.getClass().getSimpleName() + "().pre violated, "
                    + "buckets cannot be null."
                );
            } 
            
            if (determineBucket == null) {
                throw new IllegalArgumentException(
                    "new " + this.getClass().getSimpleName() + "().pre violated, "
                    + "determineBucket cannot be null."
                );
            }
        }

        /* Assigning private fields */ {
            this.buckets = buckets;
            this.determineBucket = determineBucket;
        }
    }

    /**
     * divides a Set of Students ({@code klas}) into groups of {@code groupSize} +/- {@code
     * deviation}, randomly, by picking students at random. But making sure that each group has 
     * approximately the same amount of each category.
     * 
     * @param klas a Set of {@code Student}'s to make the groups from.
     * @param groupSize the size that each group should approximately have.
     * @param deviation the amount of students that any group can have extra or have missing.
     * 
     * @pre the following statements must all be true: <ul>
     *      <li>{@code this.buckets != null}</li>
     *      <li>{@code this.determineBucket != null}</li>
     *      <li>{@code this.isDividable(klas, groupSize, deviation)}</li>
     * </ul>
     * 
     * @return a Set of Students ({@code klas}) into groups of {@code groupSize} +/-
     * {@code deviation}, randomly, by picking students at random. But making sure 
     * that each group has approximately the same amount of each category.
     * 
     * @post {@code 
     *  \result == noEmptyGroupsPossible 
     *   && (
     *     (potential >= overflow) 
     *     || (overflow + potential >= minimalGroupSize)
     *   )
     * }
     * <br>
     * where:<ul>
     *   <li>{@code minimalGroupSize == groupSize - deviation}</li>
     *   <li>{@code noEmptyGroupsPossible == minimalGroupSize > 0}</li>
     *   <li>{@code overflow == klas.size() % groupSize}</li>
     *   <li>{@code potential == deviation * (klas.size() / groupSize)}</li>
     * </ul>
     * 
     * @throws IllegalArgumentException if Pre-condition is violated.
     */
    @Override
    public Set<Group<Student>> divide(
        final Group<Student> klas, 
        final int groupSize, 
        final int deviation
    ) throws IllegalArgumentException { 
    }
}