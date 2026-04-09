package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.TransportationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class TransportationModelJDBCImpl implements TransportationModelHibInt {

    private static Logger log = Logger.getLogger(TransportationModelJDBCImpl.class);

    public long nextPK() throws DatabaseException {

        log.debug("Model nextPK Started");
        Connection con = null;
        long pk = 0;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("select max(ID) from ST_TRANSPORTATION");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pk = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new DatabaseException("Database Exception :" + e);
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return pk + 1;
    }

    public long add(TransportationDTO dto) throws ApplicationException {

        log.debug("Model add Started");
        Connection con = null;
        long pk = 0;

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            pk = nextPK();

            PreparedStatement ps = con.prepareStatement(
                    "insert into ST_TRANSPORTATION values(?,?,?,?,?)");

            ps.setLong(1, pk);
            ps.setString(2, dto.getDescription());
            ps.setString(3, dto.getMode());
            ps.setDate(4, new java.sql.Date(dto.getOrderDate().getTime()));
            ps.setString(5, dto.getCost());

            ps.executeUpdate();
            con.commit();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add Transportation");
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        log.debug("Model add End");
        return pk;
    }

    public void delete(TransportationDTO dto) {

        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement("delete from ST_TRANSPORTATION where ID=?");
            ps.setLong(1, dto.getId());
            ps.executeUpdate();

            con.commit();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                con.rollback();
            } catch (Exception ex) {
            }
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    public void update(TransportationDTO dto) throws ApplicationException {

        Connection con = null;

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "update ST_TRANSPORTATION set DESCRIPTION=?, MODE=?, ORDER_DATE=?, COST=? where ID=?");

            ps.setString(1, dto.getDescription());
            ps.setString(2, dto.getMode());
            ps.setDate(3, new java.sql.Date(dto.getOrderDate().getTime()));
            ps.setString(4, dto.getCost());
            ps.setLong(5, dto.getId());

            ps.executeUpdate();
            con.commit();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in updating Transportation");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    public TransportationDTO findByPK(long pk) throws ApplicationException {

        Connection con = null;
        TransportationDTO dto = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from ST_TRANSPORTATION where ID=?");
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dto = new TransportationDTO();
                dto.setId(rs.getLong(1));
                dto.setDescription(rs.getString(2));
                dto.setMode(rs.getString(3));
                dto.setOrderDate(rs.getDate(4));
                dto.setCost(rs.getString(5));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception in findByPK Transportation");
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return dto;
    }

    public List search(TransportationDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Connection con = null;
        ArrayList list = new ArrayList();

        StringBuffer sql = new StringBuffer("select * from ST_TRANSPORTATION where 1=1");

        if (dto != null) {

            if (dto.getId() > 0) {
                sql.append(" AND ID = " + dto.getId());
            }

            if (dto.getDescription() != null && dto.getDescription().length() > 0) {
                sql.append(" AND DESCRIPTION like '" + dto.getDescription() + "%'");
            }

            if (dto.getMode() != null && dto.getMode().length() > 0) {
                sql.append(" AND MODE like '" + dto.getMode() + "%'");
            }

            if (dto.getOrderDate() != null) {
                sql.append(" AND ORDER_DATE = '" + new java.sql.Date(dto.getOrderDate().getTime()) + "'");
            }

            if (dto.getCost() != null && dto.getCost().length() > 0) {
                sql.append(" AND COST like '" + dto.getCost() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" limit " + pageNo + "," + pageSize);
        }

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dto = new TransportationDTO();
                dto.setId(rs.getLong(1));
                dto.setDescription(rs.getString(2));
                dto.setMode(rs.getString(3));
                dto.setOrderDate(rs.getDate(4));
                dto.setCost(rs.getString(5));
                list.add(dto);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new ApplicationException("Exception in search Transportation");
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return list;
    }

    public List search(TransportationDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

}
