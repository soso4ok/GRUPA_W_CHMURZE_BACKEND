// Збережіть це як main.bicep

param location string
param acrName string
param aksName string


param vmSize string = 'Standard_D2s_v3'
param kubernetesVersion string = '1.31.0'

resource containerRegistry 'Microsoft.ContainerRegistry/registries@2023-07-01' = {
  name: acrName
  location: location
  sku: {
    name: 'Basic'
  }
  properties: {
    adminUserEnabled: false
  }
}

resource aksCluster 'Microsoft.ContainerService/managedClusters@2024-05-01' = {
  name: aksName
  location: location
  identity: {
    type: 'SystemAssigned'
  }
  properties: {
    kubernetesVersion: kubernetesVersion
    dnsPrefix: aksName
    enableRBAC: true
    agentPoolProfiles: [
      {
        name: 'agentpool'
        count: 1
        vmSize: vmSize
        osType: 'Linux'
        mode: 'System'
      }
    ]
    networkProfile: {
      outboundType: 'loadBalancer'
      loadBalancerSku: 'standard'
    }
  }
}


resource acrPullRoleDef 'Microsoft.Authorization/roleDefinitions@2022-04-01' existing = {
  scope: subscription()
  name: '7f951dda-4ed3-4680-a7ca-43fe172d538d'
}

var kubeletObjectId = aksCluster.properties.identityProfile.kubeletidentity.objectId
var roleAssignmentName = guid(containerRegistry.id, aksCluster.id, acrPullRoleDef.id)

resource acrPullRole 'Microsoft.Authorization/roleAssignments@2022-04-01' = {
  name: roleAssignmentName
  scope: containerRegistry
  properties: {
    roleDefinitionId: acrPullRoleDef.id
    principalId: kubeletObjectId
    principalType: 'ServicePrincipal'
    description: 'Allow AKS kubelet to pull images from ACR'
  }
}

output AKS_NAME string = aksCluster.name
output ACR_NAME string = containerRegistry.name