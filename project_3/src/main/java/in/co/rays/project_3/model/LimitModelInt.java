package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.LimitDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface LimitModelInt {

    public long add(LimitDTO dto) throws ApplicationException, DuplicateRecordException;

    public void delete(LimitDTO dto);

    public void update(LimitDTO dto) throws ApplicationException;

    public LimitDTO findByCode(String code) throws ApplicationException;

    public LimitDTO findByPk(long pk) throws ApplicationException;

    public List search(LimitDTO dto) throws ApplicationException;

    public List search(LimitDTO dto, int pageNo, int pageSize) throws ApplicationException;
}