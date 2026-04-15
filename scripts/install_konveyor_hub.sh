#!/usr/bin/env bash
OLM_VERSION="v0.42.0"

# release channel - either stable or alpha
KONVEYOR_VERSION="stable"

# install operator lifecycle manager for operator installation
curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/$OLM_VERSION/install.sh | bash -s $OLM_VERSION

# install konveyor operator
kubectl apply -f ./konveyor-hub/konveyor-operator-$KONVEYOR_VERSION.yaml

echo "Waiting 60 secs for Konveyor operator to be ready..."
sleep 60

# wait for readyness of Konveyor operator
kubectl wait pods -n konveyor-tackle -l name=tackle-operator --for condition=Ready --timeout=180s

# wait for readyness of Konveyor CRDs
kubectl -n konveyor-tackle wait --for condition=established --timeout=300s crd tackles.tackle.konveyor.io

# Apply Tackle instance configuration
kubectl apply -f ./konveyor-hub/tackle-instance.yaml
