function slide_click(slide_id, cal_flag, slide_path, scores) {
    var slide_item = document.getElementById(slide_id);
    slide_item.classList.add("active")

    var active_slide_item = document.getElementById(js_active_slide);
    active_slide_item.classList.remove("active")
    js_active_slide = slide_id;

    // set slide path
    var slide_path_item = document.getElementById("slide_path_id");
    slide_path_item.textContent = slide_path;

    var slide_score_h3 = document.getElementById("slide_score_id");
    slide_score_h3.textContent = "Score:" + slide_scores[js_active_slide];

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

            var score = slide_patch_socres[js_active_slide][row * slide_patch_cols + col - 1];
            console.log(score);
            var patch_score = document.getElementById("slide_score_"+(row*slide_patch_cols+col))
            patch_score.textContent = score;
        }
    }

    // show start button by cal_flag
    var start_btn = document.getElementById("start_btn_id");
    start_btn.onclick = function (val){ return function(){console.log("val" + val); start(val); }}(js_active_slide);
    ifHidderStartButton();
}
