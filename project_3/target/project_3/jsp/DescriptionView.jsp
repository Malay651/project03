<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.DescriptionCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Description View</title>
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
	<form action="<%=ORSView.DESCRITPION_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.DescriptionDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Department</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Department</h3>
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

						<!-- Department Name -->
						<span><b>Department Name</b><span style="color: red">*</span></span>
						<input type="text" name="departmentName" class="form-control"
							placeholder="Enter Department Name"
							value="<%=DataUtility.getStringData(dto.getDepartmentName())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("departmentName", request)%>
						</font><br>

						<!-- Department Head -->
						<span><b>Department Head</b><span style="color: red">*</span></span>
						<input type="text" name="departmentHead" class="form-control"
							placeholder="Enter Department Head"
							value="<%=DataUtility.getStringData(dto.getDepartmentHead())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("departmentHead", request)%>
						</font><br>

						<!-- Location -->
						<span><b>Location</b><span style="color: red">*</span></span> <input
							type="text" name="location" class="form-control"
							placeholder="Enter Location"
							value="<%=DataUtility.getStringData(dto.getLocation())%>">
						<font color="red"> <%=ServletUtility.getErrorMessage("location", request)%>
						</font><br>

						<!-- Status -->
						<span><b>Status</b><span style="color: red">*</span></span>
						<%=HTMLUtility.getList("status", dto.getStatus(), map)%>
						<font color="red" > <%=ServletUtility.getErrorMessage("status", request)%>
						</font> <br>
						<br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=DescriptionCtl.OP_UPDATE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=DescriptionCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=DescriptionCtl.OP_SAVE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=DescriptionCtl.OP_RESET%>">
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