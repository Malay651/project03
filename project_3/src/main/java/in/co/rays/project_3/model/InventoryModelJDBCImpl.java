package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.InventoryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class InventoryModelJDBCImpl implements InventoryModelHibInt {

    private static Logger log = Logger.getLogger(InventoryModelJDBCImpl.class);
    private Connection con = null;

    // ================= NEXT PK =================
    public long nextPK() throws DatabaseException {
        long pk = 0;
        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(ID) FROM ST_INVENTORY");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pk = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new DatabaseException("Exception getting next pk");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return pk + 1;
    }

    // ================= ADD =================
    public long add(InventoryDTO dto) throws ApplicationException, DuplicateRecordException {

        if (findByItemCode(dto.getItemCode()) != null) {
            throw new DuplicateRecordException("Item Code already exists");
        }

        long pk = 0;

        try {
            pk = nextPK();
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO ST_INVENTORY (ID, ITEM_CODE, ITEM_NAME, QUANTITY, PRICE, STATUS, CREATED_BY, MODIFIED_BY) VALUES (?,?,?,?,?,?,?,?)");

            ps.setLong(1, pk);
            ps.setString(2, dto.getItemCode());
            ps.setString(3, dto.getItemName());
            ps.setInt(4, dto.getQuantity());
            ps.setLong(5, dto.getPrice());
            ps.setString(6, dto.getItemStatus());
            ps.setString(7, dto.getCreatedBy());
            ps.setString(8, dto.getModifiedBy());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in add Inventory");
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return pk;
    }

    // ================= DELETE =================
    public void delete(InventoryDTO dto) {
        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement("DELETE FROM ST_INVENTORY WHERE ID=?");
            ps.setLong(1, dto.getId());
            ps.executeUpdate();
            ps.close();
            con.commit();

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

    // ================= UPDATE =================
    public void update(InventoryDTO dto) throws ApplicationException, DuplicateRecordException {

        InventoryDTO existDto = findByItemCode(dto.getItemCode());
        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Item Code already exists");
        }

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE ST_INVENTORY SET ITEM_CODE=?, ITEM_NAME=?, QUANTITY=?, PRICE=?, STATUS=?, MODIFIED_BY=? WHERE ID=?");

            ps.setString(1, dto.getItemCode());
            ps.setString(2, dto.getItemName());
            ps.setInt(3, dto.getQuantity());
            ps.setLong(4, dto.getPrice());
            ps.setString(5, dto.getItemStatus());
            ps.setString(6, dto.getModifiedBy());
            ps.setLong(7, dto.getId());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception " + ex.getMessage());
            }
            throw new ApplicationException("Exception in update Inventory");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    // ================= FIND BY PK =================
    public InventoryDTO findByPK(long pk) throws ApplicationException {
        InventoryDTO dto = null;
        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ST_INVENTORY WHERE ID=?");
            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new InventoryDTO();
                dto.setId(rs.getLong(1));
                dto.setItemCode(rs.getString(2));
                dto.setItemName(rs.getString(3));
                dto.setQuantity(rs.getInt(4));
                dto.setPrice(rs.getLong(5));
                dto.setItemStatus(rs.getString(6));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in findByPK Inventory");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }

    // ================= FIND BY ITEM CODE =================
    public InventoryDTO findByItemCode(String itemCode) throws ApplicationException {
        InventoryDTO dto = null;
        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ST_INVENTORY WHERE ITEM_CODE=?");
            ps.setString(1, itemCode);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dto = new InventoryDTO();
                dto.setId(rs.getLong(1));
                dto.setItemCode(rs.getString(2));
                dto.setItemName(rs.getString(3));
                dto.setQuantity(rs.getInt(4));
                dto.setPrice(rs.getLong(5));
                dto.setItemStatus(rs.getString(6));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new ApplicationException("Exception in findByItemCode");
        } finally {
            JDBCDataSource.closeConnection(con);
        }
        return dto;
    }

    // ================= SEARCH =================
    public List<InventoryDTO> search(InventoryDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List<InventoryDTO> search(InventoryDTO dto, int pageNo, int pageSize)  {

        StringBuffer sql = new StringBuffer("SELECT * FROM ST_INVENTORY WHERE 1=1");

        if (dto != null) {
            if (dto.getId() > 0) {
                sql.append(" AND ID = " + dto.getId());
            }
            if (dto.getItemCode() != null && dto.getItemCode().length() > 0) {
                sql.append(" AND ITEM_CODE LIKE '" + dto.getItemCode() + "%'");
            }
            if (dto.getItemName() != null && dto.getItemName().length() > 0) {
                sql.append(" AND ITEM_NAME LIKE '" + dto.getItemName() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }

        List<InventoryDTO> list = new ArrayList<>();

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InventoryDTO dto1 = new InventoryDTO();
                dto1.setId(rs.getLong(1));
                dto1.setItemCode(rs.getString(2));
                dto1.setItemName(rs.getString(3));
                dto1.setQuantity(rs.getInt(4));
                dto1.setPrice(rs.getLong(5));
                dto1.setItemStatus(rs.getString(6));

                list.add(dto1);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return list;
    }

}