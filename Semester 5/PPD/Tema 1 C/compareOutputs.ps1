# compareOutputs.ps1
# Usage:
#   powershell -ExecutionPolicy Bypass -File .\compareOutputs.ps1 output_seq.txt output_par.txt

param (
    [string]$file1,
    [string]$file2
)

if (!(Test-Path $file1)) {
    Write-Host "Fisierul $file1 nu exista."
    exit 1
}

if (!(Test-Path $file2)) {
    Write-Host "Fisierul $file2 nu exista."
    exit 1
}

# Citeste continutul, ignorand primul rand (linia cu titlul)
$content1 = Get-Content $file1 | Select-Object -Skip 1
$content2 = Get-Content $file2 | Select-Object -Skip 1

# Compara fisierele
if ($content1 -join "" -eq $content2 -join "") {
    Write-Host "Rezultatele sunt identice."
} else {
    Write-Host "Rezultatele difera."

    # Optional: afiseaza primele diferente (max 10)
    Write-Host "`nPrimele diferente:"
    Compare-Object $content1 $content2 | Select-Object -First 10 | Format-Table -AutoSize
}
