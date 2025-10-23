package seedu.address.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Volunteer;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
                new Student.StudentBuilder()
                        .name("Alex Yeoh")
                        .phone("87438807")
                        .email("alexyeoh@example.com")
                        .address("Blk 30 Geylang Street 29, #06-40")
                        .tags(getTagSet("Math"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
                new Volunteer.VolunteerBuilder()
                        .name("Bernice Yu")
                        .phone("99272758")
                        .email("berniceyu@example.com")
                        .address("Blk 30 Lorong 3 Serangoon Gardens, #07-18")
                        .tags(getTagSet("Math", "Science"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
                new Student.StudentBuilder()
                        .name("Charlotte Oliveiro")
                        .phone("93210283")
                        .email("charlotte@example.com")
                        .address("Blk 11 Ang Mo Kio Street 74, #11-04")
                        .tags(getTagSet("English"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
                new Volunteer.VolunteerBuilder()
                        .name("David Li")
                        .phone("91031282")
                        .email("lidavid@example.com")
                        .address("Blk 436 Serangoon Gardens Street 26, #16-43")
                        .tags(getTagSet("Chinese"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
                new Student.StudentBuilder()
                        .name("Irfan Ibrahim")
                        .phone("92492021")
                        .email("irfan@example.com")
                        .address("Blk 47 Tampines Street 20, #17-35")
                        .tags(getTagSet("Chinese"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
                new Volunteer.VolunteerBuilder()
                        .name("Roy Balakrishnan")
                        .phone("92624417")
                        .email("royb@example.com")
                        .address("Blk 45 Aljunied Street 85, #11-31")
                        .tags(getTagSet("Malay", "English"))
                        .pairedPersons(new ArrayList<>())
                        .build(),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
