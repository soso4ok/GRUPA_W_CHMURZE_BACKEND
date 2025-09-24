targetScope = 'subscription'

param projectName string = 'grupawchmurzebackend'
param location string = 'centralus'

param adminUsername string = 'azureuser'
@secure()
param sshPublicKey string
param vmSize string = 'Standard_D2s_v3'
param kubernetesVersion string = '1.32.5'

var resourceGroupName = '${projectName}-rg'
var acrName = '${projectName}acrregestry'
var aksName = '${projectName}-aks'

resource resourceGroup 'Microsoft.Resources/resourceGroups@2024-03-01' = {
  name: resourceGroupName
  location: location
}

module mainModule 'main.resources.bicep' = {
  name: 'mainResourcesDeployment'
  scope: resourceGroup
  params: {
    location: location
    acrName: acrName
    aksName: aksName
    adminUsername: adminUsername
    sshPublicKey: sshPublicKey
    vmSize: vmSize
    kubernetesVersion: kubernetesVersion
  }
}

output AKS_NAME string = mainModule.outputs.AKS_NAME
output ACR_NAME string = mainModule.outputs.ACR_NAME
output RESOURCE_GROUP_NAME string = resourceGroupName