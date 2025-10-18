# Compara fisiere linie cu linie
param (
    [string]$file1 = "output_seq_java.txt",
    [string]$file2 = "output_par_java.txt"
)

$lines1 = Get-Content $file1
$lines2 = Get-Content $file2

if ($lines1.Length -ne $lines2.Length) {
    Write-Host "Matricele NU sunt egale: diferente in numarul de linii."
    exit
}

$equal = $true
for ($i=0; $i -lt $lines1.Length; $i++) {
    if ($lines1[$i] -ne $lines2[$i]) {
        Write-Host "Diferenta la linia $($i+1):"
        Write-Host "Secvential: $($lines1[$i])"
        Write-Host "Paralel:    $($lines2[$i])"
        $equal = $false
    }
}

if ($equal) {
    Write-Host "Matricele sunt identice!"
} else {
    Write-Host "Matricele NU sunt identice."
}
