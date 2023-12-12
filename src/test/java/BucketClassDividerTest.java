
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for {@link BucketClassDivider} with {@link Country} as the bucket.
 * 
 * @see BucketClassDivider
 * @see RandomClassDividerTest
 */
public class BucketClassDividerTest extends ClassDividerTest {

    /** 
     * Stores wether we are testing all these tests based on the gender
     * bucket or on nationality. 
     */
    public static final Boolean LETS_TEST_BASED_ON_GENDER = (true);
  
    /**
     * Creates a new {@code BucketClassDividerOnCountryTest} instance.
     */
    public BucketClassDividerTest() {
        super();

        /* Let's test either Gender or on Country */ { 
            // TO DO: figure out how to test both within the same class
            if (BucketClassDividerTest.LETS_TEST_BASED_ON_GENDER) {
                this.cd = new BucketClassDivider<Gender>(
                    List.of(Gender.values()), 
                    Student::gender
                );
            } else {
                this.cd = new BucketClassDivider<Boolean>(
                    List.of(true, false), 
                    Student::isDutch
                );
            }
        }
    }

    /**
     * Checks if {@link BucketClassDivider#divide(Group, int, int)} distributes students correctly.
     */
    @Test
    public void checkDistributionOfStudents() {
        if (BucketClassDividerTest.LETS_TEST_BASED_ON_GENDER) {
            this.checkDistributionOfStudentsUsingTheirGender();
        } else {
            this.checkDistributionOfStudentsUsingTheirCountry();
        }
    }

    /** Checks if the students were distributed fairly based on their {@link Gender}. */
    private void checkDistributionOfStudentsUsingTheirGender() {
        
        final int noStudentsSoFar = (0); // Explanation for a constant
        final int thisNewlyFoundStudent = (1); // Explanation for a constant
        final int maxAllowedDifference = (1); // Explanation for a constant

        System.out.println(
            "Testing "
            + this.getClass().getSimpleName() 
            + ".checkDistributionOfStudentsUsingTheirGender():"
        );

        int amountOfTestCases = (this.divideTestCases.size());
        int testCasesTested = (0);
        for (DivideTestCase divideTestCase : this.divideTestCases) {
           
            /* Printing a progress message */ {
                final String percentage = ((testCasesTested / amountOfTestCases) + "%");
                System.out.println(
                    "\rProgress done: " + testCasesTested + "out of" + amountOfTestCases + ", "
                    + "which is " + percentage + "."
                );
            }

            final Set<Group<Student>> createdGroups;
            /* Getting the divided class if it is devisable otherwise return */ {
                final Integer classSize = (divideTestCase.classSize());
                final Integer groupSize = (divideTestCase.groupSize());
                final Integer deviation = (divideTestCase.deviation());

                final Group<Student> klas = createClass(classSize);

                if (! this.cd.isDividable(klas, groupSize, deviation)) {
                    /* if the class in not dividable we */ continue;
                }
                
                createdGroups = (this.cd.divide(klas, groupSize, deviation));
            }
            

            final ArrayList<Map<Gender, Integer>> listOfDistributions = new ArrayList<>();
            /* Getting the distribution of the Genders of the Students */ {
                for (Group<Student> createdGroup : createdGroups) {
                    
                    final Map<Gender, Integer> distributionOfGroup;
                    distributionOfGroup = new HashMap<Gender, Integer>();
                    
                    for (Student student : createdGroup) {
                        final int amountOfStudentsWithThisGenderSoFar = (
                            distributionOfGroup.getOrDefault(student.gender(), noStudentsSoFar));
                        distributionOfGroup.put(
                            student.gender(),
                            amountOfStudentsWithThisGenderSoFar + thisNewlyFoundStudent
                        );
                    }
                    
                    listOfDistributions.add(distributionOfGroup);
                }
            }

            /* Check if all distributions are fair */ {
                for (Gender gender : Gender.values()) {
                    
                    final int amountOfStudentsInFirstGroupWithThisGender = (
                        listOfDistributions.get(0).get(gender)
                    );
                    
                    for (Map<Gender, Integer> distribution : listOfDistributions) {
                        
                        final int amountOfStudentsInThisGroupWithThisGender = (
                            distribution.get(gender)
                        );

                        final int difference = (amountOfStudentsInFirstGroupWithThisGender
                            - amountOfStudentsInThisGroupWithThisGender);
                        final boolean differenceIsBiggerThenOne = (
                            Math.abs(difference) > maxAllowedDifference);
                        if (differenceIsBiggerThenOne) {
                            fail(
                                this.getClass().getSimpleName() + "checkDistributionOfStudents() "
                                + "failed, "
                                + "the difference in the amount of Students with the gender "
                                + gender + " between two groups is too big. difference was: "
                                + difference + " max allowed is " + maxAllowedDifference + "."
                            );
                        }
                    }
                }
            }

            testCasesTested++;
        }

        System.out.println("Done!\n");
    }

