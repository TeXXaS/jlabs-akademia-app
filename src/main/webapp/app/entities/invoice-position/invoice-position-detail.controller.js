(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoicePositionDetailController', InvoicePositionDetailController);

    InvoicePositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'InvoicePosition', 'Invoice'];

    function InvoicePositionDetailController($scope, $rootScope, $stateParams, previousState, entity, InvoicePosition, Invoice) {
        var vm = this;

        vm.invoicePosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appApp:invoicePositionUpdate', function(event, result) {
            vm.invoicePosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
