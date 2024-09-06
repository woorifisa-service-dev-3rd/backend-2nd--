<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <ul>
        <c:forEach items="${article.comments}" var="comment">
            <li>
                <b>${comment.author.nickName}</b>
                <p>${comment.content}</p>
                <button>수정</button>
                <button>삭제</button>
            </li>
        </c:forEach>
    </ul>
    <!-- 댓글 입력 폼 -->
    <div class="comment_form">
        <form action="${pageContext.request.contextPath}/articles/${article.id}/comments" method="post">
            <div>
                <textarea name="content" rows="4" cols="50" placeholder="댓글을 입력하세요..."></textarea>
            </div>
            <button type="submit">댓글추가</button>
        </form>
    </div>
</div>
<a href="/articles/list">목록으로 돌아가기</a>
</body>
</html>