<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 상세보기</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .post {
            margin: 20px;
            padding: 20px;
            border: 1px solid #ddd;
        }
        .post h2 {
            margin-top: 0;
        }
    </style>
</head>
<body>
<div class="article">
    <h2>${article.title}</h2>
    <p>${article.content}</p>
</div>
<div class="comment_list">

</div>
<a href="/articles/list">목록으로 돌아가기</a>
</body>
</html>