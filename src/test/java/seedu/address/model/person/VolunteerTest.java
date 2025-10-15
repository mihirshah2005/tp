package seedu.address.model.person;

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

import org.junit.jupiter.api.Test;

import seedu.address.testutil.VolunteerBuilder;

/**
 * Contains unit tests for {@link Volunteer}.
 */
public class VolunteerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Volunteer volunteer = new VolunteerBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> volunteer.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        Volunteer alice = new VolunteerBuilder(ALICE).build();
        assertTrue(alice.isSamePerson(alice));

        // null -> returns false
        assertFalse(alice.isSamePerson(null));

        // same name, all other attributes different -> returns false
        Volunteer editedAlice = new VolunteerBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(alice.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(alice.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Volunteer editedBob = new VolunteerBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(editedBob.isSamePerson(BOB));

        // same phone, different email -> returns true
        Volunteer samePhoneDifferentEmail = new VolunteerBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertTrue(samePhoneDifferentEmail.isSamePerson(ALICE));

        // both have different phone and email -> returns false
        Volunteer completelyDifferent = new VolunteerBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertFalse(alice.isSamePerson(completelyDifferent));

        // same email, different phone -> returns true
        Volunteer sameEmailDifferentPhone = new VolunteerBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .build();
        assertTrue(alice.isSamePerson(sameEmailDifferentPhone));

        // one has default contact info, other has real contact info -> returns false
        Volunteer realVolunteer = new VolunteerBuilder(BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        Volunteer defaultVolunteer = new VolunteerBuilder(BOB).withPhone("000").withEmail("default@email").build();
        assertFalse(realVolunteer.isSamePerson(defaultVolunteer));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new VolunteerBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(editedBob.isSamePerson(BOB));

        // both have default phone and email -> returns true
        Volunteer defaultVolunteer1 = new VolunteerBuilder()
                .withName("Alice Pauline")
                .withPhone("000")
                .withEmail("default@email")
                .build();
        Volunteer defaultVolunteer2 = new VolunteerBuilder()
                .withName("alice pauline")
                .withPhone("000")
                .withEmail("default@email")
                .build();
        assertTrue(defaultVolunteer1.isSamePerson(defaultVolunteer2));


    }

    @Test
    public void equals() {
        Volunteer alice = new VolunteerBuilder(ALICE).build();
        Volunteer aliceCopy = new VolunteerBuilder(ALICE).build();
        Volunteer bob = new VolunteerBuilder(BOB).build();

        // same values -> returns true
        assertTrue(alice.equals(aliceCopy));

        // same object -> returns true
        assertTrue(alice.equals(alice));

        // null -> returns false
        assertFalse(alice.equals(null));

        // different type -> returns false
        assertFalse(alice.equals(5));

        // different volunteer -> returns false
        assertFalse(alice.equals(bob));

        // different name -> returns false
        Volunteer editedAlice = new VolunteerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different email -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different address -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(alice.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        Volunteer alice = new VolunteerBuilder(ALICE).build();
        String expected = "[Volunteer] " + alice.originalToString();
        assertEquals(expected, alice.toString());
    }
}
