package seedu.address.logic.commands.comparators;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Comparator which sorts a collection of persons based on
 * how many of the tags the user searched for is present in
 * the person's tags.
 */
public class FindTagComparator implements Comparator<Person> {
    private final Set<Tag> tagsSearchedFor;

    /**
     * Constructs a comparator to sort persons based on how many of their
     * tags are in the given set {@code tagsSearchedFor}.
     */
    public FindTagComparator(Set<Tag> tagsSearchedFor) {
        requireNonNull(tagsSearchedFor);
        this.tagsSearchedFor = Set.copyOf(tagsSearchedFor);
    }

    /**
     * Returns set of tags present in both the set of tags searched for
     * and the provided {@code tagsOfPerson}.
     *
     * <p>Method will not modify either the set in this comparator or the set in the
     * parameter.</p>
     */
    private Set<Tag> getMatchingTags(Set<Tag> tagsOfPerson) {
        requireNonNull(tagsOfPerson);
        // Solution below inspired by https://stackoverflow.com/a/8882126
        Set<Tag> matchingTags = new HashSet<>(tagsSearchedFor);
        matchingTags.retainAll(tagsOfPerson);
        return matchingTags;
    }

    private int getNumMatchingTags(Set<Tag> tagsOfPerson) {
        return getMatchingTags(tagsOfPerson).size();
    }

    // Javadoc comment adapted from that of Comparator#compare
    /**
     * Compares two persons based on how many of their tags matches
     * this comparator's set of tags. Persons are sorted in descending order
     * of the number of tags matching.
     *
     * @param o1 the first person to be compared.
     * @param o2 the second person to be compared.
     * @return a negative integer, zero, or a positive integer as the first person
     *         has less, the same, or more tags matching than the second.
     */
    @Override
    public int compare(Person o1, Person o2) {
        return Integer.compare(getNumMatchingTags(o2.getTags()), getNumMatchingTags(o1.getTags()));
        // Note: order of o1 and o2 are intentionally reversed
        // because we want to sort in descending order of no. of matching tags
        // This swap is inspired by https://stackoverflow.com/a/18206740
    }
}
