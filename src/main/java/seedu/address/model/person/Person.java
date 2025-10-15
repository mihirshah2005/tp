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
    private Name name;
    private Phone phone;
    private Email email;

    // Data fields
    private Address address;
    private Set<Tag> tags = new HashSet<>();
    private final ObservableList<Person> pairedPersons = FXCollections.observableArrayList();
    private final ObservableList<Person> unmodifiablePairedPersons =
        FXCollections.unmodifiableObservableList(pairedPersons);
    private final List<Person> unmodifiablePairingsView = Collections.unmodifiableList(pairedPersons);

    /**
     * Only name must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> pairedPersons) {
        requireAllNonNull(name, phone, email, address, tags, pairedPersons);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.pairedPersons.setAll(pairedPersons);
        FXCollections.sort(this.pairedPersons, Comparator.comparing(p -> p.getName().toString()));
    }

    /**
     * Convenience constructor that defaults personList to empty list (no pairings yet).
     * Maintains backward compatibility for code that previously did not supply pairings.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
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
    public void setName(Name name) {
        this.name = name;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
