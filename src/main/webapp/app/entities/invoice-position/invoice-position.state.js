(function() {
    'use strict';

    angular
        .module('appApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invoice-position', {
            parent: 'entity',
            url: '/invoice-position',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InvoicePositions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-position/invoice-positions.html',
                    controller: 'InvoicePositionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('invoice-position-detail', {
            parent: 'invoice-position',
            url: '/invoice-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'InvoicePosition'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invoice-position/invoice-position-detail.html',
                    controller: 'InvoicePositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'InvoicePosition', function($stateParams, InvoicePosition) {
                    return InvoicePosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'invoice-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('invoice-position-detail.edit', {
            parent: 'invoice-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-position/invoice-position-dialog.html',
                    controller: 'InvoicePositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoicePosition', function(InvoicePosition) {
                            return InvoicePosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-position.new', {
            parent: 'invoice-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-position/invoice-position-dialog.html',
                    controller: 'InvoicePositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                value: null,
                                tax: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invoice-position', null, { reload: 'invoice-position' });
                }, function() {
                    $state.go('invoice-position');
                });
            }]
        })
        .state('invoice-position.edit', {
            parent: 'invoice-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-position/invoice-position-dialog.html',
                    controller: 'InvoicePositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvoicePosition', function(InvoicePosition) {
                            return InvoicePosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-position', null, { reload: 'invoice-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invoice-position.delete', {
            parent: 'invoice-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invoice-position/invoice-position-delete-dialog.html',
                    controller: 'InvoicePositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InvoicePosition', function(InvoicePosition) {
                            return InvoicePosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invoice-position', null, { reload: 'invoice-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
