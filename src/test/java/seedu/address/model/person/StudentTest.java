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

import org.junit.jupiter.api.Test;

import seedu.address.testutil.StudentBuilder;

/**
 * Contains unit tests for {@link Student}.
 */
public class StudentTest {

    private static final Student ALICE = new StudentBuilder().withName("Alice Pauline").build();
    private static final Student BOB = new StudentBuilder().withName("Bob Choo").build();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, different phone and email -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name, different case -> returns true
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same name, trailing spaces -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same email, different phone -> returns true
        Student sameEmailDifferentPhone = new StudentBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(sameEmailDifferentPhone));

        // same phone, different email -> returns true
        Student samePhoneDifferentEmail = new StudentBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmail));

        // both different phone and email -> returns false
        Student mixedAlice = new StudentBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertFalse(ALICE.isSamePerson(mixedAlice));

        // both have default phone and email -> returns true
        Student defaultAlice = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("000")
                .withEmail("default@email")
                .build();
        Student anotherDefaultAlice = new StudentBuilder()
                .withName("alice pauline")
                .withPhone("000")
                .withEmail("default@email")
                .build();
        assertTrue(defaultAlice.isSamePerson(anotherDefaultAlice));

        // one has default contact info, other has real contact info -> returns false
        Student realAlice = new StudentBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        Student defaultAlices = new StudentBuilder(ALICE)
                .withPhone("000")
                .withEmail("default@email")
                .build();
        assertFalse(realAlice.isSamePerson(defaultAlices));

        // this has default phone, other has real phone -> returns false
        Student thisDefaultPhone = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("000")
                .withEmail("alice@example.com")
                .build();
        Student otherRealPhone = new StudentBuilder(ALICE)
                .build();
        assertFalse(thisDefaultPhone.isSamePerson(otherRealPhone));

        // same phone, different email -> returns true
        Student samePhoneDifferentEmails = new StudentBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmails));

        // both default phones, but different emails -> returns false
        Student bothDefaultPhonesDiffEmail = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("000")
                .withEmail("alice@example.com")
                .build();
        Student bothDefaultPhonesDiffEmail2 = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("000")
                .withEmail("bob@example.com")
                .build();
        assertFalse(bothDefaultPhonesDiffEmail.isSamePerson(bothDefaultPhonesDiffEmail2));

        // both default emails, different real phones -> returns false
        Student bothDefaultEmailsDiffPhones = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("default@email")
                .build();
        Student bothDefaultEmailsDiffPhones2 = new StudentBuilder()
                .withName("Alice Pauline")
                .withPhone("99999999")
                .withEmail("default@email")
                .build();
        assertFalse(bothDefaultEmailsDiffPhones.isSamePerson(bothDefaultEmailsDiffPhones2));


    }

    @Test
    public void equals() {
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        assertTrue(ALICE.equals(ALICE));
        assertFalse(ALICE.equals(null));
        assertFalse(ALICE.equals(5));
        assertFalse(ALICE.equals(BOB));

        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = "[Student] " + ALICE.originalToString();
        assertEquals(expected, ALICE.toString());
    }

}
