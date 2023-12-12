import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
         
        /* Pre-Conditions Guard-Statements */ {
            if (klas == null) {
                throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ".divide(Group<Student>, int, int).pre " 
                    + "violated, "
                    + "klas cannot be null."
                );
            }

            if (! this.isDividable(klas, groupSize, deviation)) {
                throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ".divide(Group<Student>, int, int).pre " 
                    + "violated, "
                    + "the klas with current settings is not dividable."
                    + "yet " + this.getClass().getSimpleName()
                    + "divide(Group<Student>, int, int) was called."
                );
            }
        }

        final List<Student> sortedStudents;
        /* Getting a sorted Array of the students in klass */ {
            
            final ArrayList<Student> studentsArrayList = new ArrayList<Student>();
            for (Student student : klas) {
                studentsArrayList.add(student);
            }

            Collections.sort(
                studentsArrayList, 
                (student1, student2) -> student1.sortName().compareTo(student2.sortName())
            );

            sortedStudents = studentsArrayList;
        }

        final Set<Group<Student>> createdGroups;
        /* Getting the groups the students will fall into */ {
            createdGroups = (new HashSet<Group<Student>>());
            List<Integer> groupSizes = (this.determineGroupsAndSizes(klas, groupSize, deviation));
            for (Integer size : groupSizes) {
                final Group<Student> group = (new Group<Student>());
                while (group.size() < size) {
                    final Student student = (sortedStudents.remove(0));
                    group.add(student);
                }
                createdGroups.add(group);
            }
        }

        return createdGroups;
    }
}