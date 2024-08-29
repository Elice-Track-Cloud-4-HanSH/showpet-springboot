function showPreviewImage(input) {
    const previewSection = document.getElementById("preview-section");
    const preview = document.getElementById('preview')
    const imgDeleteBtn = document.getElementById('img-delete-btn')
    if (input.files && input.files[0]) {
        previewSection.style.display = "block";
        imgDeleteBtn.style.display = "block";
        let reader = new FileReader();
        reader.onload = (e) => {
            preview.src = e.target.result;
            preview.style.display = "block";
            preview.style.maxWidth = "100%";
            preview.style.maxHeight = "auto";
        }
        reader.readAsDataURL(input.files[0]);
    } else {
        preview.src = "#";
        preview.style.display = "none";
    }
}

function removeImage(input) {
    const previewSection = document.getElementById("preview-section");
    const deleteFlagText = document.getElementById("deleted");
    const preview = document.getElementById('preview');
    const fileSection = document.getElementById('fileInput');
    fileSection.value = null;
    deleteFlagText.value = "t";
    preview.src = "#";
    previewSection.style.display = "none";
}