    /** Checks if the students were distributed fairly based on their Country. */
    private final void checkDistributionOfStudentsUsingTheirCountry() {
       
        System.out.println(
            "Testing "
            + this.getClass().getSimpleName() 
            + ".checkDistributionOfStudentsUsingTheirCountry():"
        );

        int amountOfTestCases = (this.divideTestCases.size());
        int testCasesTested = (0);
        for (DivideTestCase divideTestCase : this.divideTestCases) {
           
            /* Printing a progress message */ {
                final String percentage = ((testCasesTested / amountOfTestCases) + "%");
                System.out.println(
                    "\rProgress done: " + testCasesTested + "out of" + amountOfTestCases + ", "
                    + "which is " + percentage + "."
                );
            }

            final Integer classSize = (divideTestCase.classSize());
            final Integer groupSize = (divideTestCase.groupSize());
            final Integer deviation = (divideTestCase.deviation());

            final Group<Student> klas = createClass(classSize);

            if (! this.cd.isDividable(klas, groupSize, deviation)) {
                /* if the class in not dividable we */ continue;
            }
            
            final Set<Group<Student>> createdGroups = (this.cd.divide(
                klas, groupSize, deviation));
            this.checkDistributionOfStudentsUsingTheirCountryBody(
                createdGroups
            );

            testCasesTested++;
        }

        System.out.println("Done!\n");
    }

    /** Actually does the checking for {@link #checkDistributionOfStudentsUsingTheirCountry()}. */
    private final void checkDistributionOfStudentsUsingTheirCountryBody(
            final Set<Group<Student>> createdGroups
    ) {

        int previousAmountOfDutchies = 0;
        int previousAmountOfInternationals = 0;
        for (Group<Student> group : createdGroups) {
            /* Counting the internationals and dutchies */ {
                for (Student student : group) {
                    if (student.isDutch()) {
                        previousAmountOfDutchies++;
                        continue;
                    }
                    previousAmountOfInternationals++;
                }
            }

            continue;
        }

        for (Group<Student> group : createdGroups) {

            int amountOfDutchies = 0;
            int amountOfInternationals = 0;
            /* Counting the internationals and dutchies */ {
                for (Student student : group) {
                    if (student.isDutch()) {
                        amountOfDutchies++;
                    } else {
                        amountOfInternationals++;
                    }
                }
            }

            final int differenceInInternationals = Math.abs(
                previousAmountOfInternationals - amountOfInternationals);
            if (differenceInInternationals > 1) {
                fail(
                    this.getClass().getSimpleName() + ".checkDistributionOfStudents() failed, "
                    + "the difference in the amount of internationals"
                );
            }
                            
            final int differenceInDutchies = Math.abs(
                previousAmountOfDutchies - amountOfDutchies);
            if (differenceInDutchies > 1) {
                fail(
                    this.getClass().getSimpleName() + ".checkDistributionOfStudents() failed, "
                    + "the difference in the amount of dutch people"
                );
            }

            previousAmountOfDutchies = amountOfDutchies;
            previousAmountOfInternationals = amountOfInternationals;
        }
    }
}
