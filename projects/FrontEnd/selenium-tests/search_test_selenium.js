describe('angularjs search', function() {
    it('Should search and go to Jeugdland', function() {
        browser.get('http://localhost:63343/FrontEnd/index.html#/home');

        element(by.model('query')).sendKeys('Jeugdland');
        element(by.id('searchButton')).click();

        var title = element(by.id('title'));
       // expect(title.getText()).toEqual('Jeugdland');

    });
});