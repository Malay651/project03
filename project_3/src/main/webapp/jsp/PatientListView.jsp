<%@page import="in.co.rays.project_3.dto.InventoryDTO"%>
<%@page import="in.co.rays.project_3.controller.InventoryListCtl"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Inventory List</title>

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

	<form action="<%=ORSView.INVENTORY_LIST_CTL%>" method="post">

		<jsp:useBean id="dto" class="in.co.rays.project_3.dto.InventoryDTO"
			scope="request" />

		<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			int nextPageSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			List list = ServletUtility.getList(request);

			if (list != null && list.size() > 0) {
		%>

		<center>
			<h1 class="text-dark font-weight-bold pt-3">
				<u>Inventory List</u>
			</h1>
		</center>

		<!-- SUCCESS MESSAGE -->
		<%
			if (!ServletUtility.getSuccessMessage(request).equals("")) {
		%>
		<center>
			<div class="col-md-4 alert alert-success text-center">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<h4>
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%></font>
				</h4>
			</div>
		</center>
		<%
			}
		%>

		<!-- ERROR MESSAGE -->
		<%
			if (!ServletUtility.getErrorMessage(request).equals("")) {
		%>
		<center>
			<div class="col-md-4 alert alert-danger text-center">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<h4>
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
				</h4>
			</div>
		</center>
		<%
			}
		%>

		<div class="row mb-3">
			<div class="col-sm-3"></div>
			<div class="col-sm-3">
				<input type="text" name="itemCode" placeholder="Enter Item Code"
					class="form-control">
			</div>
			<div class="col-sm-3">
				<input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_SEARCH%>" class="btn btn-primary">
				<input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_RESET%>" class="btn btn-dark">
			</div>
		</div>

		<!-- TABLE -->
		<div class="table-responsive">
			<table class="table table-dark table-bordered table-hover">
				<thead>
					<tr>
						<th width="7%"><input type="checkbox" id="select_all" name="select" class="text">
						Select All</th>
						<th width="5%" class="text">S.No</th>
						<th width="15%" class="text">Item Code</th>
						<th width="15%" class="text">Item Name</th>
						<th width="10%" class="text">Quantity</th>
						<th width="10%" class="text">Price</th>
						<th width="10%" class="text">Status</th>
						<th width="8%" class="text">Edit</th>
					</tr>
				</thead>

				<tbody>
					<%
						Iterator<InventoryDTO> it = list.iterator();
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
						<td class="text"><a href="InventoryCtl?id=<%=dto.getId()%>">Edit</a>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
		</div>

		<table width="100%">
			<tr>
				<td><input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_PREVIOUS%>" class="btn btn-warning"
					<%=pageNo == 1 ? "disabled" : ""%>></td>

				<td><input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_NEW%>" class="btn btn-primary">
				</td>

				<td><input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_DELETE%>" class="btn btn-danger">
				</td>

				<td align="right"><input type="submit" name="operation"
					value="<%=InventoryListCtl.OP_NEXT%>" class="btn btn-warning"
					<%=nextPageSize != 0 ? "" : "disabled"%>></td>
			</tr>
		</table>

		<%
			} else {
		%>

		<center>
			<h2>No Inventory Found</h2>
		</center>

		<%
			}
		%>

		<input type="hidden" name="pageNo" value="<%=pageNo%>"> 
		<input type="hidden" name="pageSize" value="<%=pageSize%>">

	</form>

</body>

<%@include file="FooterView.jsp"%>
</html>