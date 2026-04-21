<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.LimitCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Limit View</title>
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
	<form action="<%=ORSView.LIMIT_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.LimitDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Limit</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Limit</h3>
						<%
							}
						%>

						<%
							HashMap statusMap = (HashMap) request.getAttribute("statusMap");
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

						<!-- Limit Code -->
						<span><b>Limit Code</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-code"></i>
								</div>
							</div>
							<input type="text" name="limitCode" class="form-control"
								placeholder="Enter Limit Code"
								value="<%=DataUtility.getStringData(dto.getLimitCode())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("limitCode", request)%></font><br>

						<!-- Limit Name -->
						<span><b>Limit Name</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-tag"></i>
								</div>
							</div>
							<input type="text" name="limitName" class="form-control"
								placeholder="Enter Limit Name"
								value="<%=DataUtility.getStringData(dto.getLimitName())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("limitName", request)%></font><br>

						<!-- Max Value -->
						<span><b>Max Value</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-sort-numeric-asc"></i>
								</div>
							</div>
							<input type="text" name="maxValue" class="form-control"
								placeholder="Enter Max Value"
								value="<%=DataUtility.getStringData(dto.getMaxValue())%>">
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("maxValue", request)%></font><br>

						<!-- Status -->
						<span><b>Status</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-toggle-on"></i>
								</div>
							</div>
							<%=HTMLUtility.getList("status", dto.getStatus(), statusMap)%>
						</div>
						<font color="red"><%=ServletUtility.getErrorMessage("status", request)%></font><br>

						<br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=LimitCtl.OP_UPDATE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=LimitCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=LimitCtl.OP_SAVE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=LimitCtl.OP_RESET%>">
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