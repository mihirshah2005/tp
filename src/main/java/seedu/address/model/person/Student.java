package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the tutoring volunteer system.
 * Same fields as Person; used to distinguish between Students and Volunteers.
 */
public class Student extends Person {

    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return "[Student] " + super.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Student && super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
