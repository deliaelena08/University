param (
    [string]$file1,
    [string]$file2
)

function Read-Matrix($file) {
    if (!(Test-Path $file)) {
        Write-Host "Fisierul $file nu exista."
        exit 1
    }

    $lines = Get-Content $file | ForEach-Object { $_.Trim() } | Where-Object { $_ -ne "" }
    $matrix = @()
    foreach ($line in $lines) {
        $row = $line -split "\s+" | ForEach-Object { [int]$_ }
        $matrix += ,$row
    }
    return $matrix
}

$matrix1 = Read-Matrix $file1
$matrix2 = Read-Matrix $file2

# Verificam dimensiunile
if ($matrix1.Count -ne $matrix2.Count -or $matrix1[0].Count -ne $matrix2[0].Count) {
    Write-Host "Matricile au dimensiuni diferite."
    exit 1
}

$differences = @()
for ($i = 0; $i -lt $matrix1.Count; $i++) {
    for ($j = 0; $j -lt $matrix1[$i].Count; $j++) {
        if ($matrix1[$i][$j] -ne $matrix2[$i][$j]) {
            $differences += [PSCustomObject]@{
                Row = $i
                Column = $j
                File1 = $matrix1[$i][$j]
                File2 = $matrix2[$i][$j]
            }
        }
    }
}

if ($differences.Count -eq 0) {
    Write-Host "Matricile sunt identice."
} else {
    Write-Host "Matricile difera. Primele diferente:"
    $differences | Select-Object -First 10 | Format-Table -AutoSize
}
