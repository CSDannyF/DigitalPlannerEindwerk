// Show preview of selecter image
function previewAvatar(input) {
    var preview = document.getElementById('avatar-preview');
    var file = input.files[0];
    var reader = new FileReader();

    reader.onloadend = function() {
    preview.src = reader.result;
    }

    if (file) {
    reader.readAsDataURL(file);
    } else {
    preview.src = "#";
    }
}