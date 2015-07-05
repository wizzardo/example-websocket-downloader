<%@ page import="com.wizzardo.downloader.DownloadStatus; com.wizzardo.downloader.DownloadJob" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>List of recent downloads</title>
    <meta name="layout" content="layout"/>
    <g:resource dir="js" file="web_socket.js"/>
    <g:resource dir="js" file="app.js"/>
</head>

<body>
<g:link action="create">create new Job</g:link>
<br>

<g:each in="${(List<DownloadJob>) jobs}" var="job">
    <div class="job ${job.status.name().toLowerCase()}" id="job_${job.id}">
        <strong>status:</strong> <span class="status">${job.status}</span><br/>
        <strong>type:</strong> ${job.type}<br/>
        <strong>name:</strong> ${job.name}<br/>
        <strong>params:</strong> ${job.params}<br/>
        <br/>

        <div>
            <g:link class="download" style="${job.status != DownloadStatus.DONE ? 'display: none' : ''}"
                    action="result"
                    params="[id: job.id]">
                Download result
            </g:link>
            <span class="toggleLog" onclick="toggleLog(${job.id})">toggle log</span>
        </div>

        <g:if test="${job.status != DownloadStatus.DONE}">
            <div class="progress">
                <div class="value" style="width: ${job.progress}%"></div>
            </div>
        </g:if>

        <g:textArea name="log_${job.id}" class="log" readonly="true" style="display: none">${job.log()}</g:textArea>

        <g:if test="${job.status == DownloadStatus.IN_PROGRESS}">
            <span class="cancel" onclick="cancel(${job.id})">cancel</span>
        </g:if>
    </div>
</g:each>
</body>
</html>