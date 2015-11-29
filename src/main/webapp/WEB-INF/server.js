var server = require('webserver').create();
var system = require('system');
var host, port;

console.log("Enter");
if (system.args.length !== 2) {
    console.log('Usage: server.js <some port>');
    phantom.exit(1);
} else {
    port = system.args[1];
    var listening = server.listen(port, function (request, response) {
        try {
            console.log("GOT HTTP REQUEST");
            console.log(request.post);
            var reqBody = JSON.parse(request.post);
            console.log(reqBody.url);
            // stop request
            if (reqBody.stop === true)
                phantom.exit();
            DoOneJob(reqBody.url, reqBody.output, function () {
                response.write("0");
                response.close();
            },
            function(){
                response.write("1");
                response.close();
            });
        }catch(e){
            response.write("1");
            response.close();
        }
    });
    if (!listening) {
        console.log("could not create web server listening on port " + port);
        phantom.exit(1);
    }
    else {
        console.log("successfully listen on port" + port);
    }
}

function DoOneJob(url, output, callback, onerr) {
    var page = require('webpage').create();
    // set page size to kindle size.
    page.viewportSize = {width: 340, height: 480};
    page.paperSize = {width: 680, height: 960, margin: '0px'};

    // stop the navigation of IFrame pages.
    page.onNavigationRequested = function (url, type, will, main) {
        if (main == false)
            page.navigationLocked = true;
    };

    page.clearMemoryCache();

    page.open(url, function (status) {
        if (status !== 'success') {
            console.log('Unable to load the address!');
            console.log(page.reason);
            console.log(page.reason_url);
            page.close();
            onerr();
        } else {

            // load page success, start to inject readability js
            // remove all iframe content since we don't need them
            page.evaluate(function () {
                var iframes = document.getElementsByTagName('iframe');
                for (var i = 0; i < iframes.length; i++) {
                    iframes[i].innerHTML = "";
                }
            });
            window.setTimeout(function () {
                page.evaluate(function () {
                    // if it is in iFrame, don't inject the js.
                    if (self != top) return;
                    // Inject the readablity.js to get a clear content of the page.
                    readStyle = 'style-newspaper';
                    readSize = 'size-large';
                    readMargin = 'margin-medium';
                    _readability_script = document.createElement('SCRIPT');
                    _readability_script.type = 'text/javascript';
                    _readability_script.src = 'http://aiquestion.info/readability/js/readability.js?x=' + (Math.random());
                    document.getElementsByTagName('head')[0].appendChild(_readability_script);
                    _readability_css = document.createElement('LINK');
                    _readability_css.rel = 'stylesheet';
                    _readability_css.href = 'http://aiquestion.info/readability/css/readability.css';
                    _readability_css.type = 'text/css';
                    _readability_css.media = 'screen';
                    document.getElementsByTagName('head')[0].appendChild(_readability_css);
                    _readability_print_css = document.createElement('LINK');
                    _readability_print_css.rel = 'stylesheet';
                    _readability_print_css.href = 'http://aiquestion.info/readability/css/readability-print.css';
                    _readability_print_css.media = 'print';
                    _readability_print_css.type = 'text/css';
                    document.getElementsByTagName('head')[0].appendChild(_readability_print_css);
                });
                window.setTimeout(function () {
                    try {
                        page.render(output, {format: 'pdf', quality: '100'});
                        page.close();
                        callback();
                    }catch(e){
                        onerr();
                    }
                }, 10000); // TODO i didn't find a good way to know when the inject js finished, so just wait some time before render to pdf
            }, 200);
        }
    });
}
