package seedu.address.model.person;

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
        public StudentBuilder() {
            super();
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
    public StudentBuilder toBuilder() {
        return (StudentBuilder) new StudentBuilder()
                .name(this.getName())
                .phone(this.getPhone())
                .email(this.getEmail())
                .address(this.getAddress())
                .tags(this.getTags())
                .pairedPersons(this.getPairedPersons());
    }

    public static StudentBuilder toBuilder(PersonBuilder personBuilder) {
        return (StudentBuilder) new StudentBuilder()
                .name(personBuilder.getName())
                .phone(personBuilder.getPhone())
                .email(personBuilder.getEmail())
                .address(personBuilder.getAddress())
                .tags(personBuilder.getTags())
                .pairedPersons(personBuilder.getPairedPersons());
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
