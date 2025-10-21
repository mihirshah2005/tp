package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Supplier<Person> ALICE_SUPPLIER = (() -> new StudentBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends").build());
    public static final Supplier<Person> BENSON_SUPPLIER = (() -> new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build());
    public static final Supplier<Person> CARL_SUPPLIER = (() -> new PersonBuilder()
            .withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").build());
    public static final Supplier<Person> DANIEL_SUPPLIER = (() -> new PersonBuilder()
            .withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends").build());
    public static final Supplier<Person> ELLE_SUPPLIER = (() -> new PersonBuilder()
            .withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build());
    public static final Supplier<Person> FIONA_SUPPLIER = (() -> new PersonBuilder()
            .withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").build());
    public static final Supplier<Person> GEORGE_SUPPLIER = (() -> new PersonBuilder()
            .withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build());

    // Manually added
    public static final Supplier<Person> HOON_SUPPLIER = (() -> new PersonBuilder()
            .withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build());
    public static final Supplier<Person> IDA_SUPPLIER = (() -> new PersonBuilder()
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build());

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Supplier<Person> AMY_SUPPLIER = (() -> new PersonBuilder()
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build());
    public static final Supplier<Person> BOB_SUPPLIER = (() -> new PersonBuilder()
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build());

    public static final Person ALICE = ALICE_SUPPLIER.get();
    public static final Person BENSON = BENSON_SUPPLIER.get();
    public static final Person CARL = CARL_SUPPLIER.get();
    public static final Person DANIEL = DANIEL_SUPPLIER.get();
    public static final Person ELLE = ELLE_SUPPLIER.get();
    public static final Person FIONA = FIONA_SUPPLIER.get();
    public static final Person GEORGE = GEORGE_SUPPLIER.get();
    public static final Person HOON = HOON_SUPPLIER.get();
    public static final Person IDA = IDA_SUPPLIER.get();
    public static final Person AMY = AMY_SUPPLIER.get();
    public static final Person BOB = BOB_SUPPLIER.get();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    /**
     * Returns an {@code AddressBook} with all the typical persons but with Alice paired to Benson,
     * returning a new copy each function call.
     */
    public static AddressBook getSelfPairingAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getSelfPairingTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }
    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE_SUPPLIER.get(), BENSON_SUPPLIER.get(), CARL_SUPPLIER.get(),
                DANIEL_SUPPLIER.get(), ELLE_SUPPLIER.get(), FIONA_SUPPLIER.get(), GEORGE_SUPPLIER.get()));
    }

    public static List<Person> getSelfPairingTypicalPersons() {
        Person alice = ALICE_SUPPLIER.get();
        Person benson = BENSON_SUPPLIER.get();
        try {
            alice.addPerson(benson);
        } catch (IllegalValueException e) {
            throw new RuntimeException(e); // should not happen
        }
        return new ArrayList<>(Arrays.asList(alice, benson, CARL_SUPPLIER.get(), DANIEL_SUPPLIER.get(),
                ELLE_SUPPLIER.get(), FIONA_SUPPLIER.get(), GEORGE_SUPPLIER.get()));
    }
}
