using System.Globalization;

namespace Seminar11.Domain;

internal class Pontaj : Entity<string>
{
    public Angajat Angajat { get; set; }
    public Sarcina Sarcina { get; set; }

    public DateTime Date { get; set; }

    public override string ToString()
    {
        return $"{Angajat} {Sarcina} {Date.ToString("d/M/yyyy", CultureInfo.InvariantCulture)}";
    }
}