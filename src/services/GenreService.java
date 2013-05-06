/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import models.dao.BaseDAO;
import models.entity.Genre;

/**
 *
 * @author Nesh
 */
public class GenreService extends BaseDAO<Genre> {

    private static GenreService instance;

    public static GenreService getInstance() {
        synchronized (GenreService.class) {
            if (instance == null) {
                instance = new GenreService();
            } else {
            }
        }
        return instance;
    }

    private GenreService(){

    }

}
