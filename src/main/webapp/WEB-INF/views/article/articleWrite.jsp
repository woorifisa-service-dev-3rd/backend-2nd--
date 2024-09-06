<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 작성하기</title>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
    </style>
</head>
<body>
<div class="article">
    <h2>게시물 등록하기</h2>
    <form action="/articles/write" method="post">
        <h3><input type="text" name="title" value="${article.title}" placeholder="제목을 입력해주세요." /></h3>
        <p><textarea name="content" placeholder="내용을 입력해주세요.">${article.content}</textarea></p>
        <button type="submit">저장하기</button>
    </form>
</div>
<a href="/articles/list">목록으로 돌아가기</a>
</body>
</html>