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
        <div class="value" style="width: {progress > 0 ? progress : 0}%"></div>
    </div>

    <textarea name="log_{id}" class="log" readonly="true" style="display: none">{log}</textarea>

    <span class="cancel" style="{status != 'IN_PROGRESS' ? 'display: none' : ''}" onclick="cancel({id})">cancel</span>


    <style scoped>
        :scope { display: block }
    </style>

    <script>
        var that = this;

        this.on('mount', function () {
//            console.log('listen progress_update_' + that.id);
            that.obs.on('progress_update_' + that.id, function (progress, callback) {
//                console.log('progress_update_' + that.id)
                if(that.progress != progress) {
                    var from = that.progress || 0;
                    var to = progress;
                    console.log('animate from ' + from + ' to ' + to);
                    lib.onAnimationFrame(function (i) {
                        that.progress = from + (to - from) * i;
                        that.update()
                    }, 100, callback);
                }
            });
        })

    </script>
</job>
