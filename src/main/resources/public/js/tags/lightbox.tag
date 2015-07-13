<lightbox>

    <div style="{!opts.visible ? 'display: none' : ''}" class="white_content">
        <yield/>
        <a href="javascript:void(0)" onclick={close} class="lightbox_close">
            ✕
        </a>
    </div>
    <div style="{!opts.visible ? 'display: none' : ''}" class="black_overlay" onclick={close}></div>

    <style>
        .black_overlay {
            position: fixed;
            top: 0%;
            left: 0%;
            width: 100%;
            height: 100%;
            background-color: black;
            z-index: 1001;
            -moz-opacity: 0.8;
            opacity: .80;
            filter: alpha(opacity=80);
        }

        .white_content {
            position: fixed;
            top: 25%;
            left: 25%;
            width: 50%;
            height: 50%;
            padding: 16px;
            background-color: white;
            z-index: 1002;
        }

        .lightbox_close {
            position: absolute;
            width: 14px;
            text-align: center;
            top: 0px;
            right: 0px;
            color: gray;
            padding: 6px;
            border-radius: 20px;
            padding-left: 8px;
            padding-right: 8px;
            font-size: 16px;
            text-decoration: none;
            z-index: 1003;
            margin-top: -10px;
            margin-right: -10px;
            background-color: whitesmoke;
            box-shadow: 0px 1px 5px 0px rgba(0, 0, 0, 0.75);

            -webkit-touch-callout: none;
            -webkit-user-select: none;
            -khtml-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .lightbox_close:active {
            box-shadow: 0px 0px 2px 0px rgba(0, 0, 0, 0.75) inset;
        }
    </style>

    <script>
        var that = this;
        close() {
            that.opts.visible = false;
            that.update();
        }
        open() {
            that.opts.visible = true;
            that.update();
        }

        console.log('obs: '+(typeof opts.obs))
        if( typeof opts.obs == 'object') {
            opts.obs.on('open', function () {
                that.open()
            })
            opts.obs.on('close', function () {
                that.close()
            })
        }
    </script>
</lightbox>
