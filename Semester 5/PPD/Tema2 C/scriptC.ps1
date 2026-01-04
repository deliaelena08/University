
$param1 = $args[0]
$param2 = $args[1]
$param3 = $args[2]

$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = (cmd /c .\$param1 $param2 2`>`&1)
    $valoare = [double]($a | Select-Object -Last 1)
    Write-Host $valoare
    $suma += $valoare	
    Write-Host ""
}
$media = $suma / $i
Write-Host "Timp de executie mediu:" $media

if (!(Test-Path outC.csv)){
    New-Item outC.csv -ItemType File
    Set-Content outC.csv 'Tip Matrice,Tip alocare,Nr threads,Timp executie'
}

Add-Content outC.csv ",,$($args[1]),$($media)"