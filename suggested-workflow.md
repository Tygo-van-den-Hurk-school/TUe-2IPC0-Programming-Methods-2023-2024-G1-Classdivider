Suggested workflow (from canvas on 12-dec-2023):
    
1. Extract the given [starter project ZIP file](https://canvas.tue.nl/courses/25282/files/5181624?wrap=1): This creates directory `classdivider`.

2. Startup:
    1. Initialize a `Git` repository in this directory with `NetBeans`.
    2. Build and run the program.
    3. Run the existing tests. They should **all** pass.
    4. Generate the `JavaDoc` documentation.
    5. Study the existing code to see how it differs from your solution to `Assignment W1 A3`.
    6. Run `Checkstyle` on the project. There should be **no** errors.
    7. If you're ready to start changing the project, increment the version property in the `pom.xml` to `0.8`.
    8. Commit your work.
3. Make the `public` and `protected` methods in class `ClassDivider` robust and commit your work.
4. Test robustness in `ClassDividerTest` and commit your work.
5. Apply the `Strategy Design Pattern`:
    1. Create a subclass of `ClassDivider` called `RandomClassDivider`.
    2. Then make `ClassDivider` abstract, and make method `divide` abstract.
    3. In `RandomClassDivider` override method `divide`. Cut and past the body of `ClassDivider#divide` to `RandomClassDivider#divide`.
    4. Implement `ClassDivisionStrategy#create`.
    5. Create a subclass of `ClassDividerTest` called `RandomClassDividerTest`. Add a constructor in which you assign a new instance of `RandomClassDivider` to protected instance variable `cd`.
    6. Make `ClassDividerTest` abstract.
    7. Test the project: All tests should pass.
    8. Run `CheckStyle` on your project: There should be **no** errors.
    9. Commit your work.
6. Add the `LastName` strategy, including implementing `NameRangePrinter` and testing `LastNameClassDivider`. Commit your work.
7. Add and test `BucketClassDivider`. Commit your work.
8. Add the two strategies `DutchAndInternationals` and `Genders`. Commit your work.

self added Steps:

 1. add your your name, student number, the date to **all** files.
 2.  submit the result to canvas.

I followed the following steps exactly to complete the assignment as you suggested.