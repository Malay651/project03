<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>

<%@page import="in.co.rays.project_3.controller.InventoryCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Inventory View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<style type="text/css">
.p4 {
	background-image: url('<%=ORSView.APP_CONTEXT%>/img/user1.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	padding-top: 75px;
}
</style>
</head>

<body class="p4">

	<div class="header">
		<%@include file="Header.jsp"%>
	</div>

	<main>
	<form action="<%=ORSView.INVENTORY_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.InventoryDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Inventory</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Inventory</h3>
						<%
							}
						%>
						<%
							HashMap map = (HashMap) request.getAttribute("map");
						%>

						<!-- Success Message -->
						<h4 align="center">
							<%
								if (!ServletUtility.getSuccessMessage(request).equals("")) {
							%>
							<div class="alert alert-success alert-dismissible">
								<button type="button" class="close" data-dismiss="alert">&times;</button>
								<%=ServletUtility.getSuccessMessage(request)%>
							</div>
							<%
								}
							%>
						</h4>

						<!-- Error Message -->
						<h4 align="center">
							<%
								if (!ServletUtility.getErrorMessage(request).equals("")) {
							%>
							<div class="alert alert-danger alert-dismissible">
								<button type="button" class="close" data-dismiss="alert">&times;</button>
								<%=ServletUtility.getErrorMessage(request)%>
							</div>
							<%
								}
							%>
						</h4>

						<!-- Hidden Fields -->
						<input type="hidden" name="id" value="<%=dto.getId()%>"> <input
							type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy"
							value="<%=dto.getModifiedBy()%>"> <input type="hidden"
							name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Item Code -->
						<span><b>Item Code</b><span style="color: red">*</span></span> <input
							type="text" name="itemCode" class="form-control"
							placeholder="Enter Item Code"
							value="<%=DataUtility.getStringData(dto.getItemCode())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("itemCode", request)%>
						</font><br>

						<!-- Item Name -->
						<span><b>Item Name</b><span style="color: red">*</span></span> <input
							type="text" name="itemName" class="form-control"
							placeholder="Enter Item Name"
							value="<%=DataUtility.getStringData(dto.getItemName())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("itemName", request)%>
						</font><br>

						<!-- Quantity -->
						<span><b>Quantity</b><span style="color: red">*</span></span> <input
							type="text" name="quantity" class="form-control"
							placeholder="Enter Quantity"
							value="<%=DataUtility.getStringData(dto.getQuantity())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("quantity", request)%>
						</font><br>

						<!-- Price -->
						<span><b>Price</b><span style="color: red">*</span></span> <input
							type="text" name="price" class="form-control"
							placeholder="Enter Price"
							value="<%=DataUtility.getStringData(dto.getPrice())%>"> <font
							color="red"> <%=ServletUtility.getErrorMessage("price", request)%>
						</font><br>

						<!-- Item Status -->
						<span><b>Status</b><span style="color: red">*</span> <%=HTMLUtility.getList("itemStatus", dto.getItemStatus(), map)%>

							<font color="red" class="pl-sm-5"> <%=ServletUtility.getErrorMessage("itemStatus", request)%>
							</font>
							
							<br> <!-- Buttons -->
							<div class="text-center">
								<%
									if (id > 0) {
								%>
								<input type="submit" name="operation" class="btn btn-success"
									value="<%=InventoryCtl.OP_UPDATE%>"> <input
									type="submit" name="operation" class="btn btn-warning"
									value="<%=InventoryCtl.OP_CANCEL%>">
								<%
									} else {
								%>
								<input type="submit" name="operation" class="btn btn-success"
									value="<%=InventoryCtl.OP_SAVE%>"> <input type="submit"
									name="operation" class="btn btn-warning"
									value="<%=InventoryCtl.OP_RESET%>">
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