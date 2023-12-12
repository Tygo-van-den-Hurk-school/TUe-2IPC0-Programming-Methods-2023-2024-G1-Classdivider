import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test StudentsFile class.
 *
 * @author Huub de Beer
 */
public class StudentsFileTest {

    private static final String HEADER = "first name,last name,ID,country,gender";

    public StudentsFileTest() {
    }

    /**
     * Test of fromCSV method, of class StudentsFile.
     */
    @Test
    public void testFromCSV_String() {
        try {
            String csv;
            Group<Student> students;

            // Empty string results in an empty group of students
            csv = "";
            students = StudentsFile.fromCSV(csv);
            assertTrue(students.isEmpty());

            // Single row results in a singleton group with the student described in that row
            Student single = new Student(
                    "First name",
                    "Last name",
                    "ID",
                    "country",
                    Gender.NOT_REPORTED
            );
            csv = "%s\n%s,%s,%s,%s,%s".formatted(
                    HEADER,
                    single.firstName(),
                    single.lastName(),
                    single.id(),
                    single.country(),
                    single.gender()
            );
            students = StudentsFile.fromCSV(csv);
            assertEquals(1, students.size());
            Student student = students.pick();
            assertEquals(single, student);
            assertEquals(single.firstName(), student.firstName());
            assertEquals(single.lastName(), student.lastName());

            // Incorrect data results in error
            assertThrows(
                    IllegalArgumentException.class,
                    () -> StudentsFile.fromCSV("%s\nThis is an invalid row".formatted(HEADER))
            );
        } catch (IOException ex) {
            fail("Unexpected IOException thrown by commons CSV library: " + ex);
        }
    }

    /**
     * Test of toCSV method, of class StudentsFile.
     */
    @Test
    public void testToCSV_Group() {
        try {
            Group<Student> group = new Group<>();
            String csv = StudentsFile.toCSV(group);
            assertEquals("", csv.trim());

            final String firstName = "Huub";
            final String lastName = "de Beer";
            final String id = "232112";
            final String country = "NL";
            final Gender gender = Gender.X;
            Student student = new Student(firstName, lastName, id, country, gender);
            group = new Group<>();
            group.add(student);
            csv = StudentsFile.toCSV(group);
            String lastRow = csv.split("\\R")[0].trim();

            assertEquals("%s,%s,%s,%s,%s".formatted(
                    firstName,
                    lastName,
                    id,
                    country,
                    gender
            ), lastRow);
        } catch (IOException ex) {
            fail("Unexpected IOException thrown by commons CSV library: " + ex);
        }
    }

}
