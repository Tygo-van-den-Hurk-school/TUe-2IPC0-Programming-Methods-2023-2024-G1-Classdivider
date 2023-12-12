/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    /** Throws an error on behave of {@link #divide(Group, int, int)}. */
    private final void validateDivide(
            final Group<Student> klas, 
            final int groupSize, 
            final int deviation
    ) throws IllegalArgumentException  {

        if (klas == null) {
            throw new IllegalArgumentException(
                this.getClass().getSimpleName() + ".divide(Group<Student>, int, int).pre " 
                + "violated, "
                // With reason:
                + "klas cannot be null."
            );
        }

        if (! this.isDividable(klas, groupSize, deviation)) {
            throw new IllegalArgumentException(
                this.getClass().getSimpleName() + ".divide(Group<Student>, int, int).pre " 
                + "violated, "
                // With reason:
                + "the klas with current settings is not dividable."
                + "yet " + this.getClass().getSimpleName()
                + "divide(Group<Student>, int, int) was called."
            );
        }
    }

    /* Returns a list where the students are ordered by their bucket */
    private final Queue<Student> orderStudentsByBucket(final Set<Student> klas) {
        
        final Queue<Student> queueOrderByBucket = new LinkedList<>();
        /* Getting the Students ordered by bucket */ {
            for (Type bucket : buckets) {
                for (Student student : klas) {
                    final boolean studentBelongsInThisBucket = (
                        determineBucket.apply(student) == bucket);
                    if (studentBelongsInThisBucket) {
                        queueOrderByBucket.add(student);
                    }
                }
            }
        }

        return queueOrderByBucket;
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
              
        this.validateDivide(klas, groupSize, deviation);
        
        final Queue<Student> queueOrderByBucket = (this.orderStudentsByBucket(klas));
        final List<Group<Student>> createdGroups = new ArrayList<Group<Student>>();
        final List<Integer> groupSizes = (
            this.determineGroupsAndSizes(klas, groupSize, deviation)
        );

        /* Create a `groupSizes.size()` groups */ {
            while (createdGroups.size() < groupSizes.size()) {
                createdGroups.add(new Group<>());
            }
            // should never happen, but just in case:
            assert (createdGroups.size() == groupSizes.size()) : (
                this.getClass().getSimpleName() + ".divide("
                + "divide(Group<Student>, int, int) ran into a problem, an invariant was "
                + "violated: "
                + "createdGroups.size() (" + createdGroups.size() + ") "
                + "was not equal to groupSizes.size() (" + groupSizes.size() + ")."
            ); 
        }

        /* Filling up those groups */ {
            for (int index = 0; (! queueOrderByBucket.isEmpty()); index++) {

                /* Ensuring we keep within the amount of groups */ {
                    final int amountOfGroups = (createdGroups.size());
                    index %= amountOfGroups;
                }
                
                final Group<Student> groupToAddTo = createdGroups.get(index);

                /* Ensuring that we don't fill up a group more then it's supposed to */ {
                    final int sizeOfCreatedGroup = (groupToAddTo.size());
                    final int maximumSizeOfThisCreatedGroup = (groupSizes.get(index));
                    // should never happen, but just in case:
                    assert (sizeOfCreatedGroup <= maximumSizeOfThisCreatedGroup) : (
                        this.getClass().getSimpleName() + ".divide("
                        + "divide(Group<Student>, int, int) ran into a problem, an invariant was "
                        + "violated: "
                        + "sizeOfCreatedGroup (" + sizeOfCreatedGroup + ") was bigger then "
                        + " maximumSizeOfThisCreatedGroup (" + maximumSizeOfThisCreatedGroup + ")"
                        + "which is not supposed to happen.");
                    if (sizeOfCreatedGroup == maximumSizeOfThisCreatedGroup) {
                        continue;
                    }
                }

                /* Adding a student to the group */ {
                    final Student studentToAdd = queueOrderByBucket.poll();
                    assert (studentToAdd != null) : (
                        this.getClass().getSimpleName() + ".divide("
                        + "divide(Group<Student>, int, int) ran into a problem, an invariant was "
                        + "violated: " 
                        + "the queue was not supposed to be empty as the for-loop ran another run "
                        + "however when the time came to poll this object it was missing.");
                    groupToAddTo.add(studentToAdd);
                }
            }
        }

        return new HashSet<Group<Student>>(createdGroups);
    }
}