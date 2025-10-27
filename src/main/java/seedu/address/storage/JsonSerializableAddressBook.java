package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonPairing> pairings = new ArrayList<>();

    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("pairings") List<JsonPairing> pairings) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (pairings != null) {
            this.pairings.addAll(pairings);
        }
    }

    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        List<Person> list = source.getPersonList();
        persons.addAll(list.stream().map(JsonAdaptedPerson::new).toList());

        // index persons by position to emit unique edges
        Map<Person, Integer> index = new IdentityHashMap<>();
        for (int k = 0; k < list.size(); k++) {
            index.put(list.get(k), k);
        }

        Set<Long> seen = new HashSet<>();
        for (int a = 0; a < list.size(); a++) {
            for (Person q : source.getPairedPersons(list.get(a))) {
                Integer b = index.get(q);
                if (b == null) {
                    continue;
                }
                int x = Math.min(a, b);
                int y = Math.max(a, b);
                long key = (((long) x) << 32) ^ (y & 0xffffffffL);
                if (seen.add(key)) {
                    if (!list.get(x).getType().equals(list.get(y).getType())) {
                        pairings.add(new JsonPairing(x, y));
                    } else {
                        Logger.getGlobal().warning("Ignoring and not loading pairing between two persons"
                                + " of the same type: " + list.get(x) + " and " + list.get(y));
                    }
                } else {
                    Logger.getGlobal().warning("Ignoring and not loading duplicate pairing: "
                            + list.get(x) + " and " + list.get(y));
                }
            }
        }
    }

    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedPerson jap : persons) {
            Person p = jap.toModelType();
            if (addressBook.hasPerson(p)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(p);
        }

        var loaded = addressBook.getPersonList();
        for (JsonPairing pr : pairings) {
            int a = pr.i;
            int b = pr.j;
            if (a < 0 || b < 0 || a >= loaded.size() || b >= loaded.size()) {
                Logger.getGlobal().warning("Ignoring and not saving pairing where at least either"
                        + " of the 2 indices are out of bounds: " + a + " and " + b);
                continue;
            }
            if (a == b) {
                Logger.getGlobal().warning("Ignoring and not saving self-pairing: " + a);
                continue;
            }

            Person personA = loaded.get(a);
            Person personB = loaded.get(b);
            if (personA.getType().equals(personB.getType())) {
                Logger.getGlobal().warning("Ignoring and not saving pairing between two persons of the same type: "
                        + personA + " and " + personB);
            } else {
                addressBook.pair(personA, personB);
            }
        }
        return addressBook;
    }
}

