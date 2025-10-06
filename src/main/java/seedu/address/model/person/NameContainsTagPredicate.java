package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s tags includes the given tag.
 */
public class NameContainsTagPredicate implements Predicate<Person> {
    private final Tag tag;

    /**
     * Constructs a {@code NameContainsTagPredicate}
     */
    public NameContainsTagPredicate(Tag tag) {
        requireNonNull(tag);
        this.tag = tag;
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().contains(tag);
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
        return tag.equals(otherNameContainsTagPredicate.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tag", tag).toString();
    }
}
