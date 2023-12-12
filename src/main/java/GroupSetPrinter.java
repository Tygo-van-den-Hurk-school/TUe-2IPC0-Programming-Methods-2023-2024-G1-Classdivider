/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

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