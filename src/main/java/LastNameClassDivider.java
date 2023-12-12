import java.util.Set;

/**
 * Divides {@code Student}s into {@code Group}s based on their last name.
 * 
 * @author Tygo van den Hurk
 * @version 1.0
 * @see ClassDivider
 * @see Student
 * @see Group
 */
public class LastNameClassDivider extends ClassDivider {

    /**
     * Creates a new {@code LastNameClassDivider} object.
     */
    public LastNameClassDivider() {
        super();
    }

    /**
     * Divide {@code klas} into groups of size {@code groupSize} +/- {@code deviation} based on 
     * their last name.
     *
     * @param klas class of students
     * @param groupSize target group size
     * @param deviation allowed number of students more or less in a group
     * 
     * @pre {@code isDividable(klas, groupSize, deviation)}
     * 
     * @return group set
     * 
     * @post <ul>
     *  <li>All groups have the right size: {@code (\forall g; \result.contains(g);
     *    between(g.size(), groupSize, deviation))}<br>
     *    where {@code between(n, b, d) == b - d <= n && n <= b + d}</li>
     *  <li>All students are in exactly one group:<br>
     *    {@code (\forall s; 
     *        klas.contains(s); 
     *        (\num_of g; \result.contains(g); g.contains(s)) == 1
     *    )}
     *  </li>
     *  <li>The number of students in all groups equals the number of students in the class:<br>
     * {@code klas.size() == (\sum g; \result.contains(g); g.size)}
     *  </li>
     *  <li>The class of students isn't changed: {@code klas == \old(klas)}</li>
     * </ul>
     */
    @Override
    public Set<Group<Student>> divide(
        final Group<Student> klas,
        final int groupSize, 
        final int deviation
    ) throws IllegalArgumentException {
    }
}