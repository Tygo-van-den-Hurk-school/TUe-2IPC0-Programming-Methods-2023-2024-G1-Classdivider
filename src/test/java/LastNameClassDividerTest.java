/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link LastNameClassDivider}.
 * 
 * @author Tygo van den Hurk
 * @see LastNameClassDivider
 */
public class LastNameClassDividerTest extends ClassDividerTest {

    /**
     * Creates a new {@code LastNameClassDividerTest} instance.
     */
    public LastNameClassDividerTest() {
        super();
        this.cd = new LastNameClassDivider();
    }

    /**
     * Tests all the tets cases for {@link LastNameClassDivider#divide(Group, int, int)}.
     * to test if the students are divided correctly and their order is correct.
     */
    @Test
    public void testDivideHasRightOrder() {

        System.out.println(
            "Testing " + this.getClass().getSimpleName() + ".testDivideAllGroupsHaveAValidSize():"
        );

        final int amountOfTestCases = (this.divideTestCases.size());
        int testCasesTested = (0);
        for (DivideTestCase divideTestCase : this.divideTestCases) {
            
            /* Printing a progress message */ {
                final String percentage = ((testCasesTested / amountOfTestCases) + "%");
                System.out.println(
                    "\rProgress done: " + testCasesTested + " out of" + amountOfTestCases + ", "
                    + "which is " + percentage + "."
                );
            }

            final Integer classSize = (divideTestCase.classSize());
            final Integer groupSize = (divideTestCase.groupSize());
            final Integer deviation = (divideTestCase.deviation());
            
            final Group<Student> klas = this.createClass(classSize);

            if (! this.cd.isDividable(klas, groupSize, deviation)) {
                /*if the klass with the current settings isn't dividable, we */ continue;
            }

            final Set<Group<Student>> createdGroups = this.cd.divide(
                klas, groupSize, deviation
            );
            
            /* Testing if the order is maintained */ {
                for (Group<Student> group1 : createdGroups) {
                    
                    final ArrayList<Student> studentsGroup1;
                    /* Getting the students from group 1 as an array */ {
                        studentsGroup1 = new ArrayList<>(group1.size());
                        for (Student student : group1) {
                            studentsGroup1.add(student);
                        }
                        Collections.sort(
                            studentsGroup1, 
                            (student1, student2) -> 
                                student1.sortName().compareTo(student2.sortName())
                        );
                    }


                    for (Group<Student> group2 : createdGroups) {
                        
                        final ArrayList<Student> studentsGroup2;
                        /* Getting the students from group 1 as an array */ {
                            studentsGroup2 = new ArrayList<>(group2.size());
                            for (Student student : group2) {
                                studentsGroup2.add(student);
                            }
                            Collections.sort(
                                studentsGroup2, 
                                (student1, student2) -> 
                                    student1.sortName().compareTo(student2.sortName())
                            );
                        }

                        this.checkIfOrderIsMaintained(studentsGroup1, studentsGroup2);
                    }
                }
            }

            testCasesTested += 1;
        }

        System.out.println("Done!\n");
    }

    /** Checks if the order is maintained. */
    private final void checkIfOrderIsMaintained(
            final List<Student> studentsGroup1, 
            final List<Student> studentsGroup2) {

        final Integer firstStudent = (0); // explanation for a constant.
        
        /* Checking if the check is possible */ {
            if (studentsGroup1 == null) {
                fail(
                    this.getClass().getSimpleName()
                    + ".checkIfOrderIsMaintained(List<Student>, List<Student>) failed, "
                    + "the first list (studentsGroup1) was null."
                );
            }

            if (studentsGroup1.size() == 0) {
                fail(
                    this.getClass().getSimpleName()
                    + ".checkIfOrderIsMaintained(List<Student>, List<Student>) failed, "
                    + "the first list (studentsGroup1) is empty."
                );
            }

            if (studentsGroup2 == null) {
                fail(
                    this.getClass().getSimpleName()
                    + ".checkIfOrderIsMaintained(List<Student>, List<Student>) failed, "
                    + "the second list (studentsGroup2) was null."
                );
            }

            if (studentsGroup2.size() == 0) {
                fail(
                    this.getClass().getSimpleName()
                    + ".checkIfOrderIsMaintained(List<Student>, List<Student>) failed, "
                    + "the second list (studentsGroup2) is empty."
                );
            }
        }

        final List<Student> firstOrder;
        final List<Student> secondOrder;
        /* Finding which List goes before which list*/ {
            final Student studentFromGroup1 = (studentsGroup1.get(firstStudent));
            final Student studentFromGroup2 = (studentsGroup2.get(firstStudent));
            final int lexicographicOrderingDifference = (
                studentFromGroup1.sortName().compareTo(studentFromGroup2.sortName())
            );

            final boolean lastNamesAreTheSame = (lexicographicOrderingDifference == 0);
            if (lastNamesAreTheSame) {
                return;
            }

            final boolean studentFromGroup1ComesFirst = (lexicographicOrderingDifference < 0);
            if (studentFromGroup1ComesFirst) {
                firstOrder = studentsGroup1;
                secondOrder = studentsGroup2;
            } else {
                firstOrder = studentsGroup2;
                secondOrder = studentsGroup1;
            }
        }

        /* Checking if the order was correct */ {
            final Student firstStudentFromFirstOrder = (firstOrder.get(firstStudent));
            for (Student studentFromSecondOrder : secondOrder) {
                final String messageOnFailedTest = (
                    this.getClass().getSimpleName()
                    + ".checkIfOrderIsMaintained(List<Student>, List<Student>) failed, "
                    + "the order of the students was not maintained. "
                    + "the student with the last name "
                    + firstStudentFromFirstOrder.sortName() + " "
                    + " was supposed to go before the student with the last name " 
                    + studentFromSecondOrder.sortName() + " "
                    + "however this was not the case.");
                final boolean orderIsMaintained = (
                    firstStudentFromFirstOrder
                        .sortName()
                        .compareTo(studentFromSecondOrder.sortName()) < 0);
                assertTrue(orderIsMaintained, messageOnFailedTest);
            }
        }
    }
}

