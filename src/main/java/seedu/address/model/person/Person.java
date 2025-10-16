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

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags;
    private final ObservableList<Person> pairedPersons;
    private final ObservableList<Person> unmodifiablePairedPersons;
    private final List<Person> unmodifiablePairingsView;

    /**
     * Only name must be present and not null.
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

        public PersonBuild(Name name) {
            requireAllNonNull(name);
            this.name = name;
        }

        public PersonBuild<T> phone(Phone phone) {
            if (phone != null) this.phone = phone;
            return this;
        }

        public PersonBuild<T> email(Email email) {
            if (email != null) this.email = email;
            return this;
        }

        public PersonBuild<T> address(Address address) {
            if (address != null) this.address = address;
            return this;
        }

        public PersonBuild<T> tags(Set<Tag> tags) {
            if (tags != null) this.tags = tags;
            return this;
        }

        public PersonBuild<T> pairedPersons(List<Person> pairedPersons) {
            this.pairedPersons.setAll(pairedPersons);
            FXCollections.sort(this.pairedPersons, Comparator.comparing(p -> p.getName().toString()));
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

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

    public PersonBuild<? extends PersonBuild<?>> toBuilder() {
        return toBuilder(this.name);
    }

    public PersonBuild<? extends PersonBuild<?>> toBuilder(Name name) {
        PersonBuild<? extends PersonBuild<?>> personBuild = new PersonBuild<>(name);
        return personBuild.phone(this.phone).email(this.email).address(this.address)
                .tags(this.tags).pairedPersons(this.pairedPersons);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
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

    // Setters. Relevant checks are in EditCommandParser and other relevant commands.
    public Person setName(Name name) {
        PersonBuild<? extends PersonBuild<?>> personBuild = new PersonBuild<>(name);
        return personBuild.phone(this.phone).email(this.email).address(this.address)
                .tags(this.tags).pairedPersons(this.pairedPersons).build();
    }

    public Person setPhone(Phone phone) {
        return this.toBuilder().phone(phone).build();
    }

    public Person setEmail(Email email) {
        return this.toBuilder().email(email).build();
    }

    public Person setAddress(Address address) {
        return this.toBuilder().address(address).build();
    }

    public Person setTags(Set<Tag> tags) {
        return this.toBuilder().tags(tags).build();
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
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
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
