(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoicePositionDeleteController',InvoicePositionDeleteController);

    InvoicePositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'InvoicePosition'];

    function InvoicePositionDeleteController($uibModalInstance, entity, InvoicePosition) {
        var vm = this;

        vm.invoicePosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InvoicePosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
