package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    public static final String SELF_PAIRING = "Person cannot be paired with themselves.";
    public static final String REPEAT_PAIRING = "{} and {} already paired.";

    public static final String DEFAULT_PHONE = "000";
    public static final String DEFAULT_EMAIL = "default@email";

    // Identity fields
    private Name name;
    private Phone phone;
    private Email email;

    // Data fields
    private Address address;
    private Set<Tag> tags;
    private final ObservableList<Person> pairedPersons;
    private final ObservableList<Person> unmodifiablePairedPersons;
    private final List<Person> unmodifiablePairingsView;

    /**
     * The Builder for the Person class.
     */
    public static class PersonBuild<T extends PersonBuild<T>> {
        // Required parameters
        private final Name name;

        // Optional parameters - initialized to default values
        private Phone phone = new Phone("000");
        private Email email = new Email("default@email");
        private Address address = new Address("Default Address");
        private Set<Tag> tags = new HashSet<>();
        private ObservableList<Person> pairedPersons = FXCollections.observableArrayList();

        /**
         * Constructor for PersonBuild.
         */
        public PersonBuild(Name name) {
            requireAllNonNull(name);
            this.name = name;
        }

        /**
         * Setter for the phone parameter.
         */
        public PersonBuild<T> phone(Phone phone) {
            if (phone != null) {
                this.phone = phone;
            }
            return this;
        }

        /**
         * Setter for the email parameter.
         */
        public PersonBuild<T> email(Email email) {
            if (email != null) {
                this.email = email;
            }
            return this;
        }

        /**
         * Setter for the address parameter.
         */
        public PersonBuild<T> address(Address address) {
            if (address != null) {
                this.address = address;
            }
            return this;
        }

        /**
         * Setter for the tags parameter.
         */
        public PersonBuild<T> tags(Set<Tag> tags) {
            if (tags != null) {
                this.tags = tags;
            }
            return this;
        }

        /**
         * Setter for the pairedPersons parameter.
         */
        public PersonBuild<T> pairedPersons(List<Person> pairedPersons) {
            this.pairedPersons.setAll(pairedPersons);
            FXCollections.sort(this.pairedPersons, Comparator.comparing(p -> p.getName().toString()));
            return this;
        }

        /**
         * Returns a Person object with the parameter values of the Builder.
         */
        public Person build() {
            return new Person(this);
        }
    }

    /**
     * Constructor for a Person object using the builder. This is the intended method of constructing
     * the person object via the Builder pattern.
     */
    public Person(PersonBuild<? extends PersonBuild<?>> builder) {
        requireAllNonNull(builder.name, builder.phone, builder.email, builder.address,
                builder.tags, builder.pairedPersons);
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
        this.address = builder.address;
        this.tags = builder.tags;
        this.pairedPersons = builder.pairedPersons;
        this.unmodifiablePairedPersons = FXCollections.unmodifiableObservableList(pairedPersons);
        this.unmodifiablePairingsView = Collections.unmodifiableList(pairedPersons);
    }

    /**
     * Constructor for a Person object. We can try removing this in future iterations as
     * it is not strictly necessary.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> pairedPersons) {
        PersonBuild<?> builder = new PersonBuild<>(name).phone(phone).email(email).address(address)
                .tags(tags).pairedPersons(pairedPersons);
        requireAllNonNull(builder.name, builder.phone, builder.email, builder.address,
                builder.tags, builder.pairedPersons);
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
        this.address = builder.address;
        this.tags = builder.tags;
        this.pairedPersons = builder.pairedPersons;
        this.unmodifiablePairedPersons = FXCollections.unmodifiableObservableList(builder.pairedPersons);
        this.unmodifiablePairingsView = Collections.unmodifiableList(builder.pairedPersons);
    }

    /**
     * Converts the Person back to Builder form so that it can be easily modified.
     */
    public PersonBuild<? extends PersonBuild<?>> toBuilder() {
        return toBuilder(this.name);
    }

    /**
     * Converts the Person back to Builder form so that it can be easily modified.
     * The name can be passed as a parameter as it is final and cannot be modified afterward.
     */
    public PersonBuild<? extends PersonBuild<?>> toBuilder(Name name) {
        PersonBuild<? extends PersonBuild<?>> personBuild = new PersonBuild<>(name);
        return personBuild.phone(this.phone).email(this.email).address(this.address)
                .tags(this.tags).pairedPersons(this.pairedPersons);
    }

    /**
     * Returns the name.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the phone number.
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the email.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Trims and converts the given name to lowercase for consistent comparison.
     */
    private String normalizeName(String name) {
        return name.trim().toLowerCase();
    }

    /**
     * Trims and converts the given email to lowercase for consistent comparison.
     */
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    /**
     * Trims the given phone number for consistent comparison.
     */
    private String normalizePhone(String phone) {
        return phone.trim();
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Person> getPairedPersons() {
        return unmodifiablePairingsView;
    }

    /**
     * Returns a new Person object with the specified name.
     * Relevant checks are in EditCommandParser and other relevant commands.
     */
    public Person setName(Name name) {
        this.name = name;
        return this;
    }

    /**
     * Returns a new Person object with the specified phone.
     */
    public Person setPhone(Phone phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Returns a new Person object with the specified email.
     */
    public Person setEmail(Email email) {
        this.email = email;
        return this;
    }

    /**
     * Returns a new Person object with the specified address.
     */
    public Person setAddress(Address address) {
        this.address = address;
        return this;
    }

    /**
     * Returns a new Person object with the specified tags.
     */
    public Person setTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Adds a Person pair. Only call this on one of the two partners in each pair.
     * For example, after running {@code person1.addPerson(person2)},
     * you should not then run {@code `person2.addPerson(person1)}.
     */
    public void addPerson(Person otherPerson) throws IllegalValueException {
        if (otherPerson == this) {
            throw new IllegalValueException(SELF_PAIRING);
        }
        if (pairedPersons.contains(otherPerson)) {
            throw new IllegalValueException(String.format(REPEAT_PAIRING,
                    getName().toString(), otherPerson.getName().toString()));
        }

        pairedPersons.add(otherPerson);
        otherPerson.pairedPersons.add(this);
        FXCollections.sort(pairedPersons, Comparator.comparing(s -> s.getName().toString()));
        FXCollections.sort(otherPerson.pairedPersons, Comparator.comparing(s -> s.getName().toString()));
    }

    /**
     * Adds a Person pair. Only call this on one of the two partners in each pair.
     * For example, after running {@code person1.addPerson(person2)},
     * you should not then run {@code `person2.addPerson(person1)}.
     */
    public void removePerson(Person otherPerson) throws IllegalValueException {
        if (otherPerson == this) {
            throw new IllegalValueException(SELF_PAIRING);
        }
        if (!pairedPersons.contains(otherPerson)) {
            throw new IllegalValueException(String.format(REPEAT_PAIRING,
                    getName().toString(), otherPerson.getName().toString()));
        }

        pairedPersons.remove(otherPerson);
        otherPerson.pairedPersons.remove(this);

        FXCollections.sort(pairedPersons, Comparator.comparing(s -> s.getName().toString()));
        FXCollections.sort(otherPerson.pairedPersons, Comparator.comparing(s -> s.getName().toString()));
    }

    /**
     * Returns an immutable pairings list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public ObservableList<Person> getPairings() {
        return unmodifiablePairedPersons;
    }

    /**
     * Returns true if this person and the given person are considered the same.
     * Two persons are the same if they have the same name (case-insensitive),
     * and either share the same real phone or email, or both have default contact details.
     *
     * @param otherPerson the other person to compare with
     * @return true if both represent the same person, false otherwise
     */
    public boolean isSamePerson(Person otherPerson) {
        Objects.requireNonNull(otherPerson);
        if (otherPerson == this) {
            return true;
        }

        // normalize names (case-insensitive, trimmed)
        String thisName = normalizeName(getName().fullName);
        String otherName = normalizeName(otherPerson.getName().fullName);
        if (!thisName.equals(otherName)) {
            return false;
        }

        // normalize contact values
        String thisPhone = normalizePhone(getPhone().value);
        String otherPhone = normalizePhone(otherPerson.getPhone().value);
        String thisEmail = normalizeEmail(getEmail().value);
        String otherEmail = normalizeEmail(otherPerson.getEmail().value);


        // identify real contacts
        boolean thisHasRealPhone = !thisPhone.equals(DEFAULT_PHONE);
        boolean otherHasRealPhone = !otherPhone.equals(DEFAULT_PHONE);
        boolean thisHasRealEmail = !thisEmail.equals(DEFAULT_EMAIL);
        boolean otherHasRealEmail = !otherEmail.equals(DEFAULT_EMAIL);

        // compare only when both sides have real data
        boolean isSamePhone = thisHasRealPhone && otherHasRealPhone && thisPhone.equals(otherPhone);
        boolean isSameEmail = thisHasRealEmail && otherHasRealEmail && thisEmail.equals(otherEmail);

        // if both phones default & same; if both emails default → same
        boolean bothPhonesDefault = !thisHasRealPhone && !otherHasRealPhone;
        boolean bothEmailsDefault = !thisHasRealEmail && !otherHasRealEmail;
        boolean bothPhoneAndEmailDefault = bothPhonesDefault && bothEmailsDefault;
        // same name AND (
        //    same real phone OR same real email OR both phones default OR both emails default
        // )
        return isSamePhone || isSameEmail || bothPhoneAndEmailDefault;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person otherPerson = (Person) other;
        // Intentionally EXCLUDE personList to avoid deep / cyclic recursion when pairings are mutual.
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // Exclude personList for consistency with equals (prevents cyclic recursion / stack overflow)
        return Objects.hash(name, phone, email, address, tags);
    }

    /**
     * Returns a string representation of the Person and not its subclasses for testing.
     */
    public String originalToString() {
        // Avoid printing entire personList graph (can be cyclic). Show only paired names for debugging.
        List<String> pairedNames = pairedPersons.stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.toList());
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("pairings", pairedNames)
                .toString();
    }

    @Override
    public String toString() {
        return originalToString();
    }

}
