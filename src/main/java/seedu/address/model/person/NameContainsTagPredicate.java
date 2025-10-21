package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s tags includes the all the given tags.
 */
public class NameContainsTagPredicate implements Predicate<Person> {
    private final List<Tag> tags;

    /**
     * Constructs a {@code NameContainsTagPredicate}
     */
    public NameContainsTagPredicate(List<Tag> tags) {
        requireNonNull(tags);
        this.tags = List.copyOf(tags);
        // Make a copy to ensure that modifying object passed into parameter does not
        // result in unexpected side effects in this predicate
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().containsAll(tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsTagPredicate)) {
            return false;
        }

        NameContainsTagPredicate otherNameContainsTagPredicate = (NameContainsTagPredicate) other;
        return tags.equals(otherNameContainsTagPredicate.tags);
    }

    @Override
    public int hashCode() {
        return tags.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tags).toString();
    }
}
