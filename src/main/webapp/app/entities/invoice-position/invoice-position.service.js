(function() {
    'use strict';
    angular
        .module('appApp')
        .factory('InvoicePosition', InvoicePosition);

    InvoicePosition.$inject = ['$resource'];

    function InvoicePosition ($resource) {
        var resourceUrl =  'api/invoice-positions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
