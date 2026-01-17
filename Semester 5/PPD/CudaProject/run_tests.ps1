# Get arguments
$RUNS = $args[0]
$METHOD = $args[1]
$RESULTS_CSV = "results/execution_results.csv"
$CPP_BINARY = "./cmake-build-debug/CudaProject.exe"

# Validate arguments
if ($args.Count -lt 2) {
    Write-Host "Usage: .\run_tests.ps1 <num_runs> <method>" -ForegroundColor Yellow
    exit
}

# Ensure folders exist
if (!(Test-Path "results")) { New-Item -ItemType Directory -Path "results" | Out-Null }
if (!(Test-Path "outputs")) { New-Item -ItemType Directory -Path "outputs" | Out-Null }

# Initialize CSV header
if (!(Test-Path $RESULTS_CSV)) {
    Set-Content -Path $RESULTS_CSV -Value "N, M, K, Method, Average Execution Time (ms)"
}

$TEST_CASES = @("10 10 3", "1000 1000 3", "10000 10000 3")

foreach ($test_case in $TEST_CASES) {
    $parts = $test_case -split " "
    $N, $M, $K = $parts[0], $parts[1], $parts[2]
    $input_file = "input_$($N)_$($M)_$($K).txt"

    Write-Host "`n--- Testing $input_file with $METHOD ---" -ForegroundColor Cyan

    $total_time = 0.0
    $success = $true

    for ($run = 1; $run -le $RUNS; $run++) {
        $exec_output = & $CPP_BINARY $input_file $METHOD

        if ($LASTEXITCODE -ne 0) {
            Write-Host "  Run ${run}: failed with exit code $LASTEXITCODE" -ForegroundColor Red
            $success = $false
            break
        }

        # Regex check: Find the numeric timing value in the output
        $exec_time = $exec_output | Where-Object { $_ -match '^\d+(\.\d+)?$' } | Select-Object -Last 1

        if ($null -eq $exec_time) {
            Write-Host "  Run ${run}: Could not find timing value in output." -ForegroundColor Red
            $success = $false
            break
        }

        $total_time += [double]$exec_time
        Write-Host "  Run ${run}: $exec_time ms"
    }

    if ($success) {
        $avg_time = [math]::Round($total_time / $RUNS, 4)
        Add-Content -Path $RESULTS_CSV -Value "$N, $M, $K, $METHOD, $avg_time"
        Write-Host ">> Average Time: $avg_time ms" -ForegroundColor Green
    }
}