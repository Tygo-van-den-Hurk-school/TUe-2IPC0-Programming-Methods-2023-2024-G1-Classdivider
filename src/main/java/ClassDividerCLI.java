import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 * ClassDivider–Divide a class of students into groups.
 *
 * @version 0.8
 * @author Huub de Beer
 */
@Command(
    name = "classdivider",
    mixinStandardHelpOptions = true,
    version = "classdivider 0.8",
    description = "Divide a class of students into groups.")
public class ClassDividerCLI implements Callable<Integer> {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ CommandLine Options ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /*
     * Size of the groups to create.
     */
    @CommandLine.Option(
        names = {"-g", "--group-size"},
        description = "target group size.",
        required = true
    )
    private int groupSize;

    /*
     * Number of students that a group can deviate from the target group size.
     *
     * For example, if the target group size is 10 and the deviation is 2, all groups in the group 
     * set will have sizes between 8 and 12. Defaults to 1.
     */
    @CommandLine.Option(
        names = {"-d", "--deviation"},
        description = (
            "allowed difference of number of students in a group "
            + " and the target group size. Defaults to ${DEFAULT-VALUE}."))
    private int deviation = 1;
    
    /** Stores the {@link ClassDivisionStrategy} that is going to be used. */
    @CommandLine.Option(
        names = {"-s", "--strategy"},
        description = (
            "class division strategy. Pick one of: ${COMPLETION-CANDIDATES}."
            + " Defaults to ${DEFAULT-VALUE}."))
    private ClassDivisionStrategy strategy = ClassDivisionStrategy.Random;

    /*
     * Path to file containing student data
     */
    @Parameters(
        index = "0",
        description = "path to file with students data in CSV format"
    )
    private Path studentsFile;

    @Spec
    CommandSpec commandSpec; // injected by picocli

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Helper Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /** 
     * Throws an {@link ParameterException} if the {@link #groupSize} or {@link #deviation} is 
     * wrong. 
     */
    private final void validateGroupSizeAndDeviation() {
        
        if (this.groupSize <= 0) {
            final String message = ("target group size must be a positive integer number.");
            throw new ParameterException(this.commandSpec.commandLine(), message);
        }

        if (this.deviation >= this.groupSize || this.deviation < 0) {
            final String message = (
                "deviation must be a positive number smaller than the target group size.");
            throw new ParameterException(commandSpec.commandLine(), message);
        }
    }
    
    /** Gets the {@link Student}s from a {@code .csv} {@link File}. */
    private final Group<Student> validateStudentsFile() {
        try {
            
            final Group<Student> klas = StudentsFile.fromCSV(studentsFile);
            return klas;

        } catch (final IOException e) {
            
            final String message = (
                "Unable to open or read students file '%s': %s."
            ).formatted(studentsFile, e.getMessage());

            throw new ParameterException(commandSpec.commandLine(), message);
        }
    }
    
    /** Gets the printer that belongs to the {@link Strategy} used. */
    private final GroupSetPrinter getPrinter(
            final ClassDivisionStrategy strategy,
            final Group<Student> klas
    ) {

        return switch (strategy) {
            default -> new FirstNamePrinter(klas);
        };
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Main Functions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Override
    public Integer call() throws Exception {

        final int success = (0);            

        this.validateGroupSizeAndDeviation();
        
        final Group<Student> klas = validateStudentsFile();
        final ClassDivider divider = new ClassDivider();
        
        /* Check if the pre condition of the program is violated */ {
            if (! divider.isDividable(klas, groupSize, deviation)) {
                final String message = (
                    "Unable to divide a class of %d students into groups of %d+/-%d students."
                ).formatted(klas.size(), groupSize, deviation);
                throw new ParameterException(commandSpec.commandLine(), message);
            }
        } 
        
        final GroupSetPrinter printer = (this.getPrinter(strategy, klas));
        final Set<Group<Student>> groupSet = (divider.divide(klas, groupSize, deviation));
        
        printer.print(groupSet);

        return success;
    }

    /**
     * Program "classloader" starts here.
     * 
     * @param args command-line arguments
     */
    public static final void main(final String[] args) {
        
        final ClassDividerCLI classDividerCLI = (new ClassDividerCLI());
        final CommandLine cli = (new CommandLine(classDividerCLI));
        
        cli.setCaseInsensitiveEnumValuesAllowed(true);
        
        final int exitCode = cli.execute(args);
        
        System.exit(exitCode);
    }
}
