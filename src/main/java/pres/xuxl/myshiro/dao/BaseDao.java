package pres.xuxl.myshiro.dao;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDao<M, ID extends Serializable> extends JpaRepository<M, ID> {


    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.CrudRepository#findAll()
     */
    List<M> findAll();

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.PagingAndSortingRepository#findAll(
     * org.springframework.data.domain.Sort)
     */
    List<M> findAll(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction
     * provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    Page<M> findAll(Pageable pageable);


	
}
