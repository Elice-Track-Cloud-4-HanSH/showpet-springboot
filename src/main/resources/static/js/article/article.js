let currentArticle;
let currentCategory;
let currentComment; // 삭제할 때 쓰는 commentId
let elementToDelete; // 삭제할 댓글 박스

function handleDeleteArticleButton(_, postId, boardId) {
    currentArticle = postId;
    currentCategory = boardId;
}

function handleDeleteArticleModalButton() {
    fetch(`/articles/${currentArticle}`, {
        method: "DELETE",
    }).then(() => (window.location.href = `/category/${currentCategory}`));
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

function checkArticlePasswordIsCorrect(articleId) {
    const password = document.getElementById("delete-password");
    const delBtn = document.getElementById("delete-post");
    fetch(`/api/articles/validate-password`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json" // 요청 본문의 형식 지정
        },
        body: JSON.stringify({
            "password": password.value,
            "articleId": articleId
        })
    }).then((res) => {
        if (res.status === 200) {
            delBtn.disabled = false;
            delBtn.classList.remove("disabled");
        } else {
            throw new Error("올바르지 않은 비밀번호입니다. 다시 입력해주세요.");
        }
    }).catch((err) => {
        delBtn.disabled = true;
        delBtn.classList.add("disabled");
        alert(err.message);
    })
}

function handleEditCommentButton(content, commentId, articleId) {
    document.getElementById("commentContentTextarea").innerText = content;
    document
        .getElementById("commentEditForm")
        .setAttribute("action", `/comments/edit/${commentId}?articleId=${articleId}`);
}