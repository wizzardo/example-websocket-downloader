<job class="job {status.toLowerCase()}" id="job_{id}">
    <strong>status:</strong> <span class="status">{status}</span><br/>
    <strong>type:</strong> {type}<br/>
    <strong>name:</strong> {name}<br/>
    <strong>params:</strong> {params}<br/>
    <br/>

    <div>
        <a href="{result}" style="{status != 'DONE' ? 'display: none' : ''}">Download result</a>
        <span class="toggleLog" onclick="toggleLog({id})">toggle log</span>
    </div>

    <div class="progress" style="{status == 'DONE' ? 'display: none' : ''}">
        <div class="value" style="width: {progress?progress:0}%"></div>
    </div>

    <textarea name="log_{id}" class="log" readonly="true" style="display: none">{log}</textarea>

    <span class="cancel" style="{status != 'IN_PROGRESS' ? 'display: none' : ''}" onclick="cancel({id})">cancel</span>


    <style scoped>
        :scope { display: block }
    </style>

    <script>

    </script>
</job>
