package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.InventoryDTO;
import in.co.rays.project_3.dto.TransportationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface InventoryModelHibInt {
	
	public long add(InventoryDTO dto) throws ApplicationException, DuplicateRecordException;
	public void delete(InventoryDTO dto);
	public void update(InventoryDTO dto) throws ApplicationException, DuplicateRecordException;
	public InventoryDTO findByPK(long pk) throws ApplicationException;
	public List search(InventoryDTO dto) throws ApplicationException;
	public List search(InventoryDTO dto,int pageNo,int pageSize) ;

}
