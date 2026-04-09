<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.project_3.util.HTMLUtility"%>
<%@page import="in.co.rays.project_3.controller.CandidateCtl"%>
<%@page import="in.co.rays.project_3.util.ServletUtility"%>
<%@page import="in.co.rays.project_3.util.DataUtility"%>
<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Candidate View</title>
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
	</div>

	<main>
	<form action="<%=ORSView.CANDIDATE_CTL%>" method="post">

		<div class="row pt-3 pb-4">

			<jsp:useBean id="dto" class="in.co.rays.project_3.dto.CandidateDTO"
				scope="request" />

			<div class="col-md-4"></div>

			<div class="col-md-4">
				<div class="card">
					<div class="card-body">

						<%
							long id = DataUtility.getLong(request.getParameter("id"));
							if (id > 0) {
						%>
						<h3 class="text-center text-primary">Update Candidate</h3>
						<%
							} else {
						%>
						<h3 class="text-center text-primary">Add Candidate</h3>
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
							<div class="alert alert-success alert-dismissible text-center">
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
							<div class="alert alert-danger alert-dismissible text-center">
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

						<!-- Candidate Code -->
						<span><b>Candidate Code</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-code"></i>
								</div>
							</div>
							<input type="text" name="candidateCode" class="form-control"
								placeholder="Enter Candidate Code"
								value="<%=DataUtility.getStringData(dto.getCandidateCode())%>">
						</div>
						<font color="red"> <%=ServletUtility.getErrorMessage("candidateCode", request)%></font><br>

						<!-- Candidate Name -->
						<span><b>Candidate Name</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-user"></i>
								</div>
							</div>
							<input type="text" name="candidateName" class="form-control"
								placeholder="Enter Candidate Name"
								value="<%=DataUtility.getStringData(dto.getCandidateName())%>">
						</div>
						<font color="red"> <%=ServletUtility.getErrorMessage("candidateName", request)%></font><br>

						<!-- Email -->
						<span><b>Email</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-envelope"></i>
								</div>
							</div>
							<input type="email" name="email" class="form-control"
								placeholder="Enter Email"
								value="<%=DataUtility.getStringData(dto.getEmail())%>">
						</div>
						<font color="red"> <%=ServletUtility.getErrorMessage("email", request)%></font><br>

						<!-- Skill Set -->
						<span><b>Skill Set</b><span style="color: red">*</span></span>
						<div class="input-group">
							<div class="input-group-prepend">
								<div class="input-group-text">
									<i class="fa fa-cogs"></i>
								</div>
							</div>
							<%=HTMLUtility.getList("skillSet", dto.getSkillSet(), map) %>
						</div>
						<font color="red"> <%=ServletUtility.getErrorMessage("skillSet", request)%></font><br>

						<br>

						<!-- Buttons -->
						<div class="text-center">
							<%
								if (id > 0) {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=CandidateCtl.OP_UPDATE%>"> <input
								type="submit" name="operation" class="btn btn-warning"
								value="<%=CandidateCtl.OP_CANCEL%>">
							<%
								} else {
							%>
							<input type="submit" name="operation" class="btn btn-success"
								value="<%=CandidateCtl.OP_SAVE%>"> <input type="submit"
								name="operation" class="btn btn-warning"
								value="<%=CandidateCtl.OP_RESET%>">
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