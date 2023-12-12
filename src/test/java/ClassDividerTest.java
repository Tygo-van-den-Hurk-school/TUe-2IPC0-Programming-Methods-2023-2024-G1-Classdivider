import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ClassDivider.
 *
 * @author Huub de Beer
 */
public abstract class ClassDividerTest {
    
    /** the instance of {@code ClassDivider} to test. */
    protected ClassDivider cd;
    
    /**
     * Constructs a new {@code ClassDividerTest}.
     */
    public ClassDividerTest() {

        /* Generating the class for for all sizes */ { // speeds up the caching.
            final Group<Student> klas = this.createClass(ClassDividerTest.MAX_KLAS_SIZE_TO_TEST);
            while (klas.size() < ClassDividerTest.MAX_KLAS_SIZE_TO_TEST) {
                klas.add(this.generateRandomStudent(klas.size()));
            }

            while (klas.size() > 0) {
                ClassDividerTest.DYNAMIC_PROGRAMMING_CACHE.put(
                    klas.size(), this.copyClass(klas));
                klas.remove(klas.iterator().next());
            }
        }

        /* Creating test cases */ {
            final Integer maxClassSizeToTest = (ClassDividerTest.MAX_KLAS_SIZE_TO_TEST);
            for (int classSize = 1; classSize <= maxClassSizeToTest; classSize++) {

                final Integer maxGroupSize = (maxClassSizeToTest);
                for (int groupSize = 1; groupSize <= maxGroupSize; groupSize++) {

                    final Integer maxDeviation = (maxClassSizeToTest);
                    for (int deviation = 0; deviation < maxDeviation; deviation++) {

                        final int maximumValue = (20000); // explanation for a constant
                        final int minimalValue = (maximumValue - 1); // explanation for a constant
                        /*
                         * Chances are now 1/20000 that a test case will be added.
                         * Considering that there are:
                         * 
                         * 100 x 100 x 100 = 1.000.000 test cases, 
                         * 
                         * this means that there are now 50 test cases on average.
                         */
                        //
                        final int actualValue = (ClassDividerTest.RANDOM.nextInt(maximumValue));
                        final Boolean testCaseWillBeAdded = (actualValue >= minimalValue);
                        if (testCaseWillBeAdded) {
                            this.divideTestCases.add(
                                new DivideTestCase(classSize, groupSize, deviation)
                            );
                        }
                    }
                }
            }

            System.out.println(
                "Created " + this.divideTestCases.size() + " test cases for "
                + this.getClass().getSimpleName() + "."
            );
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~ Tests for ClassDivider.isDivide(Set<Students>) ~~~~~~~~~~~~~~~~~~~~~//

    /**
     * Test that {@code isDividable} returns false when empty groups would be allowed because
     * {@code deviation >= groupSize}. I.e., that the minimal allowed group size is smaller or equal
     * to zero.
     */
    @Test
    public void testIsDividableEmptyGroupWhenDeviationEqualOrLargerThanGroupSize() {

        // minimal group size == 0
        final Group<Student> klas = this.createClass(2);
        final int groupSize = 1; 
        final int equalDeviation = groupSize;
        assertFalse(this.cd.isDividable(klas, groupSize, equalDeviation));

        final int largerDeviation = groupSize + 1;
        assertFalse(this.cd.isDividable(klas, groupSize, largerDeviation));
    }

    /**
     * Test that {@code isDividable} returns true when {@code klas} can be divided into groups of
     * exactly {@code groupSize} and false when it cannot be divided, given {@code deviation == 0}.
     */
    @Test
    public void testIsDividableOverflowIsZero() {
        
        final int deviation = 0;

        Group<Student> klas = this.createClass(1);
        
        // 1 % 1 == 0
        int groupSize = 1;
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));

        /* Even sized classes */ {
            klas = this.createClass(2);
            
            // 2 % 1 == 0
            groupSize = 1;
            assertTrue(this.cd.isDividable(klas, groupSize, deviation));
            
            // 2 % 2 == 0
            groupSize = 2;
            assertTrue(this.cd.isDividable(klas, groupSize, deviation));
        }

        /* Odd sized classes */ {
            klas = this.createClass(3);
            
            // 3 % 1 == 0
            groupSize = 1;
            assertTrue(this.cd.isDividable(klas, groupSize, deviation));
            
            // 3 % 2 != 0
            groupSize = 2;
            assertFalse(this.cd.isDividable(klas, groupSize, deviation));
            
            // 3 % 3 == 0
            groupSize = 3;
            assertTrue(this.cd.isDividable(klas, groupSize, deviation));
        }
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
        Group<Student> klas = this.createClass(3);
        int groupSize = 2;
        
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));

        // Non-zero potential
        // Overflow == 1
        deviation = 1; // So, potential == 1, overflow == 1
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));
        
        klas = this.createClass(5); // So, potential == 2, overflow == 1 
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));

        // Overflow > 1; even overflow
        groupSize = 3;
        deviation = 2;
        klas = this.createClass(11); // So, potential == 4, overflow == 2
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));

        // Potential == overflow; odd overflow
        groupSize = 6;
        deviation = 5;
        klas = this.createClass(11); // So, potential == 5, overflow == 5
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));

        // Potential > overflow; odd and even overflow
        groupSize = 9;
        deviation = 5;
        klas = this.createClass(11); // So, potential == 5, overflow == 2
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));
        
        groupSize = 8;
        deviation = 5;
        klas = this.createClass(11); // So, potential == 5, overflow == 3
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));
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
        Group<Student> klas = this.createClass(19);
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));
        
        // potential == 2
        // ==> potential + overflow >= minimal group size (11 >= 9)
        deviation = 2;
        assertTrue(this.cd.isDividable(klas, groupSize, deviation));
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
        Group<Student> klas = this.createClass(31);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));
        
        // 1 < overflow (=8) < 16
        klas = this.createClass(25);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));
        
        // 1 < overflow (=2) < 16
        klas = this.createClass(19);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));

        // 2 < overflow (=12) < 14
        klas = this.createClass(46);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));
        
        // 2 < overflow (=6) < 14
        klas = this.createClass(40);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));
        
        // 2 < overflow (=3) < 14
        klas = this.createClass(37);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));

        deviation = 2;
        
        // 4 < overflow (=) < 11
        klas = this.createClass(43);
        assertFalse(this.cd.isDividable(klas, groupSize, deviation));
    }

    //~~~~~~~~~~~~~ Tests for ClassDivider.divide(Set<Students>)'s common properties ~~~~~~~~~~~~//
    
    /**
     * Test {@code divide} divides a class of 1 student correctly in just one way.
     */
    @Test
    public void testDivideSingletonClass() {
        
        final int classSize = (1);
        final int groupSize = (1);
        final int deviation = (0);

        final int amountOfGroups = (1);

        final Group<Student> klas = this.createClass(classSize);
        final Set<Group<Student>> groupSet = this.cd.divide(klas, groupSize, deviation);

        assertEquals(amountOfGroups, groupSet.size());
    }
    
    /** 
     * A testcase for {@link ClassDivider#divide(Group, int, int)} that stores 
     * the parameters that need to be provided. 
     */
    record DivideTestCase(int classSize, int groupSize, int deviation) {}
    
    /** Contains test cases for {@code divide}. */
    List<DivideTestCase> divideTestCases = new ArrayList<>();
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that all students
     * are in exactly one group.
     */
    @Test
    public void testDivideStudentsInExactlyOneGroup() {
        
        for (DivideTestCase divideTestCase : this.divideTestCases) {
            
            final Group<Student> klas = this.createClass(divideTestCase.classSize);
            
            if (!this.cd.isDividable(klas, divideTestCase.groupSize, divideTestCase.deviation)) {
                continue;
            }

            assertStudentsInExactlyOneGroup(
                klas, 
                this.cd.divide(klas, divideTestCase.groupSize(), divideTestCase.deviation())
            );
        }
    }
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that all resulting groups have a
     * valid size.
     */
    @Test
    public void testDivideAllGroupsHaveAValidSize() {
        
        for (DivideTestCase divideTestCase : this.divideTestCases) {
            
            final Group<Student> klas = this.createClass(divideTestCase.classSize);
            
            if (!this.cd.isDividable(klas, divideTestCase.groupSize, divideTestCase.deviation)) {
                continue;
            }

            assertAllGroupsHaveValidSize(
                    this.cd.divide(klas, divideTestCase.groupSize(), divideTestCase.deviation()), 
                    divideTestCase.groupSize(),
                    divideTestCase.deviation()
            );                    
        }
    }
    
    /**
     * Test {@code divide} divides {@code klas} in such a way that the number of students in the 
     * class equals to the number of students in all groups combined.
     */
    @Test
    public void testDivideNumberOfStudentsEqual() {
        
        for (DivideTestCase divideTestCase : this.divideTestCases) {
            
            final Group<Student> klas = this.createClass(divideTestCase.classSize);
        
            if (!this.cd.isDividable(klas, divideTestCase.groupSize, divideTestCase.deviation)) {
                continue;
            }

            assertGroupSetStudentsNumberIsClassSize(
                    klas, 
                    this.cd.divide(klas, divideTestCase.groupSize(), divideTestCase.deviation())
            );                    
        }
    }
    
    /**
     * Test {@code divide} doesn't change {@code klas} or the students therein.
     */
    @Test
    public void testDivideDoesNotChangeClass() {
        
        for (DivideTestCase divideTestCase : this.divideTestCases) {
            
            final Group<Student> klas = this.createClass(divideTestCase.classSize);
            final Group<Student> copy = copyClass(klas);            
            
            if (!this.cd.isDividable(klas, divideTestCase.groupSize, divideTestCase.deviation)) {
                continue;
            }

            this.cd.divide(klas, divideTestCase.groupSize(), divideTestCase.deviation());
            
            assertTrue(klas.containsAll(copy));
            assertTrue(copy.containsAll(klas));
        }
    }
    
    //~~~~~~~~~ Tests for ClassDivider.determineGroupsAndSizes(Group<Student>, int, int) ~~~~~~~~//

    private static final int MAX_KLAS_SIZE_TO_TEST = 100;

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
        for (int classSize = 1; classSize <= MAX_KLAS_SIZE_TO_TEST; classSize++) {

            // Varying group sizes
            final Group<Student> klas = this.createClass(classSize);
            for (int groupSize = 1; groupSize <= MAX_KLAS_SIZE_TO_TEST; groupSize++) {

                // Varying deviation sizes
                final int numberOfGroups = klas.size() / groupSize;
                for (int deviation = 0; deviation < groupSize; deviation++) {
                    /* 
                    System.out.print("Divide class of %d in groups of %d±%d students: "
                           .formatted(classSize, groupSize, deviation));
                    */

                    if (!this.cd.isDividable(klas, groupSize, deviation)) {
                        continue;
                    }
                    
                    final List<Integer> sizes = (
                        this.cd.determineGroupsAndSizes(klas, groupSize, deviation)
                    );

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
    
    //~ HelperMethods for Testing ClassDivider.determineGroupsAndSizes(Group<Student>, int,int) ~//
      
    /**
     * Create a class of {@code classSize} students.
     *
     * @param classSize number of students in the class
     * @return generated class
     * @throws IllegalArgumentException if {@code classSize} is negative.
     */
    protected final Group<Student> createClass(
            final int classSize
    ) throws IllegalArgumentException {
       
        /* Checking Pre-Conditions */ {
            if (classSize < 0) {
                throw new IllegalArgumentException(
                    this.getClass().getSimpleName() + ".this.createClass(int).pre violated, "
                    + "requires a non-negative classSize, "
                    + "but the classSize requested was: " + classSize + "."
                );
            }
        }

        if (classSize == 0) {
            return new Group<>();
        }

        /* Checking if this has been computed before */ {
            final Map<Integer, Group<Student>> cache = ClassDividerTest.DYNAMIC_PROGRAMMING_CACHE;
            final Group<Student> cacheEntry = cache.get(classSize);
            final Boolean cacheEntryExists = (cacheEntry != null);
            if (cacheEntryExists) {
                return cacheEntry;
            }
        }

        /* Make a copy of the smaller class and add the student */ {
            final Group<Student> recursivelyComputedClass = this.createClass(classSize - 1);
            final Group<Student> copyOfRecursivelyComputedClass = this.copyClass(
                recursivelyComputedClass);
            copyOfRecursivelyComputedClass.add(this.generateRandomStudent(classSize));
            return copyOfRecursivelyComputedClass;
        }
    }
        
    private final Group<Student> copyClass(final Group<Student> klas) {
        
        final Group<Student> copy = new Group<>();
        for (Student student : klas) {

            final Student studentCopy = new Student(
                student.firstName(),
                student.lastName(),
                student.id(),
                student.country(),
                student.gender());
            copy.add(studentCopy);
        }

        return copy;
    }
    
    private final void assertStudentsInExactlyOneGroup(
            final Group<Student> klas, 
            final Set<Group<Student>> groups
    ) {
        
        for (Student student: klas) {
        
            int count = 0;
            for (Group<Student> group: groups) {
                if (group.contains(student)) {
                    count++;
                }
            }
            
            assertEquals(1, count);
        }
    }

    private final void assertAllGroupsHaveValidSize(
            final Set<Group<Student>> groupSet, 
            final int groupSize, 
            final int deviation
    ) {
        for (Group<Student> group : groupSet) {
            assertTrue(group.size() <= groupSize + deviation);
            assertTrue(group.size() >= groupSize - deviation);
        }
    }

    private final void assertGroupSetStudentsNumberIsClassSize(
            final Group<Student> klas,
            final Set<Group<Student>> groupSet
    ) {
        final int studentsInGroupSetCount = (
            groupSet.stream().map(Group::size).reduce(0, Integer::sum));
        assertEquals(klas.size(), studentsInGroupSetCount);
    }

    //~~~~~~~~~~~~~~~~~~~~~ Variables I added for generating Random Students ~~~~~~~~~~~~~~~~~~~~//

    /** Stores previously created classes to speed up class creation. */
    public static final Map<Integer, Group<Student>> DYNAMIC_PROGRAMMING_CACHE = new HashMap<>();

    /** Contains a BUNCH of random first names. */
    private static final String[] POSSIBLE_FIRST_NAMES = {
        "Emily", "Jacob", "Olivia", "Ethan", "Sophia", "Liam", "Ava", "Jackson", "Emma", "Aiden",
        "Isabella", "Lucas", "Mia", "Logan", "Ella", "Noah", "Grace", "Mason", "Lily", "Caden",
        "Harper", "Oliver", "Abigail", "Caleb", "Chloe", "Samuel", "Addison", "Elijah", "Natalie", 
        "Landon", "Brooklyn", "Alexander", "Scarlett", "Daniel", "Zoey", "Michael", "Leah", 
        "Carter", "Grace", "Benjamin", "Avery", "Henry", "Sofia", "Owen", "Riley", "Wyatt", 
        "Aria", "Julian", "Layla", "Matthew", "Zoey", "Jack", "Aubrey", "Leo", "Hannah", "Jayden",
        "Stella", "Gabriel", "Aurora", "Zachary", "Ellie", "Luke", "Peyton", "Anthony", "Nora", 
        "David", "Savannah", "Isaac", "Zoey", "Christopher", "Bella", "Andrew", "Violet", 
        "Nathan", "Claire", "Christian", "Penelope", "Jackson", "Lucy", "Joseph", "Hazel", 
        "Caleb", "Aurora", "Jonathan", "Mila", "Samuel", "Anna", "Benjamin", "Scarlett", 
        "Hunter", "Aaliyah", "Evan", "Ellie", "Nicholas", "Kennedy", "Ryan", "Paisley", "Gavin"};
    /** Contains a BUNCH of random last names. */
    private static final String[] POSSIBLE_LAST_NAMES = {
        "Van den Berg", "Jansen", "De Vries", "Van der Meer", "Bakker", "Visser", "Smit", 
        "Mulder", "Boer", "Van Dijk", "Hoekstra", "Kok", "Kuijpers", "Scholten", "Hendriks",
        "Jacobs", "Willems", "Vermeer", "Bos", "Vos", "Gerritsen", "Koopman", "Molenaar", 
        "Van der Linden", "Wouters", "Peters", "Van der Heijden", "Meijer", "Van Beek", 
        "Van der Wal", "Kramer", "Bosch", "Van Leeuwen", "Van de Ven", "Sanders", "Hofman", 
        "Van Rijn", "Dekker", "Van der Velde", "Veenstra", "Van Houten", "Van der Laan", 
        "Huisman", "Timmermans", "Van der Schoot", "Van Loon", "Dijkstra", "Schouten", 
        "Van der Steen", "Martens", "Collins", "Stewart", "Sanchez", "Morris", "Rogers", 
        "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey", "Rivera", "Cooper", "Richardson",
        "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez", "James", "Watson", 
        "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross", "Henderson",
        "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores", 
        "Washington", "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander",
        "Russell", "Griffin", "Diaz", "Hayes"};
    /** The two nationalities we'll be considering. */
    private static final String[] POSSIBLE_COUNTRIES = {
        "NL", "US", "BE", "DE", "FR", "UK", "ES", "IT", "CA", "JP", "RU", };
    /** All possible Genders. */
    private static final Gender[] POSSIBLE_GENDERS = Gender.values();

    /** A random generator based on the system time. */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methods I added ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
    /**
     * Generates a random student.
     * With random first, and last name, gender, nationality.
     * 
     * @param id the id of the student.
     * @return a student with random attributes.
     */
    protected Student generateRandomStudent(final Integer id) {

        final String firstName;
        /* Getting a random first name */ {
            final Integer possibleFirstNamesLength = (ClassDividerTest.POSSIBLE_FIRST_NAMES.length);
            final Integer randomIndex = (ClassDividerTest.RANDOM.nextInt(possibleFirstNamesLength));
            final String randomFirstName = (ClassDividerTest.POSSIBLE_FIRST_NAMES[randomIndex]);
            firstName = randomFirstName;
        }

        final String lastName; 
        /* Getting a random first name */ {
            final Integer possibleLastNamesLength = (ClassDividerTest.POSSIBLE_LAST_NAMES.length);
            final Integer randomIndex = (ClassDividerTest.RANDOM.nextInt(possibleLastNamesLength));
            final String randomLastName = (ClassDividerTest.POSSIBLE_LAST_NAMES[randomIndex]);
            lastName = randomLastName;
        }

        final String idString = (id.toString());

        final String country;
        /* Getting a random Country */ {
            final Integer possibleCountriesLength = (ClassDividerTest.POSSIBLE_COUNTRIES.length);
            final Integer randomIndex = (ClassDividerTest.RANDOM.nextInt(possibleCountriesLength));
            final String randomCountry = (ClassDividerTest.POSSIBLE_COUNTRIES[randomIndex]);
            country = randomCountry;
        }

        final Gender gender;
        /* Getting a random Gender */ {
            final Integer possibleGendersLength = (ClassDividerTest.POSSIBLE_GENDERS.length);
            final Integer randomIndex = (ClassDividerTest.RANDOM.nextInt(possibleGendersLength));
            final Gender randomGender = (ClassDividerTest.POSSIBLE_GENDERS[randomIndex]);
            gender = randomGender;
        }

        return new Student(firstName, lastName, idString, country, gender);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Methods I added to test robustness ~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testIsDividableThrowsExceptionWhenClassIsNull() {

        final Group<Student> klas = (null);
        final int groupSize = (0);
        final int deviation = (0);

        assertThrows(
            NullPointerException.class,
            () -> this.cd.isDividable(klas, groupSize, deviation)
        );
    }

    @Test
    public void testDividableThrowsExceptionWhenGroupSizeIsZero() {
        
        final Group<Student> klas = this.createClass(1);
        final int groupSize = (2);
        final int deviation = (0);

        assert (! this.cd.isDividable(klas, groupSize, deviation)) : "made a mistake in the test";

        assertThrows(
            IllegalArgumentException.class,
            () -> this.cd.divide(klas, groupSize, deviation)
        );
    }
}
