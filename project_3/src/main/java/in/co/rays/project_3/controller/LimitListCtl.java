package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.LimitDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.LimitModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "LimitListCtl", urlPatterns = { "/ctl/LimitListCtl" })
public class LimitListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(LimitListCtl.class);

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        LimitDTO dto = new LimitDTO();

        dto.setLimitCode(DataUtility.getString(request.getParameter("limitCode")));
        dto.setLimitName(DataUtility.getString(request.getParameter("limitName")));
        dto.setMaxValue(DataUtility.getString(request.getParameter("maxValue")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);
        return dto;
    }

    /**
     * Display Logic
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("LimitListCtl doGet Start");

        List list = null;
        List next = null;

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        LimitDTO dto = (LimitDTO) populateDTO(request);

        LimitModelInt model = ModelFactory.getInstance().getLimitModel();

        try {

            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage("No record found ", request);
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

            ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);
            return;
        }

        log.debug("LimitListCtl doGet End");
    }

    /**
     * Submit Logic
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("LimitListCtl doPost Start");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        LimitDTO dto = (LimitDTO) populateDTO(request);

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        LimitModelInt model = ModelFactory.getInstance().getLimitModel();

        if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;

            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;

            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            }

        } else if (OP_NEW.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LIMIT_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LIMIT_LIST_CTL, request, response);
            return;

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            pageNo = 1;

            if (ids != null && ids.length > 0) {

                LimitDTO deletedto = new LimitDTO();

                for (String id : ids) {

                    deletedto.setId(DataUtility.getLong(id));
                    model.delete(deletedto);
                }

                ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);

            } else {
                ServletUtility.setErrorMessage("Select atleast one record", request);
            }
        }

        dto = (LimitDTO) populateDTO(request);

        try {
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        ServletUtility.setDto(dto, request);

        if (list == null || list.size() == 0) {
            ServletUtility.setErrorMessage("No record found ", request);
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

        log.debug("LimitListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.LIMIT_LIST_VIEW;
    }
}