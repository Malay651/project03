<%@page import="in.co.rays.project_3.controller.APIKeyCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>API Key View</title>
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
	<form action="<%=ORSView.APIKEY_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.APIKeyDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update API Key</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add API Key</h3>
						<%
							}
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

						<!-- API Key Code -->
						<span><b>API Key Code</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-key"></i>
								</div>
							</div>
							<input type="text" name="apikeyCode" class="form-control"
								placeholder="Enter API Key Code"
								value="<%=DataUtility.getStringData(dto.getApikeyCode())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("apikeyCode", request)%>
						</font><br>

						<!-- Key Value -->
						<span><b>Key Value</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-lock"></i>
								</div>
							</div>
							<input type="text" name="keyvalue" class="form-control"
								placeholder="Enter Key Value"
								value="<%=DataUtility.getStringData(dto.getKeyvalue())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("keyvalue", request)%>
						</font><br>

						<!-- Issued To -->
						<span><b>Issued To</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-user"></i>
								</div>
							</div>
							<input type="text" name="issuedTo" class="form-control"
								placeholder="Enter Issued To"
								value="<%=DataUtility.getStringData(dto.getIssuedTo())%>">
						</div>
						<font color="red">
							<%=ServletUtility.getErrorMessage("issuedTo", request)%>
						</font><br>

						<!-- Status -->
						<span><b>Status</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-info-circle"></i>
								</div>
							</div>
							<input type="text" name="status" class="form-control"
								placeholder="Enter Status"
								value="<%=DataUtility.getStringData(dto.getStatus())%>">
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
								value="<%=APIKeyCtl.OP_UPDATE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=APIKeyCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=APIKeyCtl.OP_SAVE%>">
							<input type="submit" name="operation" class="btn btn-warning"
								value="<%=APIKeyCtl.OP_RESET%>">
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