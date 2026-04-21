package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.UsageDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.UsageModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "UsageListCtl", urlPatterns = { "/ctl/UsageListCtl" })
public class UsageListCtl extends BaseCtl {

    private static Logger log = Logger.getLogger(UsageListCtl.class);

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        UsageDTO dto = new UsageDTO();

        dto.setUsageCode(DataUtility.getString(request.getParameter("usageCode")));
        dto.setUsageName(DataUtility.getString(request.getParameter("usageName")));
        dto.setUsageType(DataUtility.getString(request.getParameter("usageType")));
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

        log.debug("UsageListCtl doGet Start");

        List list = null;
        List next = null;

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        UsageDTO dto = (UsageDTO) populateDTO(request);

        UsageModelInt model = ModelFactory.getInstance().getUsageModel();

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

        log.debug("UsageListCtl doGet End");
    }

    /**
     * Submit Logic
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        log.debug("UsageListCtl doPost Start");

        List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        UsageDTO dto = (UsageDTO) populateDTO(request);

        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");

        UsageModelInt model = ModelFactory.getInstance().getUsageModel();

        if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;

            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;

            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            }

        } else if (OP_NEW.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.USAGE_CTL, request, response);
            return;

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.USAGE_LIST_CTL, request, response);
            return;

        } else if (OP_DELETE.equalsIgnoreCase(op)) {

            pageNo = 1;

            if (ids != null && ids.length > 0) {

                UsageDTO deleteDto = new UsageDTO();

                for (String id : ids) {

                    deleteDto.setId(DataUtility.getLong(id));
                    model.delete(deleteDto);
                }

                ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);

            } else {
                ServletUtility.setErrorMessage("Select atleast one record", request);
            }
        }

        dto = (UsageDTO) populateDTO(request);

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

        log.debug("UsageListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.USAGE_LIST_VIEW;
    }
}