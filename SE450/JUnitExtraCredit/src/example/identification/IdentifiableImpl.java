package example.identification;

import example.common.InvalidDataException;

/**
 * Default implementation of the {@link Identifiable} interface.
 * @author Rong Zhuang
 */
public class IdentifiableImpl implements Identifiable {

    private String identifier;

    /**
     * Constructs and initializes a identifiableImpl with a specified id.
     * @param id identity for the identifiableImpl instance
     * @throws InvalidDataException if id value is null or empty.
     */
    public IdentifiableImpl(String id) throws InvalidDataException {
        setIdentifier(id);
    }
 
    /**
     * Get the identifier of this identifiableImpl.
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Set identifier with the specified id.
     * @param id a string for identifying this identifiableImpl
     * @throws InvalidDataException if id value is null or empty.
     */
    public final void setIdentifier(String id) throws InvalidDataException {
        if (id == null || id.length() == 0) {
            throw new InvalidDataException("Null or empty ID passed to setIdentifier");
        }
        identifier = id;
    }
}
