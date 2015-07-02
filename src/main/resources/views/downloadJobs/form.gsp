<div>
    <g:hiddenField name="type" value="${job.class.simpleName}"/>
    <g:each in="${((Map) requiredParams).entrySet()}">
        <div>
            <span class="key">${it.key}</span>
            <g:textField name="${it.key}" placeholder="${String.valueOf(it.getValue()).replace("\"", "")}"/>
        </div>
    </g:each>
</div>