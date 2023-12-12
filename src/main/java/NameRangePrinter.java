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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Prints the first and last name in the range of the students in the group set.
 * 
 * @author Tygo van den Hurk
 * @see GroupSetPrinter
 */
public class NameRangePrinter implements GroupSetPrinter {

    /**
     * Create a new {@code NameRangePrinter}.
     */
    public NameRangePrinter() {
    }

    @Override
    public void print(final Set<Group<Student>> groupSet) {

        final String[] sortedGroupsAsString;
        /* Getting all the names ranges for all the groups */ {
            String[] groupsAsString = new String[groupSet.size()];
            int groupIndex = 0;
            for (Group<Student> group : groupSet) {
                groupsAsString[groupIndex] = this.getStringFrom(group);
                groupIndex++;
            }
            Arrays.sort(groupsAsString);
            sortedGroupsAsString = groupsAsString;
        }

        /* Printing the names in the terminal */ {
            for (String lineToPrint : sortedGroupsAsString) {
                System.out.println(lineToPrint);
            }
        }
    }
    
    /** Prints the name of a a group of students. */    
    private String getStringFrom(final Group<Student> group) {

        final List<Student> sortedStudents;
        /* Getting a sorted Array of the students in klass */ {
            final ArrayList<Student> students = new ArrayList<Student>(group);
            Collections.sort(
                students, 
                (student1, student2) -> student1.sortName().compareTo(student2.sortName()));
            sortedStudents = students;
        }
        
        final String nameOfFirstStudent;
        final String nameOfLastStudent;
        /* Getting the name of the first student and the last student */ {

            final int firstStudentInList = (0); // explanation for a constant
            final Student firstStudent = sortedStudents.get(firstStudentInList);
            nameOfFirstStudent = firstStudent.sortName();

            final int lastStudentInList = (sortedStudents.size() - 1);
            final Student lastStudent = (sortedStudents.get(lastStudentInList));
            nameOfLastStudent = lastStudent.sortName();
        }

        final String result;
        /* Formatting the resulting string with the information we just computed */ {
            final String range = (nameOfFirstStudent + " - " + nameOfLastStudent);
            result = ("Group " + range + " (" + group.size() + " students)");
        }

        return result;
    }
}
