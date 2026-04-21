<%@page import="in.co.rays.project_3.dto.TransportationDTO"%>
<%@page import="in.co.rays.project_3.controller.TransportationListCtl"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Transportation List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/al.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 85px;
}

.text {
	text-align: center;
}
</style>
</head>

<%@include file="Header.jsp"%>

<body class="hm">

	<div>
		<form class="pb-5" action="<%=ORSView.TRANSPORTATION_LIST_CTL%>"
			method="post">

			<jsp:useBean id="dto"
				class="in.co.rays.project_3.dto.TransportationDTO" scope="request" />

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				int nextPageSize = 0;
				if (request.getAttribute("nextListSize") != null) {
					nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());
				}

				List list = ServletUtility.getList(request);
				if (list == null) {
					list = new java.util.ArrayList();
				}

				Iterator<TransportationDTO> it = list.iterator();
			%>

			<center>
				<h1 class="text-dark font-weight-bold pt-3">
					<u>Transportation List</u>
				</h1>
			</center>

			<!-- Success Message -->
			<div class="row">
				<div class="col-md-4"></div>
				<%
					if (!ServletUtility.getSuccessMessage(request).equals("")) {
				%>
				<div class="col-md-4 alert alert-success alert-dismissible"
					style="background-color: #80ff80">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="#008000"> <%=ServletUtility.getSuccessMessage(request)%>
						</font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>

			<!-- Error Message -->
			<div class="row">
				<div class="col-md-4"></div>
				<%
					if (!ServletUtility.getErrorMessage(request).equals("")) {
				%>
				<div class="col-md-4 alert alert-danger alert-dismissible">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<h4>
						<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
						</font>
					</h4>
				</div>
				<%
					}
				%>
				<div class="col-md-4"></div>
			</div>

			<%
				if (list.size() != 0) {
			%>

			<!-- Search Panel -->
			<div class="row mb-3">
				<div class="col-sm-3"></div>

				<div class="col-sm-3">
					<input type="text" name="Description"
						placeholder="Enter Description" class="form-control"
						value="<%=ServletUtility.getParameter("Description", request)%>">
				</div>

				<div class="col-sm-3">
					<input type="text" name="Mode" placeholder="Enter Mode"
						class="form-control"
						value="<%=ServletUtility.getParameter("Mode", request)%>">
				</div>

				<div class="col-sm-3">
					<input type="submit" class="btn btn-primary btn-md"
						name="operation" value="<%=TransportationListCtl.OP_SEARCH%>">
					<input type="submit" class="btn btn-dark btn-md" name="operation"
						value="<%=TransportationListCtl.OP_RESET%>">
				</div>
			</div>

			<!-- Table -->
			<div class="table-responsive">
				<table class="table table-bordered table-dark table-hover">
					<thead>
						<tr style="background-color: #8C8C8C;">
							<th width="7%"><input type="checkbox" id="select_all">
								Select All</th>
							<th width="5%" class="text">S.No</th>
							<th width="20%" class="text">Description</th>
							<th width="15%" class="text">Mode</th>
							<th width="20%" class="text">Order Date</th>
							<th width="15%" class="text">Cost</th>
							<th width="8%" class="text">Edit</th>
						</tr>
					</thead>
					<tbody>
						<%
							while (it.hasNext()) {
									dto = it.next();
						%>
						<tr>
							<td align="center"><input type="checkbox" class="checkbox"
								name="ids" value="<%=dto.getId()%>"></td>
							<td class="text"><%=index++%></td>
							<td class="text"><%=dto.getDescription()%></td>
							<td class="text"><%=dto.getMode()%></td>
							<td class="text"><%=DataUtility.getDateString(dto.getOrderDate())%></td>
							<td class="text"><%=dto.getCost()%></td>
							<td class="text"><a
								href="TransportationCtl?id=<%=dto.getId()%>">Edit</a></td>
						</tr>
						<%
							}
						%>
					</tbody>
				</table>
			</div>

			<!-- Buttons -->
			<table width="100%">
				<tr>
					<td><input type="submit" name="operation"
						class="btn btn-warning btn-md"
						value="<%=TransportationListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td><input type="submit" name="operation"
						class="btn btn-primary btn-md"
						value="<%=TransportationListCtl.OP_NEW%>"></td>

					<td><input type="submit" name="operation"
						class="btn btn-danger btn-md"
						value="<%=TransportationListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						class="btn btn-warning btn-md"
						value="<%=TransportationListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<br>

			<div style="padding-left: 48%;">
				<input type="submit" name="operation" class="btn btn-primary btn-md"
					value="<%=TransportationListCtl.OP_BACK%>">
			</div>

			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

		</form>
	</div>

</body>

<%@include file="FooterView.jsp"%>
</html>