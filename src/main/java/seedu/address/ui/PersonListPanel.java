package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
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

    private final ObservableList<Person> masterList;

    public PersonListPanel(ObservableList<Person> masterList) {
        super(FXML);
        this.masterList = masterList;

        FilteredList<Person> students = new FilteredList<>(masterList, p -> p instanceof Student);
        FilteredList<Person> volunteers = new FilteredList<>(masterList, p -> p instanceof Volunteer);

        studentListView.setItems(students);
        volunteerListView.setItems(volunteers);

        studentListView.setCellFactory(lv -> new PersonListViewCell(this.masterList));
        volunteerListView.setCellFactory(lv -> new PersonListViewCell(this.masterList));

        // when the master list order/contents change, refresh both lists so numbers will update
        this.masterList.addListener((javafx.collections.ListChangeListener<Person>) c -> {
            studentListView.refresh();
            volunteerListView.refresh();
        });
    }

    /**
     * Custom cell that shows a Person via PersonCard.
     */
    static class PersonListViewCell extends ListCell<Person> {
        private final ObservableList<Person> masterList;

        PersonListViewCell(ObservableList<Person> masterList) {
            this.masterList = masterList;
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);
            if (empty || person == null) {
                setGraphic(null);
                setText(null);
                return;
            }
            // indexes here are based on the master list so that I don't break the pair function
            int globalIndex = masterList.indexOf(person) + 1;
            setGraphic(new PersonCard(person, globalIndex).getRoot());
        }
    }
}