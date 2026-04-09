package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.InventoryDTO;
import in.co.rays.project_3.model.InventoryModelHibInt;

import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Inventory List functionality controller to perform Search and List operation.
 */
@WebServlet(name = "InventoryListCtl", urlPatterns = { "/ctl/InventoryListCtl" })
public class InventoryListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(InventoryListCtl.class);

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        InventoryDTO dto = new InventoryDTO();

        dto.setItemCode(DataUtility.getString(request.getParameter("itemCode")));
        dto.setItemName(DataUtility.getString(request.getParameter("itemName")));
        dto.setItemStatus(DataUtility.getString(request.getParameter("itemStatus")));

        populateBean(dto, request);
        return dto;
    }

    /**
     * Display logic
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("InventoryListCtl doGet Start");

        List list;
        List next;

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        InventoryDTO dto = (InventoryDTO) populateDTO(request);
        InventoryModelHibInt model = ModelFactory.getInstance().getInventoryModel();

        try {
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found", request);
            }

            if (next == null || next.size() == 0) {
                request.setAttribute("nextListSize", 0);
            } else {
                request.setAttribute("nextListSize", next.size());
            }

            ServletUtility.setList(list, request);
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("InventoryListCtl doGet End");
    }

    /**
     * Submit logic
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("InventoryListCtl doPost Start");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1
                : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        InventoryDTO dto = (InventoryDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        InventoryModelHibInt model = ModelFactory.getInstance().getInventoryModel();

        if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;
            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            }
        } else if (OP_NEW.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.INVENTORY_CTL, request, response);
            return;
        } else if (OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.INVENTORY_LIST_CTL, request, response);
            return;
        } else if (OP_DELETE.equalsIgnoreCase(op)) {
            pageNo = 1;
            if (ids != null && ids.length > 0) {
                InventoryDTO deletedto = new InventoryDTO();
                for (String id : ids) {
                    deletedto.setId(DataUtility.getLong(id));
                    model.delete(deletedto);
                    ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);
                }
            } else {
                ServletUtility.setErrorMessage("Select at least one record", request);
            }
        }

        dto = (InventoryDTO) populateDTO(request);

        list = model.search(dto, pageNo, pageSize);
        next = model.search(dto, pageNo + 1, pageSize);

        ServletUtility.setDto(dto, request);

        if (list == null || list.size() == 0) {
            ServletUtility.setErrorMessage("No record found", request);
        }

        if (next == null || next.size() == 0) {
            request.setAttribute("nextListSize", 0);
        } else {
            request.setAttribute("nextListSize", next.size());
        }

        ServletUtility.setList(list, request);
        ServletUtility.setPageNo(pageNo, request);
        ServletUtility.setPageSize(pageSize, request);
        ServletUtility.forward(getView(), request, response);

        log.debug("InventoryListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.INVENTORY_LIST_VIEW;
    }
}