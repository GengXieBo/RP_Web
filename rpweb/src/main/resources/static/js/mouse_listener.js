const CW = 800, CH = 800;
var pre_sx = 0, pre_sy = 0;
var mouse_offsetX = 0, mouse_offsetY = 0;

var mouse_down_flag = false;

var canvas = document.getElementById("canvas")
var ctx = canvas.getContext("2d");

// default get slide_thumbnail img
var canvas_img;
set_canvas_img();

var scale_ratio = 1;

function MouseWheelHandler(e) {
    console.log(e)
    var e = window.event
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta)));
    var pre_scale_ratio = scale_ratio;
    scale_ratio = Math.max(1, scale_ratio + 0.15 * delta);
    var nw = canvas_img.naturalWidth, nh = canvas_img.naturalHeight;
    console.log(scale_ratio, canvas_img.naturalWidth, canvas_img.naturalHeight);

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    var sx = 0, sy = 0, sw = nw, sh = nh;
    if (scale_ratio > 1) {
        sw = nw / scale_ratio, sh = nh / scale_ratio;
        cur_offsetX = mouse_offsetX * (nw / (CW * pre_scale_ratio)) + pre_sx;
        cur_offsetY = mouse_offsetY * (nh / (CH * pre_scale_ratio)) + pre_sy;
        sx = cur_offsetX - mouse_offsetX * (nw / (CW * scale_ratio))
        sy = cur_offsetY - mouse_offsetY * (nh / (CH * scale_ratio))
    }

    console.log(sx, sy, sw, sh)
    ctx.drawImage(canvas_img, sx, sy, sw, sh, 0, 0, CW, CH);

    pre_sx = sx;
    pre_sy = sy;
}

function MouseMoveHandler(e) {
    // console.log(e)
    var move_speed = 1.5;
    if(mouse_down_flag) {
        var nw = canvas_img.naturalWidth, nh = canvas_img.naturalHeight;
        var move_x = (mouse_offsetX - e.offsetX) * move_speed;
        var move_y = (mouse_offsetY - e.offsetY) * move_speed;
        pre_sx = Math.min(nw - nw / scale_ratio, Math.max(0, pre_sx + move_x));
        pre_sy = Math.min(nh - nh / scale_ratio, Math.max(0, pre_sy + move_y));
        ctx.drawImage(canvas_img, pre_sx, pre_sy, nw / scale_ratio, nh / scale_ratio, 0, 0, CW, CH);
    }
    mouse_offsetX = e.offsetX;
    mouse_offsetY = e.offsetY;
}

function mouseOver() {
    document.documentElement.style.overflow='hidden';
}

function mouseOut() {
    document.documentElement.style.overflow='auto';
}

function mousedown(e) {
    console.log("down");
    mouse_down_flag = true;
}

function mouseup(e) {
    console.log("up");
    mouse_down_flag = false;
}

function slide_patch_click(active_index, patch_index) {
    console.log("slide patch clicked....");
    slide_patch_id = "slide_patch_" + patch_index;
    set_canvas_img(slide_patch_id);
}

function set_canvas_img(img_id = "slide_thumbnail_id") {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    pre_sx = 0;
    pre_sy = 0;
    scale_ratio = 1;

    img = document.getElementById(img_id);
    console.log(img.complete);
    if (img.complete) {
        ctx.drawImage(img, 0, 0, CW, CH);
    } else {
        img.onload = function () {
            console.log(img_id, "img loaded ...")
            ctx.drawImage(img, 0, 0, CW, CH);
            if (canvas.addEventListener) {
                console.log(img_id, "img loaded ...")
                canvas.addEventListener("mousewheel", MouseWheelHandler, false);
                canvas.addEventListener('mousemove', MouseMoveHandler, false);

                canvas.addEventListener('DOMMouseScroll', MouseWheelHandler, false);

            }
        }
    }
    canvas_img = img;
    console.log("set canvas img success", img_id, canvas_img.naturalWidth, canvas_img.naturalHeight);
}