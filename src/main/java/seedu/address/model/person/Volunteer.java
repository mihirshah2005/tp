package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Volunteer in the tutoring volunteer system.
 * Same fields as Person; used to distinguish between Students and Volunteers.
 */
public class Volunteer extends Person {

    /**
     * Constructs a {@code Volunteer}.
     * All fields must be present and not null.
     *
     * @param name    The volunteer's name.
     * @param phone   The volunteer's phone number.
     * @param email   The volunteer's email address.
     * @param address The volunteer's home address.
     * @param tags    The set of tags associated with the volunteer.
     */
    public Volunteer(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return "[Volunteer] " + super.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Volunteer && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
