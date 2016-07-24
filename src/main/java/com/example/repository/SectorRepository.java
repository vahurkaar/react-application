package com.example.repository;

import com.example.model.Sector;

import java.util.List;

/**
 * Data access interface for the SECTOR database object.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 23/07/16
 */
public interface SectorRepository {

    /**
     * Finds a sector object with the given id. The sector object does not contain it's children.
     *
     * @param id sector id
     * @return sector object
     */
    Sector findById(Long id);

    /**
     * Finds all sector objects based on it's parent.
     * By default, the returned sector objects have only the first parentId of children.
     *
     * @param parentId parent sector id
     * @return list of sector objects
     */
    List<Sector> findByParentId(Long parentId);

    /**
     * Finds all sector objects based on it's parent.
     * This methods gives the following options, regarding sector children:
     * <ul>
     *     <li>do not fetch childrent</li>
     *     <li>fetch only one parentId of children</li>
     *     <li>fetch the entire children hierarchy</li>
     * </ul>
     *
     * @param parentId parent sector id
     * @param fetchChildren boolean flag, which specifies, whether we need the resulting sectors' children objects
     * @param deepFetch boolean flag, which specifies, whether we need the entire children hierarchy.
     *                  When fetchChildren is false, then this parameter is ignored.
     * @return list of sector objects
     */
    List<Sector> findByParentId(Long parentId, boolean fetchChildren, boolean deepFetch);

    /**
     * Finds all the user's sectors.
     *
     * @param userId the user's id
     * @return list of user sector objects
     */
    List<Sector> findUserSectors(Long userId);
}
