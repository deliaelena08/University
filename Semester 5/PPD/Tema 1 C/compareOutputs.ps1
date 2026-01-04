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

$content1 = Get-Content $file1 | Select-Object -Skip 1
$content2 = Get-Content $file2 | Select-Object -Skip 1

if ($content1 -join "" -eq $content2 -join "") {
    Write-Host "Rezultatele sunt identice."
} else {
    Write-Host "Rezultatele difera."
    Write-Host "`nPrimele diferente:"
    Compare-Object $content1 $content2 | Select-Object -First 10 | Format-Table -AutoSize
}
