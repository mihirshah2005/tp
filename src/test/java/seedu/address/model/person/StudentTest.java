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
 * Contains unit tests for {@link Student}.
 */
public class StudentTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = (Student) new Student.StudentBuilder().name("new").build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // same name, different phone and email -> returns false
        Student editedAlice = (Student) Student.toBuilder(ALICE)
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .address(VALID_ADDRESS_BOB)
                .tags(VALID_TAG_HUSBAND)
                .build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = (Student) Student.toBuilder(ALICE).name(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name, different case -> returns true
        Student editedBob = (Student) Student.toBuilder(BOB).name(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same name, trailing spaces -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = (Student) Student.toBuilder(BOB).name(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same email, different phone -> returns true
        Student sameEmailDifferentPhone = (Student) Student.toBuilder(ALICE).phone(VALID_PHONE_BOB).build();
        assertTrue(ALICE.isSamePerson(sameEmailDifferentPhone));

        // same phone, different email -> returns true
        Student samePhoneDifferentEmail = (Student) Student.toBuilder(ALICE).email(VALID_EMAIL_BOB).build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmail));

        // both different phone and email -> returns false
        Student mixedAlice = (Student) Student.toBuilder(ALICE)
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
        Student realAlice = (Student) Student.toBuilder(ALICE)
                .phone(VALID_PHONE_BOB)
                .email(VALID_EMAIL_BOB)
                .build();
        Student defaultAlices = (Student) Student.toBuilder(ALICE)
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
        Student samePhoneDifferentEmails = (Student) Student.toBuilder(ALICE)
                .email(VALID_EMAIL_BOB)
                .build();
        assertTrue(ALICE.isSamePerson(samePhoneDifferentEmails));

        // both default phones, but different emails -> returns false
        Student bothDefaultPhonesDiffEmail = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone(Person.DEFAULT_PHONE)
                .email("alice@example.com")
                .build();
        Student bothDefaultPhonesDiffEmail2 = (Student) new Student.StudentBuilder()
                .name("Alice Pauline")
                .phone(Person.DEFAULT_PHONE)
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
        Student aliceCopy = Student.toBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        assertTrue(ALICE.equals(ALICE));
        assertFalse(ALICE.equals(null));
        assertFalse(ALICE.equals(5));
        assertFalse(ALICE.equals(BOB));

        Student editedAlice = (Student) Student.toBuilder(ALICE).name(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) Student.toBuilder(ALICE).phone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) Student.toBuilder(ALICE).email(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) Student.toBuilder(ALICE).address(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = (Student) Student.toBuilder(ALICE).tags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        Student alice = Student.toBuilder(ALICE).build();
        String expected = "[Student] " + alice.originalToString();
        assertEquals(expected, alice.toString());
    }

}
