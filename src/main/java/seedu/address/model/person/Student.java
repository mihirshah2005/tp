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
     * The Builder for the Student class.
     */

    public static class StudentBuild extends PersonBuild<StudentBuild> {

        public StudentBuild(Name name) {
            super(name);
        }

        @Override
        public Student build() {
            return new Student(this);
        }
    }

    private Student(StudentBuild builder) {
        super(builder);
    }

    public Student(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> pairedPersons) {
        super(name, phone, email, address, tags, pairedPersons);
    }

    @Override
    public StudentBuild toBuilder(Name name) {
        StudentBuild studentBuild = new StudentBuild(name);
        return (StudentBuild) studentBuild.phone(this.getPhone()).email(this.getEmail()).address(this.getAddress())
                .tags(this.getTags()).pairedPersons(this.getPairedPersons());
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
