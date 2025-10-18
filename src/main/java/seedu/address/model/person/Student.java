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
    public static class StudentBuilder extends Person.PersonBuilder {

        /**
         * Constructor for a StudentBuilder object.
         */
        public StudentBuilder(Name name) {
            super(name);
        }

        /**
         * Returns a Student object with the parameter values of the Builder.
         */
        @Override
        public Student build() {
            return new Student(this);
        }
    }

    private Student(StudentBuilder builder) {
        super(builder);
    }

    @Override
    public StudentBuilder toBuilder(Name name) {
        StudentBuilder studentBuild = new StudentBuilder(name);
        return (StudentBuilder) studentBuild
                .phone(this.getPhone())
                .email(this.getEmail())
                .address(this.getAddress())
                .tags(this.getTags())
                .pairedPersons(this.getPairedPersons());
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
