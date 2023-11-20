package eu.greyson.bank.shared.repository.impl;

import eu.greyson.bank.shared.model.BaseEntity;
import eu.greyson.bank.shared.repository.BaseRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BaseRepositoryImpl<T extends BaseEntity<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager em;

    private final Class<T> persistentClass;

    public BaseRepositoryImpl(JpaMetamodelEntityInformation<T, ?> information, EntityManager entityManager) {
        super(information, entityManager);
        this.em = entityManager;
        this.persistentClass = information.getJavaType();
    }

    @Transactional()
    @Override
    public Stream<T> findStream() {
        return findAll().stream();
    }

    @Transactional()
    @Override
    public Stream<T> findStreamDetail() {
        EntityGraph graph = this.em.getEntityGraph("graph." + persistentClass.getSimpleName() + ".detail");

        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(persistentClass);
        Root<T> message = cq.from(persistentClass);
        TypedQuery<T> q = em.createQuery(cq);
        q.setHint("jakarta.persistence.loadgraph", graph);
        return q.getResultList().stream();
    }

    @Transactional
    @Override
    public Optional<T> findSafe(ID id) {
        return findById(id);
    }

    @Transactional
    @Override
    public T findUnsafe(ID id) throws EntityNotFoundException {
        Optional<T> entity = this.findById(id);

        if (!entity.isPresent()) {
            throw new EntityNotFoundException("Entity [" + persistentClass.getName() + "] with ID [" + id + "] was not found.");
        }
        return entity.get();
    }

    @Transactional
    @Override
    public T findUnsafeDetail(ID id) throws EntityNotFoundException {
        EntityGraph graph = this.em.getEntityGraph("graph." + persistentClass.getSimpleName() + ".detail");

        Map<String, Object> hints = new HashMap<>();
        hints.put("jakarta.persistence.fetchgraph", graph);

        T entity = this.em.find(persistentClass, id, hints);

        if (entity == null) {
            throw new EntityNotFoundException("Entity [" + persistentClass.getName() + "] with ID [" + id + "] was not found.");
        }
        return entity;
    }

    @Override
    public T getReferenceById(ID id) {
        return em.getReference(persistentClass, id);
    }

    @Override
    public Stream<T> getReferencesByIds(Collection<ID> ids) {
        return ids.stream().map(this::getReferenceById);
    }

    @Override
    public void refresh(T o) {
        em.refresh(o);
    }
}
