package seedu.address.model.person;

import java.util.List;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the tutoring volunteer system.
 * Same fields as Person; used to distinguish between Students and Volunteers.
 */
public class Student extends Person {

    /**
     * Constructs a {@code Student}.
     * All fields must be present and not null.
     *
     * @param name    The student's name.
     * @param phone   The student's phone number.
     * @param email   The student's email address.
     * @param address The student's home address.
     * @param tags    The set of tags associated with the student.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> students) {
        super(name, phone, email, address, tags, students);
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
