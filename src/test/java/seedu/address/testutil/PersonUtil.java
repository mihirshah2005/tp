package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code student}.
     */
    public static String getAddCommandStudent(Person person) {
        return AddCommand.COMMAND_WORD_STUDENT + " " + getPersonDetails(person);
    }

    /**
     * Returns an add command string for adding the {@code volunteer}.
     */
    public static String getAddCommandVolunteer(Person person) {
        return AddCommand.COMMAND_WORD_VOLUNTEER + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(Person.PersonBuilder descriptor) {
        StringBuilder sb = new StringBuilder();
        if (descriptor.getName() != null) {
            sb.append(PREFIX_NAME).append(descriptor.getName().fullName).append(" ");
        }
        if (descriptor.getPhone() != null) {
            sb.append(PREFIX_PHONE).append(descriptor.getPhone().value).append(" ");
        }

        if (descriptor.getEmail() != null) {
            sb.append(PREFIX_EMAIL).append(descriptor.getEmail().value).append(" ");
        }
        if (descriptor.getAddress() != null) {
            sb.append(PREFIX_ADDRESS).append(descriptor.getAddress().value).append(" ");
        }
        if (descriptor.getTags() != null) {
            Set<Tag> tags = descriptor.getTags();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
