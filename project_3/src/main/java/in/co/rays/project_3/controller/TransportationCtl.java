package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.TransportationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.TransportationModelHibInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "TransportationCtl", urlPatterns = { "/ctl/TransportationCtl" })
public class TransportationCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TransportationCtl.class);

	/**
	 * Preload Mode List
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<>();
		map.put("Bus", "Bus");
		map.put("Train", "Train");
		map.put("Flight", "Flight");
		map.put("Bike", "Bike");
		map.put("Truck", "Truck");
		request.setAttribute("map", map);
	}

	/**
	 * Validate Transportation Fields
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("TransportationCtl validate started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("mode"))) {
			request.setAttribute("mode", PropertyReader.getValue("error.require", "Mode"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.require", "Order Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("orderDate"))) {
			request.setAttribute("orderDate", PropertyReader.getValue("error.date", "Order Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.require", "Cost"));
			pass = false;
		}

		log.debug("TransportationCtl validate ended with result: " + pass);
		return pass;
	}

	/**
	 * Populate DTO
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		log.debug("TransportationCtl populateDTO started");

		TransportationDTO dto = new TransportationDTO();
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setMode(DataUtility.getString(request.getParameter("mode")));
		dto.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));
		dto.setCost(DataUtility.getString(request.getParameter("cost")));

		populateBean(dto, request);

		log.debug("TransportationCtl populateDTO ended");
		return dto;
	}

	/**
	 * Handle GET
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("TransportationCtl doGet started");

		long id = DataUtility.getLong(request.getParameter("id"));
		TransportationModelHibInt model = ModelFactory.getInstance().getTransportationModel();

		if (id > 0) {
			try {
				TransportationDTO dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("TransportationCtl doGet ended");
	}

	/**
	 * Handle POST
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("TransportationCtl doPost started");

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));
		TransportationModelHibInt model = ModelFactory.getInstance().getTransportationModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			TransportationDTO dto = (TransportationDTO) populateDTO(request);

			try {
				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Transportation Updated Successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Transportation Added Successfully", request);
				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.setDto(dto, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TRANSPORTATION_CTL, request, response);
			return;
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TRANSPORTATION_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("TransportationCtl doPost ended");
	}

	@Override
	protected String getView() {
		return ORSView.TRANSPORTATION_VIEW;
	}
}