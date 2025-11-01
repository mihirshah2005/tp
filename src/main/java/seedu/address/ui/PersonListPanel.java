package seedu.address.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Volunteer;

/**
 * Panel showing two side-by-side lists: Students (left) and Volunteers (right).
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML private ListView<Person> studentListView;
    @FXML private ListView<Person> volunteerListView;

    /** List of all Persons, regardless of subtype */
    private final ObservableList<Person> masterList;

    private final ReadOnlyAddressBook addressBook;

    /**
     * Creates a {@code PersonListPanel} that renders a split view for students and volunteers.
     *
     * @param masterList the observable list of persons to display; must contain the same instances
     *                   the model uses for indexing so that global indices remain consistent.
     * @throws NullPointerException if {@code masterList} is {@code null}.
     */
    public PersonListPanel(ReadOnlyAddressBook addressBook, ObservableList<Person> masterList) {
        super(FXML);
        this.masterList = masterList;
        this.addressBook = addressBook;

        // Assert FXML injected fields are present
        assert studentListView != null : "FXML injection failed: studentListView is null";
        assert volunteerListView != null : "FXML injection failed: volunteerListView is null";

        FilteredList<Person> students = new FilteredList<>(masterList, p -> p instanceof Student);
        FilteredList<Person> volunteers = new FilteredList<>(masterList, p -> p instanceof Volunteer);

        studentListView.setItems(students);
        volunteerListView.setItems(volunteers);

        studentListView.setCellFactory(lv -> new PersonListViewCell(
                this.addressBook, this.masterList, "student"));
        volunteerListView.setCellFactory(lv -> new PersonListViewCell(
                this.addressBook, this.masterList, "volunteer"));

        logger.info(() -> String.format(
                "PersonListPanel initialized. masterList=%d, students=%d, volunteers=%d",
                masterList.size(), students.size(), volunteers.size()
        ));

        // when the master list order/contents change, refresh both lists so numbers will update
        this.masterList.addListener((javafx.collections.ListChangeListener<Person>) c -> {
            int additions = 0;
            int removals = 0;
            while (c.next()) {
                additions += c.getAddedSize();
                removals += c.getRemovedSize();
            }
            logger.fine(String.format(
                    "masterList changed: +%d/-%d -> %d total",
                    additions, removals, masterList.size()
            ));
            logger.fine("Master list: " + String.format(masterList.stream().map(Person::getName).toList().toString()));
            logger.fine("Students: " + String.format(students.stream().map(Person::getName).toList().toString()));
            logger.fine("Volunteers: " + String.format(volunteers.stream().map(Person::getName).toList().toString()));
            studentListView.refresh();
            volunteerListView.refresh();
        });

        autoScrollOnChange(studentListView, students);
        autoScrollOnChange(volunteerListView, volunteers);

        // prime scroll once the scene is ready; avoids first-update misses
        Platform.runLater(() -> {
            if (!students.isEmpty()) {
                studentListView.scrollTo(students.size() - 1);
            }
            if (!volunteers.isEmpty()) {
                volunteerListView.scrollTo(volunteers.size() - 1);
            }
        });
    }

    private static void autoScrollOnChange(ListView<Person> view, ObservableList<Person> list) {
        list.addListener((ListChangeListener<Person>) change -> {
            Integer target = null;
            while (change.next()) {
                if (change.wasAdded()) {
                    // Scroll to last added item
                    target = change.getTo() - 1; // safe even for multi-add
                } else if (change.wasReplaced()) {
                    // Treat like add: scroll to the end of the replaced range
                    target = change.getTo() - 1;
                } else if (change.wasRemoved()) {
                    // If items were removed, keep the viewport near where the change happened
                    int idx = Math.min(change.getFrom(), Math.max(0, list.size() - 1));
                    target = (list.isEmpty() ? null : idx);
                }
            }
            if (target != null && !list.isEmpty()) {
                final int scrollIndex = Math.max(0, Math.min(target, list.size() - 1));
                Platform.runLater(() -> view.scrollTo(scrollIndex));
            }
        });
    }


    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    static class PersonListViewCell extends ListCell<Person> {
        private final ObservableList<Person> masterList;
        private final ReadOnlyAddressBook addressBook;
        /** "student" or "volunteer" for logs*/
        private final String lane;

        PersonListViewCell(ReadOnlyAddressBook addressBook, ObservableList<Person> masterList, String lane) {
            this.masterList = masterList;
            this.addressBook = addressBook;
            this.lane = lane;
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            int idx = masterList.indexOf(person);
            if (idx < 0) {
                // Unexpected: the cell's person is not in the master list (index would be wrong)
                LogsCenter.getLogger(PersonListViewCell.class)
                        .log(Level.WARNING, () -> "Person not found in masterList while rendering (" + lane + "): "
                                + person);
                setGraphic(null);
                setText(null);
                return;
            }

            // indexes here are based on the master list to avoid breaking the pair function
            int globalIndex = masterList.indexOf(person) + 1;
            ObservableList<String> pairings = FXCollections.observableList(addressBook.getPairedPersons(person).stream()
                    .map(pairing -> ((masterList.indexOf(pairing)==-1) ? "" : masterList.indexOf(pairing) + ". ")
                            + pairing.getName().toString() ).toList());
            setGraphic(new PersonCard(addressBook, pairings, person, globalIndex).getRoot());
        }
    }
}
