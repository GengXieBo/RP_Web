function displayProgress() {
    var eventSource = new EventSource("http://localhost:8080/progress");

    eventSource.addEventListener("message", function(event){
            console.log(event.data);
            document.getElementById("foo").innerHTML = event.data;
    })

    eventSource.addEventListener("complete", function(event){
        console.log(event.data);
        document.getElementById("foo").innerHTML = event.data;
        eventSource.close();
    })

    eventSource.addEventListener("busy", function(event){
        console.log(event.data);
        document.getElementById("foo").innerHTML = event.data;
        console.log("Event Source closed");
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

function getImage()
{
    fetch("http://localhost:8080/image")
        .then((resp) => resp.arrayBuffer())
        .then((images) => {
            //var my_image = document.createElement("img");
            var img = document.createElement("img")
            var base64Flag = 'data:image/jpeg;base64,';
            var imageStr = arrayBufferToBase64(images);
            img.src = base64Flag + imageStr;
            document.getElementById("demo").appendChild(img);
        });
}

function arrayBufferToBase64(buffer) {
    var binary = '';
    var bytes = [].slice.call(new Uint8Array(buffer));

    bytes.forEach((b) => binary += String.fromCharCode(b));

    return window.btoa(binary);
};



