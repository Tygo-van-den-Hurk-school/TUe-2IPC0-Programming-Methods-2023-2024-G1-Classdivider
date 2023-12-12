import java.util.Set;

/**
 * Print a student group set to the terminal.
 * 
 * @author Huub de Beer
 */
interface GroupSetPrinter {  
    
    /**
     * Print {@code groupSet} to the terminal.
     * 
     * @param groupSet set of groups to print
     */
    public void print(Set<Group<Student>> groupSet);
}