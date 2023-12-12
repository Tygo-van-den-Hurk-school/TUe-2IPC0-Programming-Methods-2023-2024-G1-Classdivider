/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

/**
 * Test class for {@code RandomClassDivider}.
 * 
 * @author Tygo van den Hurk
 * @see RandomClassDivider
 * @see ClassDividerTest
 */
public class RandomClassDividerTest extends ClassDividerTest {
    
    /**
     * Creates a new {@code RandomClassDividerTest} instance.
     */
    public RandomClassDividerTest() {
        super();
        this.cd = new RandomClassDivider();
    }
}
