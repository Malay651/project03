package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.UsageDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface UsageModelInt {

    public long add(UsageDTO dto) throws ApplicationException, DuplicateRecordException;

    public void delete(UsageDTO dto);

    public void update(UsageDTO dto) throws ApplicationException;

    public UsageDTO findByCode(String code) throws ApplicationException;

    public UsageDTO findByPk(long pk) throws ApplicationException;

    public List search(UsageDTO dto) throws ApplicationException;

    public List search(UsageDTO dto, int pageNo, int pageSize) throws ApplicationException;

}