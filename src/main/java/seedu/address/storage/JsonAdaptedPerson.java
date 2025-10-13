package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Student;
import seedu.address.model.person.Volunteer;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final String UNSUPPORTED_TYPE_MESSAGE = "Unsupported or missing entry type: " +
            "%s (expected 'student' or 'volunteer')";


    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String type;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedPairingRef> pairings = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("pairings") List<JsonAdaptedPairingRef> pairings,
                             @JsonProperty("type") String type) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.type = type;

        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (pairings != null) {
            this.pairings.addAll(pairings);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .toList());
        pairings.addAll(source.getPairings().stream()
                .map(JsonAdaptedPairingRef::new)
                .toList());
        if (source instanceof Student) {
            type = "student";
        } else if (source instanceof Volunteer) {
            type = "volunteer";
        } else {
            throw new IllegalArgumentException("Only Student or Volunteer can be persisted");
        }
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (type == null || type.isBlank()) {
            throw new IllegalValueException(String.format(UNSUPPORTED_TYPE_MESSAGE, type));
        }
        final String normalized = type.toLowerCase();
        final Set<Tag> modelTags = new HashSet<>(personTags);

        switch (normalized) {

        case "student":
            return new Student(modelName, modelPhone, modelEmail, modelAddress, modelTags);
        case "volunteer":
            return new Volunteer(modelName, modelPhone, modelEmail, modelAddress, modelTags);
        default:
            throw new IllegalValueException(String.format(UNSUPPORTED_TYPE_MESSAGE, type));
        }
    }

    /**
     * Returns the list of pairing identities declared in the JSON payload.
     */
    public List<PersonIdentity> getPairingIdentities() throws IllegalValueException {
        List<PersonIdentity> identities = new ArrayList<>();
        for (JsonAdaptedPairingRef pairing : pairings) {
            identities.add(pairing.toIdentity());
        }
        return identities;
    }

    /**
     * Jackson-friendly representation of a pairing reference by identity fields only.
     */
    static class JsonAdaptedPairingRef {
        private final String name;
        private final String phone;

        @JsonCreator
        JsonAdaptedPairingRef(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone) {
            this.name = name;
            this.phone = phone;
        }

        JsonAdaptedPairingRef(Person person) {
            this.name = person.getName().fullName;
            this.phone = person.getPhone().value;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("phone")
        public String getPhone() {
            return phone;
        }

        PersonIdentity toIdentity() throws IllegalValueException {
            return new PersonIdentity(name, phone);
        }
    }

    /**
     * Identity tuple for linking persons by core fields.
     */
    public static class PersonIdentity {
        private final String name;
        private final String phone;

        PersonIdentity(String name, String phone) throws IllegalValueException {
            if (name == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                        Name.class.getSimpleName()));
            }
            if (!Name.isValidName(name)) {
                throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
            }
            if (phone == null) {
                throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                        Phone.class.getSimpleName()));
            }
            if (!Phone.isValidPhone(phone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            this.name = name;
            this.phone = phone;
        }

        public String getNameValue() {
            return name;
        }

        public String getPhoneValue() {
            return phone;
        }

        public String toLookupKey() {
            return name + "|" + phone;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof PersonIdentity)) {
                return false;
            }
            PersonIdentity otherIdentity = (PersonIdentity) other;
            return name.equals(otherIdentity.name) && phone.equals(otherIdentity.phone);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, phone);
        }
    }

}
