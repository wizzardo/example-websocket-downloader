
var handlers = {}
handlers.updateProgress = function (data) {
    if (data.progress < 100) {
        $('#job_' + data.id + ' .progress .value').animate({width: data.progress + '%'}, 100);
    } else {
        $('#job_' + data.id + ' .progress').hide();
        $('#job_' + data.id + ' .download').show();
    }
}

handlers.updateLog = function (data) {
    var textarea = $('#job_' + data.id + ' textarea')[0]
    textarea.value = textarea.value + data.log
    scrollToBottom(textarea)
}

handlers.updateStatus = function (data) {
    $('#job_' + data.id).removeClass().addClass('job').addClass(data.cssClass);
    $('#job_' + data.id + ' span.status').html(data.status);
    if (data.status != 'IN_PROGRESS')
        $('#job_' + data.id + ' span.cancel').hide();
}

function toggleLog(id) {
    $('#job_' + id + ' textarea').slideToggle(200)
    scrollToBottom(('#job_' + id + ' textarea')[0])
}

function scrollToBottom(textarea) {
    textarea.scrollTop = textarea.scrollHeight - textarea.clientHeight;
}

// Write your code in the same way as for native WebSocket:
var ws = new WebSocket("ws://"+location.hostname+":"+location.port+"/ws");
ws.onopen = function () {
    console.log("open");
//            ws.send("Hello");  // Sends a message.
};
ws.onmessage = function (e) {
    // Receives a message.
    console.log(e.data);
    var data = JSON.parse(e.data)

    handlers[data.command](data)
};
ws.onclose = function () {
    console.log("closed");
};

function cancel(id) {
    ws.send('{"command":"cancel", "id":' + id + '}');
}

