<html>
<head>
    <title>Create download job</title>
    <meta name="layout" content="layout"/>
</head>

<body>

<select id="types" onchange="onSelectJobType()">
    <option value=" -- ">--</option>
    <g:each in="${jobTypes}">
        <option value="$it">$it</option>
    </g:each>
</select>

<form method="post" action="${createLink(action: 'save')}">
    <div id="inputs"></div>
    <input type="submit" value="create">
</form>

<script>
    function onSelectJobType() {
        var params = {type: lib('#types')[0].value};
        lib.ajax({
            url: '${createLink(action: 'form')}',
            type: 'POST',
            data: params,
            success: function (result) {
                lib('#inputs')[0].innerHTML = result;
            }
        })
    }
</script>
</body>
</html>