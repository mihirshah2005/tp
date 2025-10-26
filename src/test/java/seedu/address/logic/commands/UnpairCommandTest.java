package seedu.address.logic.commands;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


class UnpairCommandTest {
    private Model model;

    @BeforeEach
    void setUp() {
        // Fresh model EVERY test run
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }
}
