package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.testutil.PersonBuilder;


public class PersonTest {
    @Test
    public void getPairedPersons_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> ALICE.getPairedPersons().clear());
    }

    @Test
    public void getPairedPersons_noPairings_returnsEmptyList() {
        Person alice = new PersonBuilder(ALICE).build();
        assertTrue(alice.getPairedPersons().isEmpty());
    }

    @Test
    public void getPairedPersons_withPairings_returnsCorrectList() {
        Person alice = new PersonBuilder(ALICE).build();
        Person bob = new PersonBuilder(BOB).build();
        assertDoesNotThrow(() -> alice.addPerson(bob));
        assertEquals(1, alice.getPairedPersons().size());
        assertTrue(alice.getPairedPersons().contains(bob));
    }

    @Test
    public void addPerson() {
        Person alice = new PersonBuilder(ALICE).build();
        Person bob = new PersonBuilder(BOB).build();
        assertDoesNotThrow(() -> alice.addPerson(bob));
        assertThrows(IllegalValueException.class, () -> alice.addPerson(bob));
        assertThrows(IllegalValueException.class, () -> bob.addPerson(alice));
        assertDoesNotThrow(() -> alice.addPerson(CARL));
    }

    @Test
    public void removePerson() {
        Person alice = new PersonBuilder(ALICE).build();
        Person bob = new PersonBuilder(BOB).build();
        assertDoesNotThrow(() -> alice.addPerson(bob));
        assertDoesNotThrow(() -> bob.removePerson(alice));
        assertThrows(IllegalValueException.class, () -> CARL.removePerson(alice));
        assertThrows(IllegalValueException.class, () -> CARL.removePerson(CARL));
    }

    @Test
    public void isSamePerson() {

        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // same name and same phone -> returns true
        Person editedAlice = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB) // different email, same name + phone
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same name and same email -> returns true
        editedAlice = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB) // different phone, same name + email
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same name, different phone AND different email -> returns false
        editedAlice = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name but different case -> returns true (case-insensitive)
        Person editedBob = new PersonBuilder(BOB)
                .withName(VALID_NAME_BOB.toLowerCase())
                .build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same name but with trailing spaces -> returns true (trimmed)
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB)
                .withName(nameWithTrailingSpaces)
                .build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String pairedNames = ALICE.getPairedPersons().stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.toList())
                .toString();
        String expected = "[Student] seedu.address.model.person.Student{name=Alice Pauline, phone=94351253,"
                + " email=alice@example.com, address=123, Jurong West Ave 6, #08-111, tags=[[friends]], "
                + "pairings=" + pairedNames + "}";
        assertEquals(expected, ALICE.toString());
    }
}
