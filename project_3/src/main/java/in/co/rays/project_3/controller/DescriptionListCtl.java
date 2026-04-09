package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.DescriptionDTO;
import in.co.rays.project_3.model.DescriptionModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "DescriptionListCtl", urlPatterns = { "/ctl/DescriptionListCtl" })
public class DescriptionListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(DescriptionListCtl.class);

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		DescriptionDTO dto = new DescriptionDTO();

		dto.setDepartmentName(DataUtility.getString(request.getParameter("departmentName")));
		dto.setDepartmentHead(DataUtility.getString(request.getParameter("departmentHead")));
		dto.setLocation(DataUtility.getString(request.getParameter("location")));
		dto.setStatus(DataUtility.getString(request.getParameter("status")));

		populateBean(dto, request);
		return dto;
	}

	/**
	 * Display logic
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("DescriptionListCtl doGet Start");

		List list;
		List next;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		DescriptionDTO dto = (DescriptionDTO) populateDTO(request);
		DescriptionModelInt model = ModelFactory.getInstance().getDescriptionModel();

		try {
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			if (list == null && list.size() == 0) {
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

			ServletUtility.handleListDBDown(getView(), dto, pageNo, pageSize, request, response);

			return;
		}

		log.debug("DescriptionListCtl doGet End");
	}

	/**
	 * Submit logic
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("DescriptionListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		DescriptionDTO dto = (DescriptionDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		DescriptionModelInt model = ModelFactory.getInstance().getDescriptionModel();

		if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			}

		} else if (OP_NEW.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DESCRITPION_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DESCRIPTION_LIST_CTL, request, response);
			return;

		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			pageNo = 1;

			if (ids != null && ids.length > 0) {

				DescriptionDTO deleteDto = new DescriptionDTO();

				for (String id : ids) {
					deleteDto.setId(DataUtility.getLong(id));
					model.delete(deleteDto);
				}

				ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);

			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}

		dto = (DescriptionDTO) populateDTO(request);

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

		log.debug("DescriptionListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.DESCRIPTION_LIST_VIEW;
	}
}