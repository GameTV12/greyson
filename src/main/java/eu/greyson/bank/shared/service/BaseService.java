package eu.greyson.bank.shared.service;

import eu.greyson.bank.shared.exception.UnknownSqlException;
import eu.greyson.bank.shared.model.BaseEntity;
import eu.greyson.bank.shared.repository.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Stream;

public abstract class BaseService<Entity extends BaseEntity<ID>, ID extends Serializable, Repository extends BaseRepository<Entity, ID>> {

    private static Map<Integer, Class<? extends RuntimeException>> exceptionMapper = new HashMap<>();

    /*static {
        exceptionMapper.put(UnknownForeignKeyException.SQL_ERROR_CODE, UnknownForeignKeyException.class);
    }*/

    protected abstract Repository getDefaultRepository();

    /**
     * Generic getById method.
     *
     * @param id unique identifier in data store.
     * @return found object or empty.
     */
    public Optional<Entity> get(ID id) {
        return getDefaultRepository().findSafe(id);
    }

    /**
     * Unsafe version of {@link BaseService#get} method.
     *
     * @throws EntityNotFoundException in the case entity was not found.
     */
    public Entity getUnsafe(ID id) throws EntityNotFoundException {
        return getDefaultRepository().findUnsafe(id);
    }

    /**
     * Unsafe version of {@link BaseService#get} method with activated detail entity graph. <br />.
     * Detail entity graph name pattern is "graph." + entityName + ".detail".
     *
     * @throws EntityNotFoundException in the case entity was not found.
     */
    public Entity getUnsafeDetail(ID id) throws EntityNotFoundException {
        return getDefaultRepository().findUnsafeDetail(id);
    }

    /**
     * @return all records from data store.
     */
    public Stream<Entity> getAll() {
        return getDefaultRepository().findStream();
    }

    /**
     * @return all records from data store by ids.
     */
    public Stream<Entity> getByIds(Collection<ID> ids) {
        return getDefaultRepository().getReferencesByIds(ids);
    }

    /**
     * Detail entity graph name pattern is "graph." + entityName + ".detail".
     */
    public Stream<Entity> getAllDetail() {

        return getDefaultRepository().findStreamDetail();
    }

    /**
     * Generic save method.
     *
     * @param entity to save.
     * @return saved entity.
     */
    public Entity save(Entity entity) {
        try {
            return getDefaultRepository().saveAndFlush(entity);
        } catch (DataIntegrityViolationException dive) {
            checkSQLIntegrityErrors(dive);
            throw new UnknownSqlException(dive.getMessage());
        }
    }

    /**
     * Generic save method.
     *
     * @param entities to save.
     * @return saved entities
     */
    public List<Entity> saveAll(Collection<Entity> entities) {
        try {
            return getDefaultRepository().saveAll(entities);

        } catch (DataIntegrityViolationException dive) {
            checkSQLIntegrityErrors(dive);
            throw new UnknownSqlException(dive.getMessage());
        }
    }

    /**
     * First deletes all records from table, than inserts new records from collection
     */
    public List<Entity> replaceAll(Collection<Entity> collection) {
        try {
            getDefaultRepository().deleteAllInBatch();
            return getDefaultRepository().saveAll(collection);

        } catch (DataIntegrityViolationException dive) {
            checkSQLIntegrityErrors(dive);
            throw new UnknownSqlException(dive.getMessage());
        }
    }

    /**
     * Generic delete method.
     *
     * @param entity to delete.
     */
    public void delete(Entity entity) {
        try {
            getDefaultRepository().delete(entity);
            getDefaultRepository().flush();
        } catch (DataIntegrityViolationException dive) {
            checkSQLIntegrityErrors(dive);
            throw new UnknownSqlException(dive.getMessage());
        }
    }

    /**
     * Generic delete method.
     *
     * @param id of entity to delete.
     */
    public void delete(ID id) {
        try {
            getDefaultRepository().deleteById(id);
            getDefaultRepository().flush();
        } catch (DataIntegrityViolationException dive) {
            checkSQLIntegrityErrors(dive);
            throw new UnknownSqlException(dive.getMessage());
        }
    }

    /**
     * Method check and mapping SQL data integrity error to better exception.
     *
     * @param dive SQL error to check and mapping.
     */
    private void checkSQLIntegrityErrors(final DataIntegrityViolationException dive) {
        if (dive.getMostSpecificCause() instanceof SQLIntegrityConstraintViolationException) {
            SQLIntegrityConstraintViolationException sqlException = (SQLIntegrityConstraintViolationException) dive.getMostSpecificCause();

            try {
                Class<? extends RuntimeException> ex = exceptionMapper.getOrDefault(sqlException.getErrorCode(), UnknownSqlException.class);
                Constructor<? extends RuntimeException> cons = ex.getConstructor(String.class);
                throw cons.newInstance(sqlException.getMessage());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new UnknownSqlException(sqlException.getMessage());
            }
        }
    }

}
