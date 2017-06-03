(function() {
    'use strict';

    angular
        .module('appApp')
        .factory('InvoicePositionSearch', InvoicePositionSearch);

    InvoicePositionSearch.$inject = ['$resource'];

    function InvoicePositionSearch($resource) {
        var resourceUrl =  'api/_search/invoice-positions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
