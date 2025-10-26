package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

import java.util.Set;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    void pair(Person a, Person b);

    void unpair(Person a, Person b);

    boolean isPaired(Person a, Person b);

    Set<Person> getPairedPersons(Person p);
}
