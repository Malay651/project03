package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.DescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface DescriptionModelInt {

	public long add(DescriptionDTO dto) throws ApplicationException, DuplicateRecordException;

	public void delete(DescriptionDTO dto);

	public void update(DescriptionDTO dto) throws ApplicationException, DuplicateRecordException;

	public DescriptionDTO findByPK(long pk) throws ApplicationException;

	public List search(DescriptionDTO dto) throws ApplicationException;

	public List search(DescriptionDTO dto, int pageNo, int pageSize);

}
