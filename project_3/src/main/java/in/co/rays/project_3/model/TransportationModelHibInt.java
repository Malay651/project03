package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.dto.TransportationDTO;
import in.co.rays.project_3.exception.ApplicationException;

public interface TransportationModelHibInt {
	
	public long add(TransportationDTO dto) throws ApplicationException;
	public void delete(TransportationDTO dto);
	public void update(TransportationDTO dto) throws ApplicationException;
	public TransportationDTO findByPK(long pk) throws ApplicationException;
	public List search(TransportationDTO dto) throws ApplicationException;
	public List search(TransportationDTO dto,int pageNo,int pageSize) throws ApplicationException;


}
