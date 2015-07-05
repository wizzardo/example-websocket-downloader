var handlers = {}
handlers.updateProgress = function (data) {
    if (data.progress < 100) {
        lib.animate(lib('#job_' + data.id + ' .progress .value')[0], {width: data.progress + '%'}, 100);
    } else {
        lib('#job_' + data.id + ' .progress')[0].style.display = 'none';
        lib('#job_' + data.id + ' .download')[0].style.display = '';
    }
};

handlers.updateLog = function (data) {
    var textarea = lib('#job_' + data.id + ' textarea')[0];
    textarea.value = textarea.value + data.log;
    scrollToBottom(textarea)
};

handlers.updateStatus = function (data) {
    var job = lib('#job_' + data.id)[0];
    lib.removeClass(job);
    lib.addClass(job, 'job');
    lib.addClass(job, data.cssClass);
    lib('#job_' + data.id + ' span.status')[0].innerHTML = data.status;
    if (data.status != 'IN_PROGRESS')
        lib('#job_' + data.id + ' span.cancel')[0].style.display = 'none';
};

function toggleLog(id) {
    var el = lib('#job_' + id + ' textarea')[0];
    if (el.style.display == 'none') {
        var to = window.getComputedStyle(el).height;
        el.style.height = '0px';
        el.style.display = '';
        lib.animate(el, {height: to}, 200);
    } else {
        var from = el.style.height;
        lib.animate(el, {height: '0px'}, 200, function () {
            el.style.display = 'none';
            el.style.height = from;
        });
    }
    //$('#job_' + id + ' textarea').slideToggle(200);
    scrollToBottom(('#job_' + id + ' textarea')[0])
}

function scrollToBottom(textarea) {
    textarea.scrollTop = textarea.scrollHeight - textarea.clientHeight;
}

// Write your code in the same way as for native WebSocket:
var ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ws");
ws.onopen = function () {
    console.log("open");
//            ws.send("Hello");  // Sends a message.
};
ws.onmessage = function (e) {
    // Receives a message.
    console.log(e.data);
    var data = JSON.parse(e.data);

    handlers[data.command](data)
};
ws.onclose = function () {
    console.log("closed");
};

function cancel(id) {
    ws.send('{"command":"cancel", "id":' + id + '}');
}