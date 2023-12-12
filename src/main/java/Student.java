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
 * Student.
 *
 * @see
 * <a href="https://shinesolutions.com/2018/01/08/falsehoods-programmers-believe-about-names-with-examples/">This
 * page about potential issues with names</a> that we ignore in this Student record. We modeled
 * student names from a Dutch perspective.
 *
 * @author Huub de Beer
 *
 * @param firstName student's first name
 * @param lastName student's last name
 * @param id student's ID
 * @param country student's country of origin
 * @param gender self-reported gender
 */
public record Student(
        String firstName,
        String lastName,
        String id,
        String country,
        Gender gender
) implements Comparable<Student> {

    /** Stores the Key that corresponds to Dutch. */
    private static final String DUTCH = "NL";

    /**
     * Return the sort name, Dutch style.
     *
     * In the Netherlands, we sort a names starting with the last name, then the first name, and
     * then optional "tussenvoegsels" like "de" or "van der".
     *
     * Examples:
     *
     * <ul>
     * <li> Beer, Huub de
     * <li> Jansens, Jan
     * <li> Borne, Lisa van der
     * </ul>
     *
     * @pre true
     * @return This student's name reformatted for sorting
     */
    public String sortName() {

        int index = 0;

        /* Finding the index of the first char that is a cappital letter */ {
            while (
                (index < lastName.length()) 
                    && (! Character.isUpperCase(lastName.charAt(index)))
            ) {
                index++;
            }
        }

        final String prefix = lastName.substring(0, index).trim();
        final String last = lastName.substring(index);

        return (last + ", " + firstName + (prefix.isBlank() ? "" : " " + prefix));
    }

    /**
     * Two students are equal when they have the same ID.
     *
     * @pre true
     * @param obj object to compare this student with
     * @post {@code \result == (other instanceof Student && other.id() == this.id())}
     * @return true if other's ID is equal to this student's ID, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        
        if (obj instanceof Student student) {
            return (this.id().equals(student.id()));
        }

        return false;
    }

    /**
     * Compare this student with {@code other} student.
     * 
     * @param otherStudent another student to compare this student to
     * @return 0 when equal, negative when {@code this} is smaller than {@code other}, 
     * positive when {@code this} is larger than {@code other}.
     * 
     * @pre {@code true}
     * @post {@code this == other ==> \result == 0}
     * @post {@code this < other ==> \result < 0}
     * @post {@code this > other ==> \result > 0}
     */
    @Override
    public int compareTo(final Student otherStudent) {
        final String thisSortName = (this.sortName());
        final String otherStudentSortName = (otherStudent.sortName());
        return (thisSortName.compareTo(otherStudentSortName));
    }
    
    /**
     * Determine if this student is Dutch or not.
     * 
     * @return true when country code is "NL"
     * 
     * @pre {@code true}
     * @post {@code \result <==> this.country() == "NL"}
     */
    public boolean isDutch() {
        return (this.country.equals(DUTCH));
    }

}