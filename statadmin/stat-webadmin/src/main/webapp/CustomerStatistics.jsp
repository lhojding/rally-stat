<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Statistik �ver kundernas tj�nsteanv�ndning</title>
<link rel="stylesheet" href="css/style.css" type="text/css"/>
</head>
<body>
<h1>Statistik �ver kundernas tj�nsteanv�ndning</h1>
<a href="index.jsp">Index</a>
<a href="CustomerStatistics.jsp">Tomt s�kformul�r</a>
<p/>
<%@ page import="se.uc.stat.web.Services" %>
<%@ page import="se.uc.stat.web.CustomerStatistics" %>
<%
final Services services = new Services();
final CustomerStatistics statistics = services.getCustomerStatistics(
        request.getParameterMap());
%>
<%@ include file="includes/searchForm.jspf" %>
</body>
</html>