<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.UsageCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Usage View</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

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
	</div>

	<main>
	<form action="<%=ORSView.USAGE_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.UsageDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Usage</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Usage</h3>
						<%
							}
						%>

						<%
							HashMap usageTypeMap = (HashMap) request.getAttribute("usageTypeMap");
						%>

						<!-- Success Message -->
						<h4 align="center">
							<%
								if (!ServletUtility.getSuccessMessage(request).equals("")) {
							%>
							<div class="alert alert-success text-center">
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
							<div class="alert alert-danger text-center">
								<%=ServletUtility.getErrorMessage(request)%>
							</div>
							<%
								}
							%>
						</h4>

						<!-- Hidden Fields -->
						<input type="hidden" name="id" value="<%=dto.getId()%>">
						<input type="hidden" name="createdBy" value="<%=dto.getCreatedBy()%>">
						<input type="hidden" name="modifiedBy" value="<%=dto.getModifiedBy()%>">
						<input type="hidden" name="createdDatetime"
							value="<%=DataUtility.getTimestamp(dto.getCreatedDatetime())%>">
						<input type="hidden" name="modifiedDatetime"
							value="<%=DataUtility.getTimestamp(dto.getModifiedDatetime())%>">

						<!-- Usage Code -->
						<span><b>Usage Code</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-code"></i>
								</div>
							</div>
							<input type="text" name="usageCode" class="form-control"
								placeholder="Enter Usage Code"
								value="<%=DataUtility.getStringData(dto.getUsageCode())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("usageCode", request)%>
						</font><br>

						<!-- Usage Name -->
						<span><b>Usage Name</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-user"></i>
								</div>
							</div>
							<input type="text" name="usageName" class="form-control"
								placeholder="Enter Usage Name"
								value="<%=DataUtility.getStringData(dto.getUsageName())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("usageName", request)%>
						</font><br>

						<!-- Usage Type -->
						<span><b>Usage Type</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-cogs"></i>
								</div>
							</div>
							<%=HTMLUtility.getList("usageType", dto.getUsageType(), usageTypeMap)%>
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("usageType", request)%>
						</font><br>

						<!-- Status -->
						<span><b>Status</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-toggle-on"></i>
								</div>
							</div>
							<select name="status" class="form-control">
								<option value="">--Select--</option>
								<option value="Active"
									<%=("Active".equals(dto.getStatus()) ? "selected" : "")%>>Active</option>
								<option value="Inactive"
									<%=("Inactive".equals(dto.getStatus()) ? "selected" : "")%>>Inactive</option>
							</select>
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("status", request)%>
						</font><br>

						<br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=UsageCtl.OP_UPDATE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=UsageCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=UsageCtl.OP_SAVE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=UsageCtl.OP_RESET%>">
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