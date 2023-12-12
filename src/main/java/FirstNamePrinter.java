import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Print a student group set to the terminal. Number the groups. Each student is printed on a new
 * line by their first name. When multiple students have the same first name, the first character
 * of their last name is printed as well.
 * 
 * @author Huub de Beer
 */
public class FirstNamePrinter implements GroupSetPrinter {
    
    /** Stores wether or not a student has a unique first name. */
    private final Map<String, Boolean> uniqueFirstName = new HashMap<>();

    /**
     * Create a new {@code StudentGroupSetPrinter}.
     * 
     * @param klas class of students the group set to print originated from. This class is used
     * to determine if students' first names are unique or not.
     */
    public FirstNamePrinter(final Group<Student> klas) {

        /* Storing wether or not students have a unique first name */ {
            for (Student student : klas) {
                if (uniqueFirstName.containsKey(student.firstName())) {
                    uniqueFirstName.put(student.firstName(), false);
                } else {
                    uniqueFirstName.put(student.firstName(), true);
                }
            }
        }
    }

    @Override
    public void print(final Set<Group<Student>> groupSet) {
                
        /* Print each group */ {
            int groupNr = 0;
            for (Group<Student> group : groupSet) {
                groupNr++;
                System.out.println("Group " + groupNr + ":");
                this.print(group);
                System.out.println();
            }
        }
    }

    /** Prints the names of all . */
    private void print(final Group<Student> group) {

        for (Student student : group) {
            
            final String firstName = (student.firstName());
            final char firstLetterOfLastName = (student.sortName().charAt(0));

            final String name;
            /* Getting the name to print */ {
                final boolean hasUniqueFirstName = (this.uniqueFirstName.get(student.firstName()));
                if (! hasUniqueFirstName) {
                    name = (firstName);
                } else {
                    name = (firstName + " " + firstLetterOfLastName);
                }
            }

            System.out.println("- " + name);
        }
    }
}