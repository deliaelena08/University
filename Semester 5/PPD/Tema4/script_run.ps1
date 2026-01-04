# Parametrii scriptului:
# $args[0] -> Modul (secvential sau paralel)
# $args[1] -> P (numar total thread-uri) - optional pentru secvential
# $args[2] -> P_r (numar thread-uri reader) - optional pentru secvential

$mode = $args[0]
$p = $args[1]
$pr = $args[2]
$runs = 10

javac Main.java 2>$null

$javaCmdArgs = @("Main", $mode)
$csvLabel = ""

if ($mode -eq "secvential") {
    $csvLabel = "Secvential,1,1"
    Write-Host "--- Rulare SECVENTIAL de $runs ori ---" -ForegroundColor Cyan
}
elseif ($mode -eq "paralel") {
    if (-not $p -or -not $pr) {
        Write-Error "Pentru modul paralel trebuie sa specifici P si Pr!"
        exit
    }
    $javaCmdArgs += $p
    $javaCmdArgs += $pr
    $csvLabel = "Paralel,$p,$pr"
    Write-Host "--- Rulare PARALEL (P=$p, Pr=$pr) de $runs ori ---" -ForegroundColor Cyan
}
else {
    Write-Error "Mod necunoscut! Foloseste 'secvential' sau 'paralel'."
    exit
}

$suma = 0

for ($i = 1; $i -le $runs; $i++) {
    Write-Host -NoNewline "Rulare $i... "

    # Rulam Java si capturam output-ul
    # Java returneaza doar timpul (ex: 24.123456) pe ultima linie
    $output = java $javaCmdArgs

    # Extragem ultima linie (timpul)
    $timeString = $output | Select-Object -Last 1

    # Convertim string-ul (cu punct) in double, indiferent de regiunea PC-ului tau
    # Folosim InvariantCulture pentru a fi siguri ca "." este separator zecimal
    try {
        $valoare = [double]::Parse($timeString, [System.Globalization.CultureInfo]::InvariantCulture)
    } catch {
        Write-Warning "Eroare la citire valoare: $timeString"
        $valoare = 0
    }

    Write-Host "$valoare ms" -ForegroundColor Green
    $suma += $valoare
}

# Calculam media
$media = $suma / $runs

Write-Host "`n----------------------------------------"
Write-Host "Timp MEDIU ($mode): $media ms" -ForegroundColor Yellow
Write-Host "----------------------------------------`n"

# ---------------------------------------------------------
# Salvare in CSV (rezultate_timpi.csv)
# ---------------------------------------------------------
$csvFile = "rezultate_timpi.csv"

# Daca fisierul nu exista, cream header-ul
if (!(Test-Path $csvFile)) {
    # Header: Mod de rulare, Total Threaduri, Reader Threaduri, Timp Mediu (ms)
    Set-Content $csvFile 'Mod,Total Threads (P),Reader Threads (Pr),Timp Mediu (ms)'
}

# Adaugam linia noua
$csvLine = "$csvLabel,$media"
Add-Content $csvFile $csvLine

Write-Host "Salvat in $csvFile" -ForegroundColor Gray