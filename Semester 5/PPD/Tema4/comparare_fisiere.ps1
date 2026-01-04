$p = $args[0]
$pr = $args[1]

if (-not $p -or -not $pr) {
    Write-Error "Trebuie sa specifici P si Pr! Ex: ./verify.ps1 4 2"
    exit
}

javac Main.java

Write-Host "1. Rulare SECVENTIAL (Generare referinta)..." -ForegroundColor Cyan
java Main secvential | Out-Null
$seqSorted = "rezultate_seq_sorted.txt"
Get-Content "rezultate.txt" | Sort-Object | Set-Content $seqSorted

Write-Host "2. Rulare PARALEL (P=$p, Pr=$pr)..." -ForegroundColor Cyan
java Main paralel $p $pr | Out-Null
$parSorted = "rezultate_par_sorted.txt"
Get-Content "rezultate.txt" | Sort-Object | Set-Content $parSorted

Write-Host "3. Comparare fisiere..." -ForegroundColor Yellow
$differences = Compare-Object -ReferenceObject (Get-Content $seqSorted) -DifferenceObject (Get-Content $parSorted)

if ($null -eq $differences) {
    Write-Host "OK: Fisierele sunt identice!" -ForegroundColor Green
} else {
    Write-Host "EROARE: Fisierele sunt diferite!" -ForegroundColor Red
    Write-Host "Diferente gasite:"
    $differences
}

if (Test-Path $seqSorted) { Remove-Item $seqSorted }
if (Test-Path $parSorted) { Remove-Item $parSorted }