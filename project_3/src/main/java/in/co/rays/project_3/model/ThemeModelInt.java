package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ThemeDTO;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.ApplicationException;

/**
 * Interface of Theme model
 * @author malay dongre
 *
 */
public interface ThemeModelInt {

    public long add(ThemeDTO dto) throws ApplicationException, DuplicateRecordException;

    public void delete(ThemeDTO dto) throws ApplicationException;

    public void update(ThemeDTO dto) throws ApplicationException, DuplicateRecordException;

    public List list() throws ApplicationException;

    public List list(int pageNo, int pageSize) throws ApplicationException;

    public List search(ThemeDTO dto) throws ApplicationException;

    public List search(ThemeDTO dto, int pageNo, int pageSize) throws ApplicationException;

    public ThemeDTO findByPK(long pk) throws ApplicationException;

    public ThemeDTO findByName(String name) throws ApplicationException;
}