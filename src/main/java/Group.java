/*\
 * Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
 * With Student Number: 1705709
 * 
 * On 12th of december 2023, at 11:35 CET
 * 
 * for the assignment of 2IPC0 Programming methods, 
 * G1a: Add Different Class Dividing Strategies to the Classdivider Project
\*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code Group} of things.
 * 
 * A {@code Group} is a set.
 * 
 * @author Huub de Beer
 * @param <Type> group member type
 */
public class Group<Type> implements Set<Type> {

    /** Random number generator used to randomly pick members from this group. */
    private static Random RNG = new Random();
    
    /**
     * Set the seed for the random number generator used to randomly pick members
     * from this group.
     * 
     * Note. Use for testing purposes.
     * 
     * @param seed seed for random number generator
     */
    protected static void setRandomSeed(final long seed) {
        Group.RNG = new Random(seed);
    }

    private final List<Type> members;
    
    /*
     * Model: {@code {m|this.contains(m)}}
     * 
     * Abstraction function: AF(this) = {m|members.contains(m)}
     */

    /**
     * Create a new empty group.
     * 
     * @pre true
     */
    public Group() {
        this.members = new ArrayList<Type>();
    }

    /**
     * Group's size.
     * 
     * @pre {@code true}
     * @post {@code \result == |this|}
     * @return this group's size
     */
    @Override
    public int size() {
        return this.members.size();
    }

    /**
     * Determine if this group is empty or not.
     * 
     * @pre {@code true}
     * @post {@code \result == (size() == 0)}
     * @return true if this group is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.members.isEmpty();
    }

    /**
     * Pick a member from this group at random.
     * 
     * @pre {@code !this.isEmpty()}
     * @post {@code this.contains(\result)}
     * @return picked member
     * @throws IllegalStateException when {@code this.isEmpty()}
     */
    public Type pick() {
        
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot pick an member from an empty group.");
        }

        return this.members.get(Group.RNG.nextInt(0, this.size()));
    }

    @Override
    public String toString() {
        return (
            this.members.stream()
                   .map(Object::toString)
                   .collect(Collectors.joining("; "))
            );
    }

    /**
     * Add a member to this group.
     * 
     * @pre {@code true}
     * @param member member to add
     * @post {@code this == {member} union \old(this)}
     * @return true when member wasn't already in this group. False otherwise.
     */
    @Override
    public boolean add(final Type member) {

        if (this.members.contains(member)) {
            return false;
        }

        this.members.add(member);
        return true;
    }

    /** Iterate over the members in this group in a random order. */
    private final class RandomIterator implements Iterator<Type> {
        
        private final List<Type> alreadyPicked;

        public RandomIterator() {
            this.alreadyPicked = new ArrayList<>();
        }

        @Override
        public boolean hasNext() {
            return (Group.this.members.size() != this.alreadyPicked.size());
        }

        @Override
        public Type next() {
            final List<Type> candidates = (
                Group.this.members.stream()
                       .filter(s -> ! this.alreadyPicked.contains(s))
                       .toList()
            );
            
            final int maxValueForRandomIndex = (candidates.size());
            final int randomIndex = (Group.RNG.nextInt(0, maxValueForRandomIndex));
            final Type pick = candidates.get(randomIndex);

            this.alreadyPicked.add(pick);

            return pick;
        }
    }

    /**
     * Iterate over the members in this group in random order.
     * 
     * @return random iterator
     */
    @Override
    public Iterator<Type> iterator() {
        return new RandomIterator();
    }

    /**
     * Add all members.
     * 
     * @pre {@code true}
     * @param members the members to add to this group
     * @post {@code this == members union \old(this)}
     * @return {@code true}
     */
    @Override
    public boolean addAll(final Collection<? extends Type> members) {
        
        for (Type member : members) {
            this.add(member);
        }

        return true;
    }

    /**
     * Clear this group.
     * 
     * @pre {@code true}
     * @post {@code isEmpty()}
     */
    @Override
    public void clear() {
        this.members.clear();
    }

    /**
     * Determine if member is in this group.
     * 
     * @pre {@code true}
     * @param member the member to check membership for
     * @post {@code \result == member \in this}
     * @return true if member in this group, false otherwise
     */
    @Override
    public boolean contains(final Object member) {
        return this.members.contains(member);
    }

    /**
     * Determine if all members are in this group.
     * 
     * @pre {@code true}
     * @param members the members to check membership for
     * @post {@code (\forall m; members.contains(m); this.contains(m))}
     * @return true if all members are in this group, false otherwise
     */
    @Override
    public boolean containsAll(final Collection<?> members) {
        return this.members.containsAll(members);
    }

    /**
     * Remove member from this group.
     * 
     * @pre {@code true}
     * @param member the member to remove from this group
     * @post <ul>
     * <li>{@code this == \old(this) - {member}}</li>
     * <li>{@code \result == \old{this}.contains(member)}</li>
     * </ul>
     * @return true if removed member was in this group, false otherwise
     */
    @Override
    public boolean remove(final Object member) {
        return this.members.remove(member);
    }

    /**
     * Remove members from this group.
     * 
     * @pre {@code true}
     * @param members the members to remove from this group
     * @post <ul>
     * <li>{@code this == \old(this) - members}</li>
     * <li>{@code \result == (\old{this} != this)}</li>
     * </ul>
     * @return true if group changed, false otherwise
     */
    @Override
    public boolean removeAll(final Collection<?> members) {
        return this.members.removeAll(members);
    }

    /**
     * Intersect this with members.
     * 
     * @pre {@code true}
     * @param members the members to retain in this group
     * @post <ul>
     * <li>{@code this == \old(this) intersect members}</li>
     * <li>{@code \result == (\old(this) != this)}</li>
     * </ul>
     * @return true if group changed, false otherwise
     */
    @Override
    public boolean retainAll(final Collection<?> members) {
        return this.members.retainAll(members);
    }

    /**
     * Convert this group to an array.
     * 
     * @pre {@code true}
     * @post <ul>
     * <li>{@code (\forall i; \result.has(i); this.contains(\result[i]))}</li>
     * <li>{@code (\forall m; this.contains(m); (\exists i; \result.has(i); \result[i] == m))}</li>
     * <li>{@code (\forall m; this.contains(m); m instanceof Object)}</li>
     * </ul>
     * @return an array of all members in this group
     */
    @Override
    public Object[] toArray() {
        return this.members.toArray();
    }

    /**
     * Convert this group to an array.
     * 
     * @pre {@code true}
     * @param <U> type of return array members
     * @param memberArray array to return values in
     * @post <ul>
     * <li>{@code (\forall i; \result.has(i); this.contains(\result[i]))}</li>
     * <li>{@code (\forall m; this.contains(m); (\exists i; \result.has(i); \result[i] == m))}</li>
     * </ul>
     * @return an array of all members in this group
     */
    @Override
    public <U> U[] toArray(final U[] memberArray) {
        return this.members.toArray(memberArray);
    }
    
    /**
     * Determine if other is equal to this group.
     * 
     * @pre {@code true}
     * @param obj to compare with this group
     * @post {@code group.containsAll(this) && this.containsAll(group)}
     * @return true if other is a group and contains exactly the same members, false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (! (obj instanceof Group<?> group)) {
            return false;
        }
        
        if (group.size() != this.size()) {
            return false;
        }

        return (group.containsAll(this) && this.containsAll(group));
    }
}