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
    <script defer>
        async function deleteComment(articleId, commentId) {
            if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                try {
                    const response = await fetch(`/articles/view/${articleId}/comments/${commentId}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    if (response.ok) {
                        // 댓글 삭제가 성공적일 경우 페이지를 새로고침
                        window.location.reload();
                    } else {
                        alert('댓글 삭제에 실패했습니다.');
                    }
                } catch (error) {
                    console.error('삭제 요청 중 오류 발생:', error);
                    alert('댓글 삭제 요청 중 오류가 발생했습니다.');
                }
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