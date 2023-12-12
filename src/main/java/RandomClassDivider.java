/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Divides {@code Student}s into {@code Group}s randomly.
 * 
 * @author Tygo van den Hurk
 * @version 1.0
 * 
 * @see ClassDivider
 * @see Student
 * @see Group
 */
public class RandomClassDivider extends ClassDivider {
    
    @Override
    public Set<Group<Student>> divide(
        final Group<Student> klas, 
        final int groupSize, 
        final int deviation
    ) throws IllegalArgumentException {

        /* Checking pre conditions */ {
            if (! this.isDividable(klas, groupSize, deviation)) {
                throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ".divide(Group<Student>, int, int).pre "
                    + "violated, "
                    + "klas (of size " + klas.size() + ") was not dividable into groups of size "
                    + "groupSize (" + groupSize + ") +/- deviation (" + deviation + ")."
                );
            }
        }

        final Set<Group<Student>> groupSet = (new HashSet<>());
        final Iterator<Student> students = (klas.iterator());
        final List<Integer> groupSizes = (this.determineGroupsAndSizes(
            klas, groupSize, deviation
        ));

        for (int size : groupSizes) {
            final Group<Student> group = new Group<>();

            for (int s = 0; s < size; s++) {
                group.add(students.next());
            }

            groupSet.add(group);
        }

        return groupSet;
    }
}
