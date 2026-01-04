namespace Seminar11.Domain;

internal enum KnowledgeLevel
{
    Junior,
    Mid,
    Senior
}

internal class Angajat : Entity<string>
{
    public string Nume { get; set; }
    public double VenitPeOra { get; set; }
    public KnowledgeLevel Nivel { get; set; }

    public override string ToString()
    {
        return ID + " " + Nume + " " + VenitPeOra + " " + Nivel;
    }
}
