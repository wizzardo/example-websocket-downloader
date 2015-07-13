<html>
<head>
    <title>List of recent downloads</title>
    <meta name="layout" content="layout"/>
    <g:resource dir="js" file="web_socket.js"/>
    <g:resource dir="js" file="app.js"/>
    <g:resource dir="js" file="lib.js"/>
</head>

<body>
<a href="javascript:void(0)" onclick="lightbox.open()">create new Job</a>
<br>

<jobs>!</jobs>

<lightbox>
    <select id="types" onchange="onSelectJobType()">
        <option value=" -- ">--</option>
        <g:each in="${jobTypes}">
            <option value="$it">$it</option>
        </g:each>
    </select>

    <form method="post" onsubmit="createJob(); return false;">
        <div id="inputs"></div>
        <input type="button" value="create" onclick="createJob()">
    </form>
</lightbox>

<g:resource dir="js" file="riot_compiler.js"/>
<script src="/static/js/tags/jobs.tag" type="riot/tag"></script>
<script src="/static/js/tags/job.tag" type="riot/tag"></script>
<script src="/static/js/tags/lightbox.tag" type="riot/tag"></script>

<script>
    handlers.jobs = function (data) {
        t = riot.mount('jobs', data);
    };

    var lightbox = riot.mount('lightbox', {}, {}, function (tag) {
        lightbox = tag[0];
    });

    function onSelectJobType() {
        var params = {type: lib('#types')[0].value};
        lib.ajax({
            url: '${createLink(controller: 'downloadJobs',action: 'form')}',
            type: 'POST',
            data: params,
            success: function (result) {
                lib('#inputs')[0].innerHTML = result;
            }
        })
    }

    function createJob() {
        var params = {};
        lib('#inputs input').each(function (el) {
            params[el.name] = el.value
        });
        lib.ajax({
            url: '${createLink(controller: 'downloadJobs',action: 'save')}',
            type: 'POST',
            data: params,
            success: function (result) {
                lightbox.close();
                ws.send('{"command":"jobs"}');
            }
        });
    }
</script>
</body>
</html>
