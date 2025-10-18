package seedu.address.model.person;

public abstract class Builder<T extends Builder<T>> {
    /**
     * Abstract constructor for Builder.
     */
    public abstract Person build();
}
