package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.APIKeyDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface APIKeyModelInt {

    public long add(APIKeyDTO dto) throws ApplicationException, DuplicateRecordException;

    public void delete(APIKeyDTO dto);

    public void update(APIKeyDTO dto) throws ApplicationException;

    public APIKeyDTO findByCode(String code) throws ApplicationException;

    public APIKeyDTO findByPk(long pk) throws ApplicationException;

    public List search(APIKeyDTO dto) throws ApplicationException;

    public List search(APIKeyDTO dto, int pageNo, int pageSize) throws ApplicationException;

}