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
        .edit-form {
            display: none;
        }
    </style>
    <script>
        const articleDelete = (id) => {
            if(confirm("삭제하시겠습니까?")){
                fetch(`/articles/delete/${id}`,{
                    method: "GET",
                    headers: {
                        "Accept" : "application/json"
                    }
                }).then(response =>{
                    if(!response.ok){
                        throw new Error("에러가 발생했습니다!");
                    }
                }).then(data =>{
                    alert("삭제가 완료되었습니다!");
                    location.href="/articles/list";
                }).catch(error => {
                    alert("오류: " + error.message);
                });
            }
        }

        function showEditForm(commentId) {
            document.getElementById('edit-form-' + commentId).style.display = 'block';
            document.getElementById('content-wrapper-' + commentId).style.display = 'none';
        }
    </script>
</head>
<body>
<div class="article">
    <h2>${article.title}</h2>
    <p>${article.content}</p>
    <a href="/articles/edit/${article.id}">수정하기</a>
    <button onClick="articleDelete(${article.id})">삭제하기</button>
</div>
<div class="comment_list">
    <ul>
        <c:forEach items="${article.comments}" var="comment">
            <li>
                <b>${comment.author.nickName}</b>
                <div id="content-wrapper-${comment.id}">
                    <p>${comment.content}</p>
                    <button onclick="showEditForm(${comment.id})">수정</button>
                    <!-- 댓글 삭제 -->
                    <form action="${article.id}/comments/delete/${comment.id}" method="post" style="display:inline;">
                        <input type="hidden" name="_method" value="delete" />
                        <button type="submit">삭제</button>
                    </form>
                </div>
                <!-- 댓글 수정 폼 -->
                <form id="edit-form-${comment.id}" class="edit-form" action="${article.id}/comments/update/${comment.id}" method="post">
                    <div>
                        <input type="hidden" name="_method" value="put" />
                        <textarea name="content" rows="4" cols="50">${comment.content}</textarea>
                    </div>
                    <button type="submit">수정 완료</button>
                </form>
            </li>
        </c:forEach>
    </ul>
    <!-- 댓글 입력 폼 -->
    <div class="comment_form">
        <form action="${article.id}/comments" method="post">
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