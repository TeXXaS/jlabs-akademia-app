(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoiceDetailController', InvoiceDetailController);

    InvoiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Invoice'];

    function InvoiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Invoice) {
        var vm = this;

        vm.invoice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:invoiceUpdate', function(event, result) {
            vm.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
