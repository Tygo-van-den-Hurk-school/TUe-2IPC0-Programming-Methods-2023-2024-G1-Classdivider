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
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class divider to randomly divide a class of students into groups.
 *
 * @author Huub de Beer
 */
public abstract class ClassDivider {

    /**
     * Determine if {@code klas} is dividable into groups of size {@code groupSize} +/-
     * {@code deviation}.
     *
     * @param klas class of students
     * @param groupSize target group size
     * @param deviation allowed number of students more or less in a group
     *
     * @return true if this class is dividable into groups of {@code groupSize} +/-
     * {@code deviation}, false otherwise. An empty class is not dividable.
     *
     * @pre {@code groupSize > 0 && deviation >= 0 && klas != null && !klas.isEmpty()}
     *
     * @post {@code \result == (klas != null) 
     *  && noEmptyGroupsPossible 
     *  && ((potential >= overflow)
     *      || (overflow + potential >= minimalGroupSize))
     * }<br>
     * where:<ul>
     * <li>{@code minimalGroupSize == groupSize - deviation}</li>
     * <li>{@code noEmptyGroupsPossible == minimalGroupSize > 0}</li>
     * <li>{@code overflow == klas.size() % groupSize}</li>
     * <li>{@code potential == deviation * (klas.size() / groupSize)}</li>
     * </ul>
     * 
     * @throws NullPointerException when {@code klas == null}
     */
    public boolean isDividable(
        final Group<Student> klas, 
        final int groupSize, 
        final int deviation
    ) throws NullPointerException {

        /* Checking pre conditions */ {
            if (klas == null) {
                throw new NullPointerException(
                    this.getClass().getSimpleName() + ".isDividable(Group<Student>, int, int).pre "
                    + "violated, klas was null, klas is not allowed to be null."
                );
            }
        }


        final int minimalGroupSize = (groupSize - deviation);

        if (minimalGroupSize <= 0) {
            return false;
        }

        final int overflow = (klas.size() % groupSize);
        final int nrOfGroups = (klas.size() / groupSize);
        final int potential = (deviation * nrOfGroups);

        final boolean overflowCanBeSpreadOverGroups = (potential >= overflow);
        final boolean overflowCanBeASeparateGroup = (overflow + potential >= minimalGroupSize);

        return (overflowCanBeSpreadOverGroups || overflowCanBeASeparateGroup);
    }

    /**
     * Divide {@code klas} into groups of size {@code groupSize} +/- {@code deviation}.
     *
     * @param klas class of students
     * @param groupSize target group size
     * @param deviation allowed number of students more or less in a group
     *
     * @return group set
     *
     * @pre {@code isDividable(klas, groupSize, deviation)}
     *
     * @post <ul>
     * <li>All groups have the right size: {@code (\forall g; \result.contains(g);
     *   between(g.size(), groupSize, deviation))}<br>
     * where {@code between(n, b, d) == b - d <= n && n <= b + d}</li>
     * <li>All students are in exactly one group:<br>
     * {@code (\forall s; klas.contains(s); (\num_of g; \result.contains(g); g.contains(s)) == 1)}
     * </li>
     * <li>The number of students in all groups equals the number of students in the class:<br>
     * {@code klas.size() == (\sum g; \result.contains(g); g.size)}
     * </li>
     * <li>The class of students isn't changed: {@code klas == \old(klas)}</li>
     * </ul>
     * 
     * @throws IllegalArgumentException when {@code !isDividable(klas, groupSize, deviation)}
     */
    public abstract Set<Group<Student>> divide(
        final Group<Student> klas, 
        final int groupSize, 
        final int deviation
    ) throws IllegalArgumentException;

    /**
     * Determine the number of groups and their sizes when dividing {@code klas} into groups of
     * {@code groupSize} +/- {@code deviations} students.
     *
     * @param klas class to divide
     * @param groupSize target group size
     * @param deviation allowed number of students more or less in a group
     *
     * @return list of group sizes; the size of this list is the number of groups.
     *
     * @pre {@code isDividable(klas, groupSize, deviation)}
     *
     * @post <ul>
     * <li>{@code klas.size() / groupSize <= \result.size() <= klas.size() / groupSize + 1}</li>
     * <li>{@code klas.size() == (\sum size; \result.contains(size); size)}</li>
     * <li>{@code (\forall size; \result.contains(size);
     *                  groupSize - deviation <= size <= groupSize + deviation)}</li>
     * </ul>
     */
    protected List<Integer> determineGroupsAndSizes(
        final Group<Student> klas,
        final int groupSize,
        final int deviation
    ) {
        final int overflow = klas.size() % groupSize;
        final int nrOfGroups = klas.size() / groupSize;

        final int potential = deviation * nrOfGroups;

        final List<Integer> listOfSizes = (Collections.nCopies(nrOfGroups, groupSize));
        final ArrayList<Integer> sizes = (new ArrayList<Integer>(listOfSizes));

        if (overflow == 0) {
            return sizes;
        } else if (overflow > potential) {
            return (this.addOverflowAsExtraGroup(deviation, overflow, sizes));
        } else {
            return (this.spreadOverflowOverGroups(deviation, overflow, sizes));
        }
    }

    /** creates a new group from the overflow. */
    private final List<Integer> addOverflowAsExtraGroup(
            final int deviation, 
            final int overflow, 
            final ArrayList<Integer> sizes
    ) {

        int extraGroupSize = overflow;

        // If sizes is empty, the overflow will be the single extra group.
        if (!sizes.isEmpty()) {
            final int groupSize = sizes.get(0);
            final int minimalGroupSize = groupSize - deviation;

            for (int d = 0; d < deviation && extraGroupSize < minimalGroupSize; d++) {
                int g = sizes.size();

                while (g > 0 && extraGroupSize < minimalGroupSize) {
                    g--;
                    sizes.set(g, sizes.get(g) - 1);
                    extraGroupSize++;
                }
            }
        }

        sizes.add(extraGroupSize);

        return sizes;
    }

    /** spreads the overflow over different groups. */
    private final List<Integer> spreadOverflowOverGroups(
            final int deviation, 
            final int overflow, 
            final ArrayList<Integer> sizes
    ) {

        int toSpread = overflow;
        for (int d = 0; d < deviation && toSpread > 0; d++) {
            for (int g = 0; g < sizes.size() && toSpread > 0; g++) {
                sizes.set(g, sizes.get(g) + 1);
                toSpread--;
            }
        }

        return sizes;
    }
}
