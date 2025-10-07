package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Volunteer;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Volunteer objects.
 */
public class VolunteerBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@example.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    public VolunteerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the VolunteerBuilder with the data of {@code personToCopy}.
     */
    public VolunteerBuilder(seedu.address.model.person.Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
    }

    public VolunteerBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    public VolunteerBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    public VolunteerBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public VolunteerBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    public VolunteerBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Volunteer build() {
        return new Volunteer(name, phone, email, address, tags);
    }
}
