
// get input of file-input box
$(function () {
    $(".custom-file-input").bind("input propertychange", function () {
        var fieldVal = $(this).val();
        fieldVal = fieldVal.replace("C:\\fakepath\\", "");

        var file_label = document.getElementById("file_label_id");
        file_label.textContent = fieldVal;
        console.log(fieldVal);

    });
});