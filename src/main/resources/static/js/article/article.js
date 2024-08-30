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

// 비밀번호 입력 모달
function openPasswordModal(){
    const passwordModalElement = document.getElementById('passwordModal');
    const passwordModal = new bootstrap.Modal(passwordModalElement);
    passwordModal.show();
}

document.getElementById('submitPasswordButton').addEventListener('click', function () {
    // 비밀번호 입력값 가져오기
    const passwordInput = document.getElementById('commentPassword');
    const passwordValue = passwordInput.value.trim();

    if (passwordValue) {
        // 비밀번호를 폼에 추가
        const commentForm = document.getElementById('commentForm');
        const passwordField = document.createElement('input');
        passwordField.type = 'hidden';
        passwordField.name = 'password';
        passwordField.value = passwordValue;
        commentForm.appendChild(passwordField);

        // 폼을 제출
        commentForm.submit();
    } else {
        alert('비밀번호를 입력하세요.');
    }
});

// 댓글 삭제 버튼 클릭
function handleDeleteCommentButton(e, commentId) {
    currentComment = commentId;
    elementToDelete = e.target.closest(".card");
}

// 실제 댓글 삭제(알림창)
function handleDeleteCommentModalButton() {
    const password = document.getElementById("deleteCommentPassword").value;

    fetch(`/comments/${currentComment}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({password: password})
    }).then(response => {
        if (response.status == 204) {
            elementToDelete.remove();
            const commentsCount = document.getElementById("commentsCount");
            commentsCount.innerText = parseInt(commentsCount.innerText) - 1;

            const modal = bootstrap.Modal.getInstance("#deleteCommentModal");
            modal.hide();
        } else if (response.status == 403) {
            alert("비밀번호가 일치하지 않습니다.");
        } else {
            alert("댓글 삭제에 실패했습니다.");
        }
    });
}

let errorMessage = /*[[${errorMessage}]]*/ '';
if(errorMessage) {
    let commentContent = document.getElementById('commentContent');
    commentContent.focus();
}

document.addEventListener("DOMContentLoaded", function() {
    const editCommentModalElement = document.getElementById('editCommentModal');
    const editCommentModal = new bootstrap.Modal(editCommentModalElement);

    // 댓글 수정 버튼 클릭 시 호출되는 함수
    window.handleEditCommentButton = function(content, commentId, articleId) {
        // 댓글 내용 설정
        document.getElementById("commentContentTextarea").value = content;
        document.getElementById("commentEditForm")
            .setAttribute("action", `/comments/edit/${commentId}?articleId=${articleId}`);

        // 검증 오류 메시지 제거
        const updateErrorMessage = document.querySelector("#editCommentModal .text-danger");
        if (updateErrorMessage) {
            updateErrorMessage.innerText = "";
        }

        // 모달창 열기
        editCommentModal.show();
        document.getElementById('commentContentTextarea').focus();
    };

    // 모달 닫기 처리
    editCommentModalElement.addEventListener('hide.bs.modal', function () {
        // 모달 닫기 전 초기화
        document.getElementById("commentContentTextarea").value = "";
        const updateErrorMessage = document.querySelector("#editCommentModal .text-danger");
        if (updateErrorMessage) {
            updateErrorMessage.innerText = "";
        }
    });

    // 모달이 완전히 닫힌 후 호출되는 이벤트
    editCommentModalElement.addEventListener('hidden.bs.modal', function () {
        // 마지막으로 클릭한 댓글 수정 버튼으로 포커스 이동
        const lastFocusedButton = document.querySelector('.btn-outline-primary[data-bs-target="#editCommentModal"]');
        if (lastFocusedButton) {
            lastFocusedButton.focus();
        }
    });

    // 검증 실패 시 모달 자동 표시 및 포커스
    let updateErrorMessage = /*[[${updateErrorMessage}]]*/ '';
    if (updateErrorMessage) {
        editCommentModal.show();
        document.getElementById('commentContentTextarea').focus();
    }

    let diffPasswordMessage = /*[[${diffPasswordMessage}]]*/ '';
    if(diffPasswordMessage) {
        editCommentModal.show();
        document.getElementById('commentContentTextarea').focus();
    }

    // 댓글 삭제 모달 초기화
    const deleteCommentModal = document.getElementById('deleteCommentModal');
    deleteCommentModal.addEventListener('hide.bs.modal', function(){
        document.getElementById('deleteCommentPassword').value = '';
    });
});