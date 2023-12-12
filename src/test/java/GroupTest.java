
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Group.
 *
 * @author Huub de Beer
 */
public class GroupTest {

    public GroupTest() {
    }

    /**
     * Test of size method, of class Group.
     */
    @Test
    public void testSize() {
        Group<Integer> group = new Group<>();
        assertEquals(0, group.size());

        group.add(0);
        assertEquals(1, group.size());

        group.add(1);
        assertEquals(2, group.size());

        group.remove(0);
        assertEquals(1, group.size());

        group.remove(1);
        assertEquals(0, group.size());
    }

    /**
     * Test of isEmpty method, of class Group.
     */
    @Test
    public void testIsEmpty() {
        Group<Integer> group = new Group<>();
        assertTrue(group.isEmpty());
        group.add(0);
        assertFalse(group.isEmpty());
    }

    /**
     * Test of pick method, of class Group.
     */
    @Test
    public void testPick() {
        Group<Integer> group = new Group<>();
        // You cannot pick a member from an empty group.
        assertThrows(IllegalStateException.class, () -> group.pick());

        // If you add one member to the group, you always pick that one.
        group.add(0);
        assertEquals(0, group.pick());

        // Fix randomness; All three of the three members are picked
        Group.setRandomSeed(1);
        group.add(1);
        group.add(2);
        assertEquals(0, group.pick());
        assertEquals(1, group.pick());
        assertEquals(1, group.pick());
        assertEquals(0, group.pick());
        assertEquals(2, group.pick());
    }

    /**
     * Test of toString method, of class Group.
     */
    @Test
    public void testToString() {
        Group<Integer> group = new Group<>();
        assertEquals("", group.toString());
        group.add(0);
        assertEquals("0", group.toString());
        group.add(1);
        assertEquals("0; 1", group.toString());
        group.add(2);
        assertEquals("0; 1; 2", group.toString());
    }

    /**
     * Test of add method, of class Group.
     */
    @Test
    public void testAdd() {
        Group<Integer> group = new Group<>();
        assertTrue(group.isEmpty());

        group.add(0);
        assertAll(
                () -> assertFalse(group.isEmpty()),
                () -> assertEquals(1, group.size()),
                () -> assertTrue(group.contains(0))
        );

        group.add(1);
        assertAll(
                () -> assertFalse(group.isEmpty()),
                () -> assertEquals(2, group.size()),
                () -> assertTrue(group.containsAll(List.of(0, 1)))
        );
    }

    /**
     * Test of iterator method, of class Group.
     */
    @Test
    public void testIterator() {
        Group<Integer> group = new Group<>();
        assertFalse(group.iterator().hasNext());

        group.add(0);
        Iterator<Integer> iter = group.iterator();
        assertTrue(iter.hasNext());
        assertEquals(0, iter.next());

        group.add(1);
        group.add(2);

        // Different random seed gives a different sequence of members
        Group.setRandomSeed(3);
        List<Integer> vals1 = new ArrayList<>();
        group.iterator().forEachRemaining(vals1::add);

        Group.setRandomSeed(2235);
        List<Integer> vals2 = new ArrayList<>();
        group.iterator().forEachRemaining(vals2::add);

        assertNotEquals(vals1, vals2);

        // Same random seed gives same sequence of members
        Group.setRandomSeed(5);
        vals1 = new ArrayList<>();
        group.iterator().forEachRemaining(vals1::add);

        Group.setRandomSeed(5);
        vals2 = new ArrayList<>();
        group.iterator().forEachRemaining(vals2::add);

        assertEquals(vals1, vals2);
    }

    /**
     * Test of addAll method, of class Group.
     */
    @Test
    public void testAddAll() {
        Group<Integer> group = new Group<>();
        assertAll(
                () -> assertTrue(group.isEmpty()),
                () -> assertEquals(0, group.size())
        );

        List<Integer> members = List.of(0, 1, 2);
        group.addAll(members);
        assertAll(
                () -> assertFalse(group.isEmpty()),
                () -> assertEquals(3, group.size()),
                () -> assertTrue(group.containsAll(members))
        );
    }

