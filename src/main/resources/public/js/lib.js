var lib = new function () {
    var toRequestParams = function (data) {
        var result = '';
        for (var key in data) {
            if (result.length > 0)
                result += "&";
            result += encodeURIComponent(key) + "=" + encodeURIComponent(data[key]);
        }
        return result;
    };

    var ajax = function (config) {
        var request = new XMLHttpRequest();
        var url = config.url;
        if (config.prototype == 'GET' && config.data)
            url += "?" + toRequestParams(config.data);

        request.open(config.type, url, true);
        if (config.type == 'POST')
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');

        request.onload = function () {
            if (request.status >= 200 && request.status < 400) {
                if (config.success)
                    config.success(request.responseText);
            }
        };

        request.onerror = function () {
            if (config.error)
                config.error();
        };

        if (config.type == 'POST') {
            if (typeof config.data == 'string')
                request.send(config.data);
            else
                request.send(toRequestParams(config.data));
        } else
            request.send();
    };

    var animate = function (el, settings, time, onComplete) {
        var from = [];
        var types = [];
        var keys = [];
        var to = [];
        for (var key in settings) {
            var v = /^[0-9]+/.exec(el.style[key]);
            v = v ? v[0] : 0;
            from.push(parseInt(v));
            var type = v && parseInt(v) != 0 ? el.style[key].substring(v.length) : '';
            v = /^[0-9]+/.exec(settings[key])[0];
            var t = (typeof settings[key] == 'string') ? settings[key].substring(v.length) : 'px';
            if (type && type != t)
                throw 'different types: ' + type + ' and ' + t;

            if (!type)
                type = t;
            types.push(type);
            to.push(parseInt(v));
            keys.push(key);
        }

        var start = +new Date();
        var tick = function () {
            var progress = (new Date() - start) / time;
            if (progress > 1)
                progress = 1;

            for (var i = 0; i < to.length; i++) {
                el.style[keys[i]] = (from[i] + (+to[i] - from[i]) * Math.sin(Math.PI / 2 * progress)) + types[i];
            }

            if (progress < 1) {
                (window.requestAnimationFrame && requestAnimationFrame(tick)) || setTimeout(tick, 16)
            } else if (onComplete)
                onComplete();
        };

        tick();
    };

    var removeClass = function (el, className) {
        if (!className) {
            el.className = '';
            return;
        }

        if (el.classList)
            el.classList.remove(className);
        else
            el.className = el.className.replace(new RegExp('(^|\\b)' + className.split(' ').join('|') + '(\\b|$)', 'gi'), ' ');
    };

    var addClass = function (el, className) {
        if (el.classList)
            el.classList.add(className);
        else
            el.className += ' ' + className;
    };

    var select = function (selector) {
        return document.querySelectorAll(selector)
    };

    var out = select;
    out.ajax = ajax;
    out.animate = animate;
    out.removeClass = removeClass;
    out.addClass = addClass;
    return out
};
