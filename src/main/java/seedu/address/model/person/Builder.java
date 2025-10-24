package seedu.address.model.person;

/**
 * Abstract Builder class to be extended.
 */
public abstract class Builder<T extends Builder<T>> {
    /**
     * Abstract constructor for Builder.
     */
    public abstract Person build();
}
