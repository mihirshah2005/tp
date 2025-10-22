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

/**
 * Contains unit tests for {@link Volunteer}.
 */
public class VolunteerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Volunteer volunteer = (Volunteer) new Volunteer.VolunteerBuilder().name("new").build();
        assertThrows(UnsupportedOperationException.class, () -> volunteer.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        Volunteer alice = (Volunteer) Volunteer.toBuilder(ALICE).build();
        assertTrue(alice.isSamePerson(alice));

        // same name, all other attributes different -> returns false
        Volunteer editedAlice = (Volunteer) Volunteer.toBuilder(ALICE)
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .address(VALID_ADDRESS_BOB)
                .tags(VALID_TAG_HUSBAND)
                .build();
        assertFalse(alice.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).name(VALID_NAME_BOB).build();
        assertFalse(alice.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Volunteer editedBob = (Volunteer) Volunteer.toBuilder(BOB).name(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(editedBob.isSamePerson(BOB));

        // same phone, different email -> returns true
        Volunteer samePhoneDifferentEmail = (Volunteer) Volunteer.toBuilder(ALICE)
                .email(VALID_EMAIL_BOB)
                .build();
        assertTrue(samePhoneDifferentEmail.isSamePerson(ALICE));

        // both have different phone and email -> returns false
        Volunteer completelyDifferent = (Volunteer) Volunteer.toBuilder(ALICE)
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .build();
        assertFalse(alice.isSamePerson(completelyDifferent));

        // same email, different phone -> returns true
        Volunteer sameEmailDifferentPhone = (Volunteer) Volunteer.toBuilder(ALICE)
                .phone(VALID_PHONE_BOB)
                .build();
        assertTrue(alice.isSamePerson(sameEmailDifferentPhone));

        // one has default contact info, other has real contact info -> returns false
        Volunteer realVolunteer = (Volunteer) Volunteer.toBuilder(BOB)
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .build();
        Volunteer defaultVolunteer = (Volunteer) Volunteer.toBuilder(BOB)
                .phone("000")
                .email("default@email")
                .build();
        assertFalse(realVolunteer.isSamePerson(defaultVolunteer));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = (Volunteer) Volunteer.toBuilder(BOB).name(nameWithTrailingSpaces).build();
        assertTrue(editedBob.isSamePerson(BOB));

        // both have default phone and email -> returns true
        Volunteer defaultVolunteer1 = (Volunteer) new Volunteer.VolunteerBuilder()
                .name("Alice Pauline")
                .phone("000")
                .email("default@email")
                .build();
        Volunteer defaultVolunteer2 = (Volunteer) new Volunteer.VolunteerBuilder()
                .name("alice pauline")
                .phone("000")
                .email("default@email")
                .build();
        assertTrue(defaultVolunteer1.isSamePerson(defaultVolunteer2));


    }

    @Test
    public void equals() {
        Volunteer alice = Volunteer.toBuilder(ALICE).build();
        Volunteer aliceCopy = Volunteer.toBuilder(ALICE).build();
        Volunteer bob = Volunteer.toBuilder(BOB).build();

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
        Volunteer editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).name(VALID_NAME_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different phone -> returns false
        editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).phone(VALID_PHONE_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different email -> returns false
        editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).email(VALID_EMAIL_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different address -> returns false
        editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).address(VALID_ADDRESS_BOB).build();
        assertFalse(alice.equals(editedAlice));

        // different tags -> returns false
        editedAlice = (Volunteer) Volunteer.toBuilder(ALICE).tags(VALID_TAG_HUSBAND).build();
        assertFalse(alice.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        Volunteer alice = Volunteer.toBuilder(ALICE).build();
        String expected = "[Volunteer] " + alice.originalToString();
        assertEquals(expected, alice.toString());
    }
}
