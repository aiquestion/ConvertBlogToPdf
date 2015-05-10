var page = require('webpage').create(),
    system = require('system'),
    address, output, size;

if (system.args.length < 3 || system.args.length > 5) {
    console.log('Usage: rasterize.js URL filename [paperwidth*paperheight|paperformat] [zoom]');
    console.log('  paper (pdf output) examples: "5in*7.5in", "10cm*20cm", "A4", "Letter"');
    phantom.exit(1);
} else {
    address = system.args[1];
    output = system.args[2];
    page.viewportSize = {width: 340, height: 480};
    if (system.args.length > 3 && system.args[2].substr(-4) === ".pdf") {
        size = system.args[3].split('*');
        page.paperSize = size.length === 2 ? {width: size[0], height: size[1], margin: '0px'}
            : {format: system.args[3], orientation: 'portrait', margin: '1cm'};
    }
    if (system.args.length > 4) {
        page.zoomFactor = system.args[4];
    }
    page.onNavigationRequested = function (url, type, will, main) {
        // stop the navigation of IFrame pages.
        if (main == false)
            page.navigationLocked = true;
    }

    page.clearMemoryCache();

    page.open(address, function (status) {
        if (status !== 'success') {
            console.log('Unable to load the address!');
            console.log(page.reason);
            console.log(page.reason_url);
            phantom.exit();
        } else {
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
                    page.render(output, {format: 'pdf', quality: '100'});
                    phantom.exit();
                }, 10000); // TODO i didn't find a good way to know when the inject js finished, so just wait some time before render to pdf
            }, 200);
        }
    });
}
