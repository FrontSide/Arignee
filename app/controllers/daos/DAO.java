package daos;

/**
 * Interface
 * for Data Access Objects
 */

public interface DAO<T> {

    /**
     * Saves an object on persistency level
     * @param model     object to persist
     **/
    public void save(T model);

    /**
     * Obtains an object from the persistency
     * level if it could be found by an id
     * @param  id desired object's id
     * @return    desired object from persistency level
     */
    public T getById(long id);

    /**
     * Removes an object from the persistency level
     * @param model     object to delete
     */
    public void remove(T model);

    /**
     * Updates an existing object in the persistency level with a new object
     * @param model     object to update
     */
    public void update(T model);

}
