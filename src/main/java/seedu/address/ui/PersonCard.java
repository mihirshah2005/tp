package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Volunteer;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final PersonListPanel.IndexedPerson self;
    private final Stream<PersonListPanel.IndexedPerson> indexedPartners;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label typeLabel;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane pairings;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     *
     * @param self The main person of this PersonCard herself to be displayed, in the form of an IndexedPerson
     *             with her index and person object.
     * @param indexedPartners A Stream of IndexedPersons (each with her index and person object)
     */
    public PersonCard(ReadOnlyAddressBook addressBook, PersonListPanel.IndexedPerson self,
                      Stream<PersonListPanel.IndexedPerson> indexedPartners) {
        super(FXML);
        this.self = self;
        this.indexedPartners = indexedPartners;

        Person person = self.person();
        id.setText(self.index() + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        String type = (person instanceof Student) ? "Type: Student"
                : (person instanceof Volunteer) ? "Type: Volunteer"
                : "Type: Person";
        typeLabel.setText(type);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        renderPairings();
    }

    private void renderPairings() {
        pairings.getChildren().clear();
        indexedPartners
                .sorted(Comparator.comparing(PersonListPanel.IndexedPerson::index))
                .forEach(indexedPartner -> {
                    // Prefix: only insert "<index>." if pairing exists in currently displayed list, e.g.,
                    // a student might appear in the list returned by `find`, but her paired volunteer doesn't
                    String prefix = (indexedPartner.index() != -1) ? (indexedPartner.index() + 1) + ". " : "";

                    // e.g., adds a label with "3. Alice", if Alice is a partner and currently displayed list contains
                    // Alice in index 3, and simply "Alice" otherwise
                    pairings.getChildren().add(new Label(prefix + indexedPartner.person().getName().toString()));
                });
    }
}
