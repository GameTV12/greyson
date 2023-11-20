package eu.greyson.bank.shared.repository;

import eu.greyson.bank.shared.model.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains elementary functions.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {
    /**
     * Delete entity from database.
     *
     * @param deleted entity for delete.
     */
    void delete(T deleted);

    /**
     * @return all records from database.
     */
    Stream<T> findStream();

    /**
     * Detail entity graph name pattern is "graph." + entityName + ".detail".
     * @return all records from database.
     */
    Stream<T> findStreamDetail();

    /**
     * @param id primary key.
     * @return record from database with specified id.
     */
    Optional<T> findSafe(ID id);

    /**
     * Unsafe version of {@link BaseRepository#findOne} method.
     * @throws EntityNotFoundException in the case entity was not found.
     */
    T findUnsafe(ID id) throws EntityNotFoundException;

    /**
     * Unsafe version of {@link BaseRepository#findOne} method with activated detail entity graph. <br />
     * Detail entity graph name pattern is "graph." + entityName + ".detail".
     * @throws EntityNotFoundException in the case entity was not found.
     */
    T findUnsafeDetail(ID id) throws EntityNotFoundException;

    /**
     * Save entity to database.
     *
     * @param persisted entity for save.
     * @return saved entity.
     */
    <S extends T> S save(S persisted);

    /**
     * Retrieve Stream of <code>Entity</code> references by unique identifiers.
     * @param ids to get references.
     * @return a <code>Entity</code> references.
     */
    Stream<T> getReferencesByIds(Collection<ID> ids);

    /**
     * Retrieve <code>Entity</code> reference by unique identifier.
     * @param id to get reference.
     * @return a <code>Entity</code> reference.
     */
    T getReferenceById(ID id);

    void refresh(T o);
}
