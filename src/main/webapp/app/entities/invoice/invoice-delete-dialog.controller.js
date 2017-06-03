(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoiceDeleteController',InvoiceDeleteController);

    InvoiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Invoice'];

    function InvoiceDeleteController($uibModalInstance, entity, Invoice) {
        var vm = this;

        vm.invoice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Invoice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
