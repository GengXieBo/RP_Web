var slide_patch_rows = 2;
var slide_patch_cols = 5;
var js_active_slide = 0;
function slide_click(slide_id, cal_flag, slide_path, scores) {
    var slide_item = document.getElementById(slide_id);
    slide_item.classList.add("active")

    var active_slide_item = document.getElementById(js_active_slide);
    active_slide_item.classList.remove("active")
    js_active_slide = slide_id;

    // set slide path
    var slide_path_item = document.getElementById("slide_path_id");
    slide_path_item.textContent = slide_path;

    // set slide thumbnail img
    var slide_thumbnail = document.getElementById("slide_thumbnail_id")
    slide_thumbnail.src = "/main/"+js_active_slide+"/0";

    // set slide_patch thumbnail img
    for (var row = 0; row < slide_patch_rows ; row++)
    {
        for(var col = 1; col <= slide_patch_cols; col++)
        {
            var slide_patch = document.getElementById("slide_patch_" + (row * slide_patch_cols + col))
            slide_patch.src = "/main/"+js_active_slide+"/" + (row * slide_patch_cols + col);

            var patch_score = document.getElementById("slide_score_" + (row * slide_patch_cols + col));
            console.log(scores[row * slide_patch_cols + col - 1])
            patch_score.textContent = scores[row * slide_patch_cols + col - 1];
        }
    }

    // show start button by cal_flag
    var start_btn = document.getElementById("start_btn_id");
    start_btn.onclick = function (val){ return function(){start(val);}}(js_active_slide);

    console.log(cal_flag);
    if (cal_flag != 0) {
        start_btn.style.visibility = "hidden";
    }
    else {
        start_btn.style.visibility = "visible";
    }
}