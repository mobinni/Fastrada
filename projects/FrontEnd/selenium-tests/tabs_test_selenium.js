describe('angularjs tabs homepage', function() {
    it('Should click and check all the tabs', function() {
        browser.get('http://localhost:63343/FrontEnd/index.html#/home');

        element(by.id('speed')).click();
        element(by.id('rpm')).click();
        element(by.id('temperature')).click();
    });
});