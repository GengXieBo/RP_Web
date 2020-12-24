
ifHidderStartButton();

function ifHidderStartButton() {
    var start_btn = document.getElementById("start_btn_id");
    console.log('ifhidden', js_active_slide, slide_flags[js_active_slide]);
    if (slide_flags[js_active_slide] == '0') {
        start_btn.style.visibility = "visible";
    }
    else {
        start_btn.style.visibility = "hidden";
    }
}

function start(index) {
    cur_cal_slide_id = js_active_slide;

    slide_flags[js_active_slide] = '1';
    ifHidderStartButton();
    showCalInfo();
    var foo = document.getElementById("foo");
    foo.innerHTML = "init...";
    var eventSource = new EventSource("/main/compute/"+index);

    eventSource.addEventListener("message", function(event){
            console.log(event.data);
            if (cur_cal_slide_id == js_active_slide) {
                showCalInfo();   
            }
            else {
                hidderCalInfo();
            }
            foo.innerHTML = Math.round(event.data * 10000) / 100 + "%";
            var cal_progress = document.getElementById("cal_progress");

            cal_progress.style.removeProperty("width")
            cal_progress.style.setProperty("width", Math.round(parseFloat(event.data) * 100) + "%");
    })

    eventSource.addEventListener("complete", function(event){
        console.log(event.data);
        document.getElementById("foo").innerHTML = event.data;
        cur_cal_slide = -1;
        hidderCalInfo();
        alert("cal completed...");

        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/main/cal_completed/' + js_active_slide, true);
        xhr.send();


        /**
         * 获取数据后的处理程序
         */
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {

                // var res = xhr.getAllResponseHeaders();
                // console.log(res);
                var slide_score = xhr.getResponseHeader("slide_score");
                var patch_scores_be = xhr.getResponseHeader("patch_scores");
                console.log(slide_score);
                console.log(patch_scores_be);

                var slide_thumbnail = document.getElementById("slide_thumbnail_id")
                slide_thumbnail.src = "/main/"+js_active_slide+"/0";

                slide_scores[js_active_slide] = parseFloat(slide_score);
                var slide_score_h3 = document.getElementById("slide_score_id");
                slide_score_h3.textContent = slide_score;

                // var patch_scores = res['patch_scores'];
                patch_scores = patch_scores_be.split(' ');

                // set slide_patch thumbnail img
                for (var row = 0; row < slide_patch_rows ; row++)
                {
                    for(var col = 1; col <= slide_patch_cols; col++)
                    {
                        var slide_patch = document.getElementById("slide_patch_" + (row * slide_patch_cols + col))
                        slide_patch.src = "/main/"+js_active_slide+"/" + (row * slide_patch_cols + col);

                        var score = patch_scores[row * slide_patch_cols + col - 1];
                        console.log(score);
                        var patch_score = document.getElementById("slide_score_" + (row * slide_patch_cols + col))
                        patch_score.textContent = score;

                        slide_patch_socres[js_active_slide][row * slide_patch_cols + col - 1] = parseFloat(score);
                    }
                }

            //    update cal flag
                var cal_flag = xhr.getResponseHeader("flag");
                slide_flags[js_active_slide] = cal_flag;
                console.log('cal completed ', slide_flags[js_active_slide], cal_flag);
                ifHidderStartButton();
            }
        };

        eventSource.close();
    })

    eventSource.addEventListener("busy", function(event){
        console.log(event.data);
        document.getElementById("foo").innerHTML = event.data;
        console.log("Event Source closed");
        alert("Gpu is busy!!!");
        // cur_cal_slide_id = -1;
        // if (cur_cal_slide_id == js_active_slide) {
        //
        // }
        // hidderCalInfo();
        eventSource.close();
    });

    eventSource.onerror = function(event){
            if(event.eventPhase == EventSource.CLOSED){
            eventSource.close();
            document.getElementById("foo").innerHTML = 1;
            console.log("Event Source Closed");
        }
    }
}

function showCalInfo() {
    var cal_progress = document.getElementById("cal_info");
    cal_progress.style.display = "block";
}

function hidderCalInfo() {
    var cal_progress = document.getElementById("cal_info");
    cal_progress.style.display = "none";
}



