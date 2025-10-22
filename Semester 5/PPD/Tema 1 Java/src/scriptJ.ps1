
$param1 = $args[0] 
$param2 = $args[1] 
$param3 = $args[2] 

$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = java -cp . $args[0] $args[1]
    $valoare = [double]($a | Select-Object -Last 1)
    Write-Host $valoare
    $suma += $valoare
    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    #Scrie date in csv
    Set-Content outJ.csv 'Tip Matrice,Nr threads,Timp executie'
}

Add-Content outJ.csv ",$($args[1]),$($media)"