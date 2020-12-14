function start(index) {
    var eventSource = new EventSource("/main/compute/"+index);

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



