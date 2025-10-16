package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final List<Person> personList = new ArrayList<>();

    /**
     * Only name must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, List<Person> personList) {
        requireAllNonNull(name, phone, email, address, tags, personList);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.personList.addAll(personList);
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
    public List<Person> getPersonList() {
        return Collections.unmodifiableList(personList);
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
        if (personList.contains(otherPerson)) {
            throw new IllegalValueException(String.format(REPEAT_PAIRING,
                    getName().toString(), otherPerson.getName().toString()));
        }

        personList.add(otherPerson);
        otherPerson.personList.add(this);
        personList.sort(Comparator.comparing(s -> s.getName().toString()));
        otherPerson.personList.sort(Comparator.comparing(s -> s.getName().toString()));
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
        if (!personList.contains(otherPerson)) {
            throw new IllegalValueException(String.format(REPEAT_PAIRING,
                    getName().toString(), otherPerson.getName().toString()));
        }

        personList.remove(otherPerson);
        otherPerson.personList.remove(this);

        // force personList to update
        personList.sort(Comparator.comparing(s -> s.getName().toString()));
        otherPerson.personList.sort(Comparator.comparing(s -> s.getName().toString()));

        System.out.println("personList: " + personList);
        System.out.println("otherPerson.personList: " + otherPerson.personList);
    }

    /**
     * Returns an immutable pairings list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Person> getPairings() {
        return Collections.unmodifiableList(personList);
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
        List<String> pairedNames = personList.stream()
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
