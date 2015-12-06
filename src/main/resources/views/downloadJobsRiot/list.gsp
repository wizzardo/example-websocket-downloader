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
    var jobs = [];
    function mount() {
        jobsTag = riot.mount('jobs', {jobs: jobs}, {}, function (tag) {
            jobsTag = tag[0];
        });
        if (jobsTag)
            jobsTag = jobsTag[0];
    }

    handlers.jobs = function (data) {
        obs = riot.observable();
        for (var i = 0; i < data.jobs.length; i++)
            data.jobs[i].obs = obs

        jobs = data.jobs;
        mount()
    };

    handlers.createJob = function (data) {
        jobs.splice(0, 0, data.job);
        data.job.obs = obs;

        mount()
    };

    handlers.updateProgress = function (data) {
        var job = find(jobs, data.id);
        obs.trigger('progress_update_' + data.id, data.progress, function () {
            job.progress = data.progress
        })
    };

    handlers.updateLog = function (data) {
        var job = find(jobs, data.id);
        job.log = job.log + data.log;
        jobsTag.update();
    };

    handlers.updateStatus = function (data) {
        var job = find(jobs, data.id);
        job.status = data.status;
        jobsTag.update();
    };

    function find(jobs, id) {
        for (var i = 0; i < jobs.length; i++) {
            if (jobs[i].id == id)
                return jobs[i];
        }
    }

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
        var params = {command: 'createJob'};
        lib('#inputs input').each(function (el) {
            params[el.name] = el.value
        });
        ws.send(JSON.stringify(params));
        lightbox.close();
    }
</script>
</body>
</html>
