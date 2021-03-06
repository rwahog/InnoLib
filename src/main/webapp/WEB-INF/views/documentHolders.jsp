<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>KitapHana</title>
	<link rel="icon" href="/resources/images/favicon-16x16.png" type="image/x-icon">
	<link href="${path}webjars/bootstrap/4.0.0/css/bootstrap.min.css"
				rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/common.css"
				rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/list.css"
				rel="stylesheet">
</head>
<body>
<%@include file="header.jsp" %>
<main class="body container">
	<div class="panel container-fluid mx-auto px-0">
		<h2>
			Document holders
		</h2>
		<table class="table table-hover table-dark documents">
			<thead>
			<tr>
				<th scope="col">#</th>
				<th scope="col">Name</th>
				<th scope="col">Surname</th>
				<th scope="col">Type</th>
				<th scope="col">Deadline/Fine</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach var="holder" items="${users}">
				<tr>
					<td>${holder.getId()}</td>
					<td>${holder.getName()}</td>
					<td>${holder.getSurname()}</td>
					<td>${holder.getType()}</td>
					<c:choose>
						<c:when test="${holder.getDocumentFine() != 0}">
							<td>${holder.getDocumentFine()} &#8381;</td>
						</c:when>
						<c:otherwise>
							<td>${holder.getDocumentDeadline()} days</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="form-row">
			<div class="form-group col-12 col-md-3">
				<a class="btn btn-block" id="button" onclick="goBack()">Back</a>
			</div>
		</div>
	</div>
</main>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.2.1.slim.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/masonry.pkgd.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
<script>
  function goBack() {
    window.history.back();
  }
</script>
</body>
</html>
