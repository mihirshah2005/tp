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

/**
 * Contains unit tests for {@link Student}.
 */
public class StudentTest {

    private static final Student ALICE = (Student) new Student.StudentBuilder().name("Alice Pauline").build();
    private static final Student BOB = (Student) new Student.StudentBuilder().name("Bob Choo").build();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new Student.StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // same name, different phone and email -> returns false
        Student editedAlice = (Student) ALICE.toBuilder()
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .address(VALID_ADDRESS_BOB)
                .tags(VALID_TAG_HUSBAND)
                .build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = (Student) ALICE.toBuilder().name(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name, different case -> returns true
        Student editedBob = (Student) BOB.toBuilder().name(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same name, trailing spaces -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = (Student) BOB.toBuilder().name(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same email, different phone -> returns true
        Student sameEmailDifferentPhone = (Student) ALICE.toBuilder().phone(VALID_PHONE_BOB).build();
        assertTrue(ALICE.isSamePerson(sameEmailDifferentPhone));

        // same phone, different email -> returns true
        Student samePhoneDifferentEmail = (Student) ALICE.toBuilder().email(VALID_EMAIL_BOB).build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmail));

        // both different phone and email -> returns false
        Student mixedAlice = (Student) ALICE.toBuilder()
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .build();
        assertFalse(ALICE.isSamePerson(mixedAlice));

        // both have default phone and email -> returns true
        Student defaultAlice = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("000")
                .email("default@email")
                .build();
        Student anotherDefaultAlice = (Student) new Student.StudentBuilder()
                .name("alice pauline")
                .phone("000")
                .email("default@email")
                .build();
        assertTrue(defaultAlice.isSamePerson(anotherDefaultAlice));

        // one has default contact info, other has real contact info -> returns false
        Student realAlice = (Student) ALICE.toBuilder()
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .build();
        Student defaultAlices = (Student) ALICE.toBuilder()
                .phone("000")
                .email("default@email")
                .build();
        assertFalse(realAlice.isSamePerson(defaultAlices));

        // this has default phone, other has real phone -> returns true
        Student thisDefaultPhone = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("000")
                .email("alice@example.com")
                .build();
        Student otherRealPhone = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("94351253")
                .email("alice@example.com")
                .build();
        assertTrue(thisDefaultPhone.isSamePerson(otherRealPhone));

        // same phone, different email -> returns true
        Student samePhoneDifferentEmails = (Student) ALICE.toBuilder()
                .email(VALID_EMAIL_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmails));

        // both default phones, but different emails -> returns false
        Student bothDefaultPhonesDiffEmail = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("000")
                .email("alice@example.com")
                .build();
        Student bothDefaultPhonesDiffEmail2 = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("000")
                .email("bob@example.com")
                .build();
        assertFalse(bothDefaultPhonesDiffEmail.isSamePerson(bothDefaultPhonesDiffEmail2));

        // both default emails, different real phones -> returns false
        Student bothDefaultEmailsDiffPhones = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("94351253")
                .email("default@email")
                .build();
        Student bothDefaultEmailsDiffPhones2 = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone("99999999")
                .email("default@email")
                .build();
        assertFalse(bothDefaultEmailsDiffPhones.isSamePerson(bothDefaultEmailsDiffPhones2));


    }

    @Test
    public void equals() {
        Student aliceCopy = ALICE.toBuilder().build();
        assertTrue(ALICE.equals(aliceCopy));
        assertTrue(ALICE.equals(ALICE));
        assertFalse(ALICE.equals(null));
        assertFalse(ALICE.equals(5));
        assertFalse(ALICE.equals(BOB));

        Student editedAlice = (Student) ALICE.toBuilder().name(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) ALICE.toBuilder().phone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) ALICE.toBuilder().email(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) ALICE.toBuilder().address(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) ALICE.toBuilder().tags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = "[Student] " + ALICE.originalToString();
        assertEquals(expected, ALICE.toString());
    }

}
