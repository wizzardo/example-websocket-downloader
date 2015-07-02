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
        var params = {type: $('#types').get('value')};
        $.request('post', '${createLink(action: 'form')}', params)
                .then(function success(result) {
                    $('#inputs').set("innerHTML", result);
                });
    }
</script>
</body>
</html>