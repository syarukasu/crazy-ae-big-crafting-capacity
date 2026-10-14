$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$libs = Join-Path $root 'libs'
New-Item -ItemType Directory -Force -Path $libs | Out-Null
$dependencies = @(
    @{
        Name = 'appliedenergistics2-forge-15.4.10.jar'
        Url = 'https://github.com/AppliedEnergistics/Applied-Energistics-2/releases/download/forge/v15.4.10/appliedenergistics2-forge-15.4.10.jar'
        Sha256 = 'FBFEE05C6674CB6B00FE425E945CA4818D41EB34069B60DCB4A36221A0390FCC'
    },
    @{
        Name = 'CrazyAE-1.20.1-v1.0.jar'
        Url = 'https://mediafilez.forgecdn.net/files/9156/669/CrazyAE-1.20.1-v1.0.jar'
        Sha256 = '7933796D56C9C2A6C459E99C1603FB3EDB7F039CEBB53B34DFDD97A5CAAB4E10'
    },
    @{
        Name = 'aco-2.0.0.jar'
        Url = 'https://mediafilez.forgecdn.net/files/9077/511/aco2.0.0-rc.7-vm-refs.8-local_1.20.1.jar'
        Sha256 = 'DCD10CB901E10C25BB412EC9CFDEE454925179C8005B16C42A92BFB1DD0F36E5'
    }
)
foreach ($dependency in $dependencies) {
    $path = Join-Path $libs $dependency.Name
    if (!(Test-Path -LiteralPath $path)) {
        Invoke-WebRequest -Uri $dependency.Url -OutFile $path
    }
    if ((Get-FileHash -LiteralPath $path -Algorithm SHA256).Hash -ne $dependency.Sha256) {
        throw "Dependency checksum mismatch: $($dependency.Name)"
    }
    Write-Host "Verified $($dependency.Name)"
}
