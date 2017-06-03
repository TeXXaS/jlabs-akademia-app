(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoicePositionDialogController', InvoicePositionDialogController);

    InvoicePositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InvoicePosition', 'Invoice'];

    function InvoicePositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InvoicePosition, Invoice) {
        var vm = this;

        vm.invoicePosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.invoices = Invoice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invoicePosition.id !== null) {
                InvoicePosition.update(vm.invoicePosition, onSaveSuccess, onSaveError);
            } else {
                InvoicePosition.save(vm.invoicePosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appApp:invoicePositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
