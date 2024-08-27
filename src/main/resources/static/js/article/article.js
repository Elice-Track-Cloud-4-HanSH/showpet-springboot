let currentArticle;
let currentBoard;
let currentComment; // 삭제할 때 쓰는 commentId
let elementToDelete; // 삭제할 댓글 박스

function handleDeleteArticleButton(_, postId) {
    currentArticle = postId;
}

function handleDeleteArticleButton(_, postId, boardId) {
    currentArticle = postId;
    currentBoard = boardId;
}

function handleDeleteArticleModalButton() {
    currentBoard = 1;
    fetch(`/articles/${currentArticle}`, {
        method: "DELETE",
    }).then(() => (window.location.href = `/boards/${currentBoard}`));
}

// 댓글 삭제 버튼 클릭
function handleDeleteCommentButton(e, commentId) {
    currentComment = commentId;
    elementToDelete = e.target.closest(".card");
}

// 실제 댓글 삭제(알림창)
function handleDeleteCommentModalButton() {
    fetch(`/comments/${currentComment}`, {
        method: "DELETE",
    }).then(() => elementToDelete.remove());

    const commentsCount = document.getElementById("commentsCount");
    commentsCount.innerText = parseInt(commentsCount.innerText) - 1;

    const modal = bootstrap.Modal.getInstance("#deleteCommentModal");
    modal.hide();
}

function handleEditCommentButton(content, commentId, articleId) {
    document.getElementById("commentContentTextarea").innerText = content;
    document
        .getElementById("commentEditForm")
        .setAttribute("action", `/comments/edit/${commentId}?articleId=${articleId}`);
}