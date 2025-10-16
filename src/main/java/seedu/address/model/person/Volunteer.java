package seedu.address.model.person;

import java.util.List;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Volunteer in the tutoring volunteer system.
 * Same fields as Person; used to distinguish between Students and Volunteers.
 */
public class Volunteer extends Person {

    /**
     * The Builder for the Volunteer class.
     */

    public static class VolunteerBuild extends PersonBuild<VolunteerBuild> {

        public VolunteerBuild(Name name) {
            super(name);
        }

        @Override
        public Volunteer build() {
            return new Volunteer(this);
        }
    }

    private Volunteer(VolunteerBuild builder) {
        super(builder);
    }

    /**
     * Constructs a {@code Volunteer}.
     * All fields must be present and not null.
     *
     * @param name    The volunteer's name.
     * @param phone   The volunteer's phone number.
     * @param email   The volunteer's email address.
     * @param address The volunteer's home address.
     * @param tags    The set of tags associated with the volunteer.
     * @param pairedPersons The list of persons paired with the volunteer.
     */
    public Volunteer(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> pairedPersons) {
        super(name, phone, email, address, tags, pairedPersons);
    }

    @Override
    public VolunteerBuild toBuilder(Name name) {
        VolunteerBuild volunteerBuild = new VolunteerBuild(name);
        return (VolunteerBuild) volunteerBuild.phone(this.getPhone()).email(this.getEmail()).address(this.getAddress())
                .tags(this.getTags()).pairedPersons(this.getPairedPersons());
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
