<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.controller.TransportationCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Transportation View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Font Awesome for Icons -->
<link rel="stylesheet"
	href="<%=ORSView.APP_CONTEXT%>/css/font-awesome.min.css">

<style type="text/css">
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/user1.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}

.input-group-text {
	background-color: #e9ecef;
}

.input-group {
	margin-bottom: 15px;
}
</style>
</head>

<body class="p4">

	<div class="header">
		<%@include file="Header.jsp"%>
		<%@include file="calendar.jsp"%>
	</div>

	<main>
	<form action="<%=ORSView.TRANSPORTATION_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto"
				class="in.co.rays.project_3.dto.TransportationDTO" scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Transportation</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Transportation</h3>
						<%
							}
						%>

						<%
							HashMap map = (HashMap) request.getAttribute("map");
						%>

						<!-- Success Message -->
						<%
							if (!ServletUtility.getSuccessMessage(request).equals("")) {
						%>
						<div class="alert alert-success alert-dismissible text-center">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<%=ServletUtility.getSuccessMessage(request)%>
						</div>
						<%
							}
						%>

						<!-- Error Message -->
						<%
							if (!ServletUtility.getErrorMessage(request).equals("")) {
						%>
						<div class="alert alert-danger alert-dismissible text-center">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<%=ServletUtility.getErrorMessage(request)%>
						</div>
						<%
							}
						%>

						<!-- Hidden Fields -->
						<input type="hidden" name="id" value="<%=dto.getId()%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Description -->
						<span><b>Description</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-file-text"></i>
								</div>
							</div>
							<input type="text" name="description" class="form-control"
								placeholder="Enter Description"
								value="<%=DataUtility.getStringData(dto.getDescription())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("description", request)%></font><br>

						<!-- Mode -->
						<span><b>Mode</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-bus"></i>
								</div>
							</div>
							<%=HTMLUtility.getList("mode", dto.getMode(), map)%>
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("mode", request)%></font><br>

						<!-- Order Date -->
						<span><b>Order Date</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-calendar"></i>
								</div>
							</div>
							<input type="text" id="datepicker2" name="orderDate"
								class="form-control" placeholder="Select Order Date" readonly
								value="<%=DataUtility.getDateString(dto.getOrderDate())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("orderDate", request)%></font><br>

						<!-- Cost -->
						<span><b>Cost</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-money"></i>
								</div>
							</div>
							<input type="text" name="cost" class="form-control"
								placeholder="Enter Cost"
								value="<%=DataUtility.getStringData(dto.getCost())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("cost", request)%></font><br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=TransportationCtl.OP_UPDATE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=TransportationCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=TransportationCtl.OP_SAVE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=TransportationCtl.OP_RESET%>">
							<%
								}
							%>
						</div>

					</div>
				</div>
			</div>

			<div class="col-md-4"></div>
		</div>

	</form>
	</main>

	<%@include file="FooterView.jsp"%>

</body>
</html>