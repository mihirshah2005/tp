package seedu.address.storage;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.FileUtil.createIfMissing;
import static seedu.address.commons.util.JsonUtil.saveJsonFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * A class to access AddressBook data stored as a json file on the hard disk.
 */
public class JsonAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);

    private Path filePath;

    public JsonAddressBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);
        try {
            Optional<JsonSerializableAddressBook> jsonAddressBook = JsonUtil.readJsonFile(
                    filePath, JsonSerializableAddressBook.class);
            if (!jsonAddressBook.isPresent()) {
                return Optional.empty();
            }

            ReadOnlyAddressBook model = jsonAddressBook.get().toModelType();

            String backupFileName = filePath.toString().replace(".json", "_backup.json");
            try {
                java.nio.file.Files.copy(filePath, Path.of(backupFileName),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignored) {
                System.out.println("Warning: Could not create backup file.");
            }

            return Optional.of(model);
        } catch (Exception e) {
            String backupFileName = filePath.toString().replace(".json", "_backup.json");
            try {
                Optional<JsonSerializableAddressBook> backup = JsonUtil.readJsonFile(
                        Path.of(backupFileName), JsonSerializableAddressBook.class);
                if (backup.isPresent()) {
                    System.out.println("Loaded data from backup file: " + backupFileName);
                    return Optional.of(backup.get().toModelType());
                }
            } catch (Exception ignored) {
                System.out.println("Warning: Failed to load backup file.");
            }
            throw new DataLoadingException(e);
        }
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyAddressBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        java.nio.file.Files.createDirectories(filePath.getParent());

        String backupFileName = filePath.toString().replace(".json", "_backup.json");
        if (java.nio.file.Files.exists(filePath)) {
            java.nio.file.Files.copy(filePath, Path.of(backupFileName),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        createIfMissing(filePath);
        saveJsonFile(new JsonSerializableAddressBook(addressBook), filePath);
    }

}
