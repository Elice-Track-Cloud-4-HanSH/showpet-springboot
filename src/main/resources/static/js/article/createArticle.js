function showPreviewImage(input) {
    const preview = document.getElementById('preview')
    if (input.files && input.files[0]) {
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