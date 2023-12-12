import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ClassDivider.
 *
 * @author Huub de Beer
 */
public class ClassDividerTest {
    protected ClassDivider cd;
    
    public ClassDividerTest() {
        this.cd = new ClassDivider();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // test isDividable
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Test that {@code isDividable} returns false when empty groups would be allowed because
     * {@code deviation >= groupSize}. I.e., that the minimal allowed group size is smaller or equal
     * to zero.
     */
    @Test
    public void testIsDividableEmptyGroupWhenDeviationEqualOrLargerThanGroupSize() {
        Group<Student> klas = createClass(2);
        // minimal group size == 0
        int groupSize = 1;
        int equalDeviation = groupSize;
        assertFalse(cd.isDividable(klas, groupSize, equalDeviation));

        // minimal group size < 0
        int largerDeviation = groupSize + 1;
        assertFalse(cd.isDividable(klas, groupSize, largerDeviation));
    }

    /**
     * Test that {@code isDividable} returns true when {@code klas} can be divided into groups of
     * exactly {@code groupSize} and false when it cannot be divided, given {@code deviation == 0}.
     */
    @Test
    public void testIsDividableOverflowIsZero() {
        final int deviation = 0;

        Group<Student> klas = createClass(1);
        // 1 % 1 == 0
        int groupSize = 1;
        assertTrue(cd.isDividable(klas, groupSize, deviation));

        // Even sized classes
        klas = createClass(2);
        // 2 % 1 == 0
        groupSize = 1;
        assertTrue(cd.isDividable(klas, groupSize, deviation));
        // 2 % 2 == 0
        groupSize = 2;
        assertTrue(cd.isDividable(klas, groupSize, deviation));

        // Odd sized classes
        klas = createClass(3);
        // 3 % 1 == 0
        groupSize = 1;
        assertTrue(cd.isDividable(klas, groupSize, deviation));
        // 3 % 2 != 0
        groupSize = 2;
        assertFalse(cd.isDividable(klas, groupSize, deviation));
        // 3 % 3 == 0
        groupSize = 3;
        assertTrue(cd.isDividable(klas, groupSize, deviation));
    }

    /**
     * Test that {@code isDividable} returns true when the potential, i.e., quotient of
     * {@code klas.size()} divided by {@code groupSize} multiplied by {@code deviation}, is at least
     * equal to the remainder of that division. This means that the overflow of students can be
     * spread over the groups.
     */
    @Test
    public void testIsDividablePotentialAtLeastOverflow() {
        // Zero potential
        int deviation = 0;
        Group<Student> klas = createClass(3);
        int groupSize = 2;
        assertFalse(cd.isDividable(klas, groupSize, deviation));

        // Non-zero potential
        // Overflow == 1
        deviation = 1; // So, potential == 1, overflow == 1
        assertTrue(cd.isDividable(klas, groupSize, deviation));
        klas = createClass(5); // So, potential == 2, overflow == 1 
        assertTrue(cd.isDividable(klas, groupSize, deviation));

        // Overflow > 1; even overflow
        groupSize = 3;
        deviation = 2;
        klas = createClass(11); // So, potential == 4, overflow == 2
        assertTrue(cd.isDividable(klas, groupSize, deviation));

        // Potential == overflow; odd overflow
        groupSize = 6;
        deviation = 5;
        klas = createClass(11); // So, potential == 5, overflow == 5
        assertTrue(cd.isDividable(klas, groupSize, deviation));

        // Potential > overflow; odd and even overflow
        groupSize = 9;
        deviation = 5;
        klas = createClass(11); // So, potential == 5, overflow == 2
        assertTrue(cd.isDividable(klas, groupSize, deviation));
        groupSize = 8;
        deviation = 5;
        klas = createClass(11); // So, potential == 5, overflow == 3
        assertTrue(cd.isDividable(klas, groupSize, deviation));
    }

    /**
     * Test that {@code isDividable} returns true when the potential, i.e., quotient of
     * {@code klas.size()} divided by {@code groupSize} multiplied by {@code deviation}, plus the
     * overflow is at least the minimal group size. This means that the overflow of students can
     * become a separate group, potentially supplemented by students from the other groups.
     */
    @Test
    public void testIsDividableOverflowPlusPotentialAtLeastMinimalGroupSize() {
        // deviation >= 1
        // overflow == 9, minimal group size == 9, potential == 1 
        // ==> potential + overflow >= minimal group size (10 >= 9)
        int deviation = 1;
        int groupSize = 10;
        Group<Student> klas = createClass(19);
        assertTrue(cd.isDividable(klas, groupSize, deviation));
        // potential == 2
        // ==> potential + overflow >= minimal group size (11 >= 9)
        deviation = 2;
        assertTrue(cd.isDividable(klas, groupSize, deviation));
    }

    /**
     * Test that {@code isDividable} returns false when the overflow is in between the potential,
     * i.e., quotient of {@code klas.size()} divided by {@code groupSize} multiplied by
     * {@code deviation}, and the minimal group size minus that potential. This means that the
     * overflow cannot be spread over existing groups nor form a separate group on its own.
     */
    @Test
    public void testIsDividableOverflowInBetweenPotentialAndMinimalGroupSizeMinPotential() {
        int groupSize = 17;
        int deviation = 1;

        // 1 < overflow (=14) < 16; overflow 15 is dividable because 16 = 17 - 1
        Group<Student> klas = createClass(31);
        assertFalse(cd.isDividable(klas, groupSize, deviation));
        // 1 < overflow (=8) < 16
        klas = createClass(25);
        assertFalse(cd.isDividable(klas, groupSize, deviation));
        // 1 < overflow (=2) < 16
        klas = createClass(19);
        assertFalse(cd.isDividable(klas, groupSize, deviation));

        // 2 < overflow (=12) < 14
        klas = createClass(46);
        assertFalse(cd.isDividable(klas, groupSize, deviation));
        // 2 < overflow (=6) < 14
        klas = createClass(40);
        assertFalse(cd.isDividable(klas, groupSize, deviation));
        // 2 < overflow (=3) < 14
        klas = createClass(37);
        assertFalse(cd.isDividable(klas, groupSize, deviation));

        deviation = 2;
        // 4 < overflow (=) < 11
        klas = createClass(43);
        assertFalse(cd.isDividable(klas, groupSize, deviation));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // test divide, common properties
    ///////////////////////////////////////////////////////////////////////////////////////////////
       
    /**
     * Test {@code divide} divides a class of 1 student correctly in just one way.
     */
    @Test
    public void testDivideSingletonClass() {
        Group<Student> klas = createClass(1);
        Set<Group<Student>> groupSet = cd.divide(klas, 1, 0);
        assertEquals(1, groupSet.size());
    }
    
    record DivideTestCase(int classSize, int groupSize, int deviation) {}
    
    List<DivideTestCase> divideTestCases = List.of(
            new DivideTestCase(1, 1, 0),
            
            new DivideTestCase(2, 1, 0),
            new DivideTestCase(2, 2, 0),
            new DivideTestCase(2, 2, 1),
            
            new DivideTestCase(3, 1, 0),
            new DivideTestCase(3, 2, 1),
            new DivideTestCase(3, 3, 0),
            new DivideTestCase(3, 3, 1),
            new DivideTestCase(3, 3, 2),
            
            new DivideTestCase(4, 1, 0),
            new DivideTestCase(4, 2, 0),
            new DivideTestCase(4, 2, 1),
            new DivideTestCase(4, 3, 1),
            new DivideTestCase(4, 3, 2),
            new DivideTestCase(4, 4, 0),
            new DivideTestCase(4, 4, 1),
            new DivideTestCase(4, 4, 2),
            new DivideTestCase(4, 4, 3),
            
            new DivideTestCase(5, 1, 0),
            new DivideTestCase(5, 2, 1),
            new DivideTestCase(5, 3, 1),
            new DivideTestCase(5, 3, 2),
            new DivideTestCase(5, 4, 1),
            new DivideTestCase(5, 4, 2),
            new DivideTestCase(5, 4, 3),
            new DivideTestCase(5, 5, 0),
            new DivideTestCase(5, 5, 1),
            new DivideTestCase(5, 5, 2),
            new DivideTestCase(5, 5, 3),
            new DivideTestCase(5, 5, 4),
            
            new DivideTestCase(19, 10, 1),
            new DivideTestCase(19, 10, 2)
    );
    
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that all students
     * are in exactly one group.
     */
    @Test
    public void testDivideStudentsInExactlyOneGroup() {
        for (DivideTestCase t: divideTestCases) {
            Group<Student> klas = createClass(t.classSize);
            assertStudentsInExactlyOneGroup(klas, cd.divide(klas, t.groupSize(), t.deviation()));
        }
    }
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that all resulting groups have a
     * valid size.
     */
    @Test
    public void testDivideAllGroupsHaveAValidSize() {
        for (DivideTestCase t: divideTestCases) {
            Group<Student> klas = createClass(t.classSize);
            assertAllGroupsHaveValidSize(
                    cd.divide(klas, t.groupSize(), t.deviation()), 
                    t.groupSize(),
                    t.deviation()
            );                    
        }
    }
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that the number of students in the 
     * class equals to the number of students in all groups combined.
     */
    @Test
    public void testDivideNumberOfStudentsEqual() {
        for (DivideTestCase t: divideTestCases) {
            Group<Student> klas = createClass(t.classSize);
            assertGroupSetStudentsNumberIsClassSize(
                    klas, 
                    cd.divide(klas, t.groupSize(), t.deviation())
            );                    
        }
    }
    
    /**
     * Test {@code divide} doesn't change {@code klas} or the students therein.
     */
    @Test
    public void testDivideDoesntChangeClass() {
        for (DivideTestCase t: divideTestCases) {
            Group<Student> klas = createClass(t.classSize);
            Group<Student> copy = copyClass(klas);            
            cd.divide(klas, t.groupSize(), t.deviation());
            assertTrue(klas.containsAll(copy));
            assertTrue(copy.containsAll(klas));
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // test determineGroupsAndSizes
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static final int MAX_CLASS_SIZE_TO_TEST = 100;

    /**
     * Test that method {@link ClassDivider#determineGroupsAndSizes} generates the right amount of
     * groups, with valid sizes. It tries to divide classes up to 100 students into all possible
     * combinations of groups and deviations. This isn't a good test to find specific defects, but
     * it is a convenient way to see the method in action, particularly when you uncomment the print
     * statements. Then all combinations are logged to the terminal.
     */
    @Test
    public void testDetermineGroupsAndSizes() {
        // Varying class sizes
        for (int classSize = 1; classSize <= MAX_CLASS_SIZE_TO_TEST; classSize++) {
            Group<Student> klas = createClass(classSize);

            // Varying group sizes
            for (int groupSize = 1; groupSize <= MAX_CLASS_SIZE_TO_TEST; groupSize++) {
                int numberOfGroups = klas.size() / groupSize;

                // Varying deviation sizes
                for (int deviation = 0; deviation < groupSize; deviation++) {
                    /* 
                    System.out.print("Divide class of %d in groups of %d±%d students: "
                           .formatted(classSize, groupSize, deviation));
                    */

                    if (!cd.isDividable(klas, groupSize, deviation)) {
                        // System.out.println(" UNDIVIDABLE");
                    } else {
                        List<Integer> sizes
                                = cd.determineGroupsAndSizes(klas, groupSize, deviation);

                        // System.out.println(sizes);
                        // Right amount of groups are generated
                        assertTrue(numberOfGroups <= sizes.size());
                        assertTrue(sizes.size() <= numberOfGroups + 1);

                        // The number of students in all groups combined is equal to the class size
                        assertEquals(klas.size(),
                                sizes.stream().reduce(0, (n, m) -> n + m));

                        // And each group has size groupSize +/- deviation
                        for (int size : sizes) {
                            assertTrue(groupSize - deviation <= size);
                            assertTrue(size <= groupSize + deviation);
                        }
                    }
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
        
    /**
     * Create a class of {@code classSize} students.
     *
     * @param classSize number of students in the class
     * @return generated class
     */
    protected Group<Student> createClass(int classSize) {
        Group<Student> klas = new Group<>();
        for (int i = 0; i < classSize; i++) {
            klas.add(new Student("", "", Integer.toString(i), "", Gender.NOT_REPORTED));
        }
        return klas;
    }
        
    private Group<Student> copyClass(Group<Student> klas) {
        Group<Student> copy = new Group<>();
        for (Student s: klas) {
            copy.add(new Student(
                    s.firstName(),
                    s.lastName(),
                    s.id(),
                    s.country(),
                    s.gender()
            ));
        }
        return copy;
    }
    
    private void assertStudentsInExactlyOneGroup(Group<Student> klas, Set<Group<Student>> groups) {
        for (Student s: klas) {
            int count = 0;
            for (Group<Student> group: groups) {
                if (group.contains(s)) {
                    count++;
                }
            }
            
            assertEquals(1, count);
        }
    }

    private void assertAllGroupsHaveValidSize(
            Set<Group<Student>> groupSet, int groupSize, int deviation
    ) {
        for (Group<Student> group : groupSet) {
            assertTrue(group.size() <= groupSize + deviation);
            assertTrue(group.size() >= groupSize - deviation);
        }
    }

    private void assertGroupSetStudentsNumberIsClassSize(
            Group<Student> klas,
            Set<Group<Student>> groupSet
    ) {
        int studentsInGroupSetCount = groupSet.stream().map(Group::size).reduce(0, Integer::sum);
        assertEquals(klas.size(), studentsInGroupSetCount);
    }
}
