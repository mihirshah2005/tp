package seedu.address.model.person;

/**
 * Represents a Volunteer in the tutoring volunteer system.
 * Same fields as Person; used to distinguish between Students and Volunteers.
 */
public class Volunteer extends Person {

    /**
     * The Builder for the Volunteer class.
     */
    public static class VolunteerBuilder extends Person.PersonBuilder {

        /**
         * Constructor for a VolunteerBuilder object.
         */
        public VolunteerBuilder() {
            super();
        }

        /**
         * Returns a Volunteer object with the parameter values of the Builder.
         */
        @Override
        public Volunteer build() {
            return new Volunteer(this);
        }
    }

    private Volunteer(VolunteerBuilder builder) {
        super(builder);
    }

    @Override
    public VolunteerBuilder toBuilder() {
        return (VolunteerBuilder) new VolunteerBuilder()
                .name(this.getName())
                .phone(this.getPhone())
                .email(this.getEmail())
                .address(this.getAddress())
                .tags(this.getTags());
    }

    /**
     * Converts the PersonBuilder object to a VolunteerBuilder object.
     */
    public static VolunteerBuilder toBuilder(PersonBuilder personBuilder) {
        return (VolunteerBuilder) new VolunteerBuilder()
                .name(personBuilder.getName())
                .phone(personBuilder.getPhone())
                .email(personBuilder.getEmail())
                .address(personBuilder.getAddress())
                .tags(personBuilder.getTags());
    }

    /**
     * Converts the Person object to a VolunteerBuilder object.
     */
    public static VolunteerBuilder toBuilder(Person person) {
        return (VolunteerBuilder) new VolunteerBuilder()
                .name(person.getName())
                .phone(person.getPhone())
                .email(person.getEmail())
                .address(person.getAddress())
                .tags(person.getTags());
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
