namespace Seminar11.Domain;

internal enum Dificultate
{
    Usoara,
    Medie,
    Grea
}

internal class Sarcina : Entity<string>
{
    public Dificultate TipDificultate { get; set; }
    public int NrOreEstimate { get; set; }

    public override string ToString()
    {
        return ID + " " + TipDificultate + " " + NrOreEstimate;
    }
}
