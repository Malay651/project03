<%@page import="in.co.rays.project_3.dto.InventoryDTO"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.controller.InventoryListCtl"%>
<%@page import="in.co.rays.project_3.controller.InventoryCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Inventory List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ORSView.APP_CONTEXT%>/js/CheckBox11.js"></script>

<style>
.hm {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/12912.jpg');
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
		<form class="pb-5" action="<%=ORSView.INVENTORY_LIST_CTL%>"
			method="post">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.InventoryDTO"
				scope="request" />

			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;
				int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

				List list = ServletUtility.getList(request);
				Iterator<InventoryDTO> it = list.iterator();
			%>

			<%
				if (list.size() != 0) {
			%>

			<center>
				<h1 class="text-dark font-weight-bold pt-3">
					<u>Inventory List</u>
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

			<!-- Search Panel -->
			<div class="row">

				<div class="col-sm-3"></div>

				<div class="col-sm-2">
					<input type="text" name="itemCode" placeholder="Enter Item Code"
						class="form-control"
						value="<%=ServletUtility.getParameter("itemCode", request)%>">
				</div>

				<div class="col-sm-2">
					<input type="text" name="itemName" placeholder="Enter Item Name"
						class="form-control"
						value="<%=ServletUtility.getParameter("itemName", request)%>">
				</div>

				<div class="col-sm-2">
					<select name="itemStatus" class="form-control">
						<option value="">--Select Status--</option>
						<option value="Available"
							<%="Available".equals(ServletUtility.getParameter("itemStatus", request)) ? "selected" : ""%>>Available</option>
						<option value="Out Of Stock"
							<%="Out Of Stock".equals(ServletUtility.getParameter("itemStatus", request)) ? "selected" : ""%>>Out
							Of Stock</option>
					</select>
				</div>

				<div class="col-sm-3">
					<input type="submit" class="btn btn-primary btn-md"
						name="operation" value="<%=InventoryListCtl.OP_SEARCH%>">
					<input type="submit" class="btn btn-dark btn-md" name="operation"
						value="<%=InventoryListCtl.OP_RESET%>">
				</div>

				<div class="col-sm-2"></div>
			</div>

			<br>

			<!-- Table -->
			<div class="table-responsive">
				<table class="table table-bordered table-dark table-hover">
					<thead>
						<tr style="background-color: #8C8C8C;">
							<th width="10%"><input type="checkbox" id="select_all">
								Select All</th>
							<th width="5%" class="text">S.No</th>
							<th width="15%" class="text">Item Code</th>
							<th width="20%" class="text">Item Name</th>
							<th width="10%" class="text">Quantity</th>
							<th width="10%" class="text">Price</th>
							<th width="10%" class="text">Status</th>
							<th width="5%" class="text">Edit</th>
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
							<td class="text"><%=dto.getItemCode()%></td>
							<td class="text"><%=dto.getItemName()%></td>
							<td class="text"><%=dto.getQuantity()%></td>
							<td class="text"><%=dto.getPrice()%></td>
							<td class="text"><%=dto.getItemStatus()%></td>
							<td class="text"><a href="InventoryCtl?id=<%=dto.getId()%>">Edit</a></td>
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
						value="<%=InventoryListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td><input type="submit" name="operation"
						class="btn btn-primary btn-md"
						value="<%=InventoryListCtl.OP_NEW%>"></td>

					<td><input type="submit" name="operation"
						class="btn btn-danger btn-md"
						value="<%=InventoryListCtl.OP_DELETE%>"></td>

					<td align="right"><input type="submit" name="operation"
						class="btn btn-warning btn-md"
						value="<%=InventoryListCtl.OP_NEXT%>"
						<%=(nextPageSize != 0) ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
				} else {
			%>

			<center>
				<h1 style="font-size: 40px; color: #162390;">Inventory List</h1>
			</center>

			<br>

			<div style="padding-left: 48%;">
				<input type="submit" name="operation" class="btn btn-primary btn-md"
					value="<%=InventoryListCtl.OP_BACK%>">
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