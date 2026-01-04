namespace Seminar11.Domain;

internal class EntityToFileMapping
{
    public static Angajat CreateAngajat(string line)
    {
        string[] fields = line.Split(',');
        var angajat = new Angajat()
        {
            ID = fields[0],
            Nume = fields[1],
            VenitPeOra = double.Parse(fields[2]),
            Nivel = (KnowledgeLevel)Enum.Parse(typeof(KnowledgeLevel), fields[3])
        };
        return angajat;
    }


    public static Sarcina CreateSarcina(string line)
    {
        string[] fields = line.Split(',');
        var sarcina = new Sarcina()
        {
            ID = fields[0],
            TipDificultate = (Dificultate)Enum.Parse(typeof(Dificultate), fields[1]),
            NrOreEstimate = int.Parse(fields[2])
        };
        return sarcina;
    }
}