    /**
     * Test of clear method, of class Group.
     */
    @Test
    public void testClear() {
        Group<Integer> group = new Group<>();
        assertTrue(group.isEmpty());
        group.add(0);
        assertFalse(group.isEmpty());
        group.clear();
        assertTrue(group.isEmpty());
    }

    /**
     * Test of contains method, of class Group.
     */
    @Test
    public void testContains() {
        Group<Integer> group = new Group<>();
        assertFalse(group.contains(0));
        group.add(0);
        assertTrue(group.contains(0));
    }

    /**
     * Test of containsAll method, of class Group.
     */
    @Test
    public void testContainsAll() {
        Group<Integer> group = new Group<>();
        assertFalse(group.containsAll(List.of(0, 1, 2)));
        group.add(0);
        assertFalse(group.containsAll(List.of(0, 1, 2)));
        group.add(1);
        group.add(2);
        assertTrue(group.containsAll(List.of(0, 1, 2)));
        assertFalse(group.containsAll(List.of(0, 1, 2, 3)));
    }

    /**
     * Test of remove method, of class Group.
     */
    @Test
    public void testRemove() {
        Group<Integer> group = new Group<>();
        assertFalse(group.remove(0));
        group.add(0);
        assertAll(
                () -> assertEquals(1, group.size()),
                () -> assertTrue(group.remove(0)),
                () -> assertEquals(0, group.size())
        );
    }

    /**
     * Test of removeAll method, of class Group.
     */
    @Test
    public void testRemoveAll() {
        Group<Integer> group = new Group<>();
        assertFalse(group.removeAll(List.of(0, 1)));

        group.add(0);
        group.add(1);
        group.add(2);
        assertAll(
                () -> assertEquals(3, group.size()),
                () -> assertTrue(group.removeAll(List.of(0, 1))),
                () -> assertEquals(1, group.size()),
                () -> assertTrue(group.contains(2))
        );
    }

    /**
     * Test of retainAll method, of class Group.
     */
    @Test
    public void testRetainAll() {
        Group<Integer> group = new Group<>();
        assertFalse(group.retainAll(List.of(0, 1)));

        group.add(0);
        group.add(1);
        group.add(2);
        assertAll(
                () -> assertEquals(3, group.size()),
                () -> assertTrue(group.retainAll(List.of(0, 1))),
                () -> assertEquals(2, group.size()),
                () -> assertFalse(group.contains(2))
        );
    }

    /**
     * Test of toArray method, of class Group.
     */
    @Test
    public void testToArrayObject() {
        Group<Integer> group = new Group<>();
        assertArrayEquals(new Object[]{}, group.toArray());
        group.add(0);
        assertArrayEquals(new Object[]{0}, group.toArray());
        group.add(1);
        assertArrayEquals(new Object[]{0, 1}, group.toArray());
    }

    /**
     * Test of toArray method, of class Group.
     */
    @Test
    public void testToArrayT() {
        Group<Integer> group = new Group<>();
        assertArrayEquals(new Integer[]{}, group.toArray());
        group.add(0);
        assertArrayEquals(new Integer[]{0}, group.toArray());
        group.add(1);
        assertArrayEquals(new Integer[]{0, 1}, group.toArray());
    }

    /**
     * Test of equals method, of class Group.
     */
    @Test
    public void testEquals() {
        Group<Integer> group1 = new Group<>();
        Group<Integer> group2 = new Group<>();

        assertTrue(group1.equals(group2));

        group1.add(1);
        group2.add(1);

        assertTrue(group1.equals(group2));

        group1.add(2);

        assertFalse(group1.equals(group2));

        group2.add(3);

        assertFalse(group1.equals(group2));
    }
}
