<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 목록</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
    </style>
</head>
<body>
<h1>게시판 목록</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>제목</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${requestScope.articles}" var="article">
        <tr>
            <td>${article.id}</td>
            <td><a href="view/${article.id}">${article.title}</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>