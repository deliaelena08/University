using Seminar11.Domain;
using Seminar11.repository;
using Seminar11.service;

namespace Seminar11;

internal class Program
{
    private static void Main(string[] args)
    {
        Console.WriteLine("Cerinta 1 (SQL)");
        GetAngajatService().GroupedByLevel().ForEach(Console.WriteLine);
        Console.WriteLine("\nCerinta 1 (LINQ)");
        List<Angajat> angajats = GetAngajatService().GroupedByLevelLINQ();
        angajats.ForEach(angajat => Console.WriteLine(angajat));
        Console.WriteLine("\nCerinta 2 (SQL)");
        foreach (var keyValuePair in GetSarcinaService().FindEstimatedHoursForSarcina())
        {
            Console.WriteLine(keyValuePair.Key.ToString()+"\t"+keyValuePair.Value.ToString());
        }
        Console.WriteLine("\nCerinta 2 (LINQ)");
        foreach (var pereche in GetSarcinaService().FindEstimatedHoursForSarcinaLINQ())
        {
            Console.WriteLine(pereche.Key.ToString()+"\t"+pereche.Value.ToString());
        }
        Console.WriteLine("\nCerinta 3 (SQL)");
        GetPontajService().GroupeByHarnicie().ForEach(x=>Console.WriteLine(x.ToString()));
        Console.WriteLine("\nCerinta 3 (LINQ)");
        GetPontajService().GroupeByHarnicieLINQ().ForEach(x=>Console.WriteLine(x.ToString()));
    }

    private static AngajatService GetAngajatService()
    {
        var fileName = "C:\\Users\\tapuc\\Desktop\\facultate\\Semestrul 3\\MAP\\Seminar11\\Seminar11\\data\\angajati.txt";

        IRepository<string, Angajat> repo1 = new AngajatInFileRepository(fileName);
        var service = new AngajatService(repo1);
        return service;
    }

    private static SarcinaService GetSarcinaService()
    {
        var fileName2 = "C:\\Users\\tapuc\\Desktop\\facultate\\Semestrul 3\\MAP\\Seminar11\\Seminar11\\data\\sarcini.txt";

        IRepository<string, Sarcina> repo1 = new SarcinaInFileRepository(fileName2);
        var service = new SarcinaService(repo1);
        return service;
    }

    private static PontajService GetPontajService()
    {
        var fileName2 = "C:\\Users\\tapuc\\Desktop\\facultate\\Semestrul 3\\MAP\\Seminar11\\Seminar11\\data\\pontaje.txt";

        IRepository<string, Pontaj> repo1 = new PontajInFileRepository(fileName2);
        var service = new PontajService(repo1);
        return service;
    }
}