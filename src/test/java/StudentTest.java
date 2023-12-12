/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Student.
 *
 * @author Huub de Beer
 */
public class StudentTest {

    public StudentTest() {
    }

    /**
     * Test of sortName method, of record Student.
     */
    @Test
    public void testSortName() {
        // Dutch style sort names
        // Last name, First name tussenvoegsels
        assertEquals("Beer, Huub de", 
                new Student(
                        "Huub", 
                        "de Beer", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        assertEquals("Borne, Elsa van der", 
                new Student(
                        "Elsa", 
                        "van der Borne", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        assertEquals("Jansens, Jan", 
                new Student(
                        "Jan", 
                        "Jansens", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        // Belgian style sort names only work if tussenvoegsel starts with capital
        assertEquals("Van der Borne, Else", 
                new Student(
                        "Else", 
                        "Van der Borne", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());

        // Some other names from around the world, as Dutch style sort names
        assertEquals("Mahamat, Omar", 
                new Student(
                        "Omar", 
                        "Mahamat", 
                        "", "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        assertEquals("Lee, Zhang", 
                new Student(
                        "Zhang", 
                        "Lee", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        assertEquals("Fernández, Alfredo", 
                new Student(
                        "Alfredo", 
                        "Fernández", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
        
        assertEquals("Santos, Julia dos", 
                new Student(
                        "Julia", 
                        "dos Santos", 
                        "", 
                        "", 
                        Gender.NOT_REPORTED
                ).sortName());
    }

    /**
     * Test of equals method, of record Student.
     */
    @Test
    public void testEquals() {
        Student s1 = new Student(
                "First name", 
                "Last name",
                "ID", 
                "", 
                Gender.NOT_REPORTED
        );
        
        Student s2 = new Student(
                "First name", 
                "Last name", 
                "Other ID", 
                "", 
                Gender.NOT_REPORTED
        );
        
        Student s3 = new Student(
                "Other first name", 
                "Other last name", 
                "ID",
                "", 
                Gender.NOT_REPORTED
        );

        assertAll(
                // Every student is equal to themselves
                () -> assertEquals(s1, s1),
                () -> assertEquals(s2, s2),
                () -> assertEquals(s3, s3),
                
                // A student is equal to another student if they have the same ID, 
                // regardless if the other fields match or not
                () -> assertEquals(s1, s3),
                
                // A student is not equal to another student if their IDs are different, 
                // regardless if they have the same name or not
                () -> assertNotEquals(s1, s2),
                () -> assertNotEquals(s3, s2)
        );
    }
    
    /**
     * Test of compareTo method, of record Student.
     */
    @Test
    public void testCompareTo() {
        Student s1 = new Student(
                "First name", 
                "Last name", 
                "ID", 
                "", 
                Gender.NOT_REPORTED
        );
        
        Student s2 = new Student(
                "First name", 
                "Last name", 
                "Other ID", 
                "", 
                Gender.NOT_REPORTED
        );
        
        Student s3 = new Student(
                "Other first name", 
                "Other last name", 
                "ID", 
                "", 
                Gender.NOT_REPORTED
        );
        
        assertEquals(0, s1.compareTo(s2));
        assertEquals(0, s1.compareTo(s1));
        assertEquals(0, s2.compareTo(s1));
        assertTrue(s1.compareTo(s3) < 0);
        assertTrue(s3.compareTo(s1) > 0);
    }

}
