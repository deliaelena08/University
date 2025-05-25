using Seminar11.Domain;
using Seminar11.repository;

namespace Seminar11.service;

internal class AngajatService
{
    private IRepository<string, Angajat> repo;

    public AngajatService(IRepository<string, Angajat> repo)
    {
        this.repo = repo;
    }


    public List<Angajat> FindAllAngajati()
    {
        return repo.FindAll().ToList();
    }

    public List<Angajat> GroupedByLevelLINQ()
    {
        return repo.FindAll().ToList().OrderByDescending(x => x.VenitPeOra) 
            .GroupBy(x => x.Nivel) 
            .OrderBy(g => g.Key)
            .SelectMany(g => g) 
            .ToList();
    }

    public List<Angajat> GroupedByLevel()
    {
        List<Angajat> result = repo.FindAll().ToList();
        var res= from ang in result
            orderby ang.Nivel,ang.VenitPeOra descending 
                select ang;
        return res.ToList();
    }
    
}