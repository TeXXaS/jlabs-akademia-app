(function() {
    'use strict';

    angular
        .module('appApp')
        .controller('InvoicePositionController', InvoicePositionController);

    InvoicePositionController.$inject = ['InvoicePosition', 'InvoicePositionSearch'];

    function InvoicePositionController(InvoicePosition, InvoicePositionSearch) {

        var vm = this;

        vm.invoicePositions = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            InvoicePosition.query(function(result) {
                vm.invoicePositions = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            InvoicePositionSearch.query({query: vm.searchQuery}, function(result) {
                vm.invoicePositions = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
