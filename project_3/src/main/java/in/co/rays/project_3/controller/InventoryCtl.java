package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.InventoryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.InventoryModelHibInt;

import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/InventoryCtl" })
public class InventoryCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InventoryCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

	    InventoryModelHibInt model = ModelFactory.getInstance().getInventoryModel();

	    HashMap<String, String> map = new HashMap<String, String>();
	    map.put("Stock", "Stock");
	    map.put("Out Of Stock", "Out Of Stock");

	    request.setAttribute("map", map);
	}

	/**
	 * Validate Inventory input fields
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("InventoryCtl validate method started");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("itemCode"))) {
			request.setAttribute("itemCode", PropertyReader.getValue("error.require", "Item Code"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("itemName"))) {
			request.setAttribute("itemName", PropertyReader.getValue("error.require", "Item Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("quantity"))) {
			request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("quantity"))) {
			request.setAttribute("quantity", "Quantity must be a number");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("price"))) {
			request.setAttribute("price", PropertyReader.getValue("error.require", "Price"));
			pass = false;
		} else {
			try {
				Long.parseLong(request.getParameter("price"));
			} catch (NumberFormatException e) {
				request.setAttribute("price", "Price must be a valid number");
				pass = false;
			}
		}

		if (DataValidator.isNull(request.getParameter("itemStatus"))) {
			request.setAttribute("itemStatus", PropertyReader.getValue("error.require", "Status"));
			pass = false;
		}

		log.debug("InventoryCtl validate method ended with result: " + pass);
		return pass;
	}

	/**
	 * Populate InventoryDTO from request
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("InventoryCtl populateDTO method started");

		InventoryDTO dto = new InventoryDTO();

		dto.setItemCode(DataUtility.getString(request.getParameter("itemCode")));
		dto.setItemName(DataUtility.getString(request.getParameter("itemName")));
		dto.setQuantity(DataUtility.getInt(request.getParameter("quantity")));
		dto.setPrice(DataUtility.getLong(request.getParameter("price"))); // Long type
		dto.setItemStatus(DataUtility.getString(request.getParameter("itemStatus")));

		populateBean(dto, request);
		log.debug("InventoryCtl populateDTO method ended");

		return dto;
	}

	/**
	 * Handles GET request
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("InventoryCtl doGet started");
		long id = DataUtility.getLong(request.getParameter("id"));
		InventoryModelHibInt model = ModelFactory.getInstance().getInventoryModel();

		if (id > 0) {
			try {
				InventoryDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("InventoryCtl doGet ended");
	}

	/**
	 * Handles POST request
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("InventoryCtl doPost started");

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		InventoryModelHibInt model = ModelFactory.getInstance().getInventoryModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			InventoryDTO dto = (InventoryDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Inventory Updated Successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Inventory Added Successfully", request);
				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setErrorMessage("Item Code Already Exists", request);
				ServletUtility.setDto(dto, request);
				ServletUtility.forward(getView(), request, response);
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INVENTORY_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INVENTORY_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("InventoryCtl doPost ended");
	}

	@Override
	protected String getView() {
		log.debug("InventoryCtl getView called");
		return ORSView.INVENTORY_VIEW;
	}
}